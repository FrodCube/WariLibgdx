package com.frod.wari.gameElements;

import java.util.Arrays;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.frod.wari.Tweener;
import com.frod.wari.screens.GameScreen;

public class Table implements InputProcessor {

	public static final int TOTAL_HOLES = 12;
	public static final int HOLES_PER_PLAYER = TOTAL_HOLES / 2;
	public static final int OFFSET_X = 23;
	public static final int OFFSET_Y = 43;
	public static final int DISTANCE_X = 14;
	public static final int GAP_X = 10;
	public static final int DISTANCE_Y = 124;
	public static final int HOLE_SIZE = 46;
	public static final int STARTING_SEEDS = 4;
	public static final int SCORE_TO_WIN = HOLES_PER_PLAYER * STARTING_SEEDS + 1;

	public static TweenManager tweenManager;

	public static enum State {
		playerInput, moveAnimation, capturing, endMove;
	}

	/*
	 * 								-- TODO --
	 * 
	 * MISSING RULES:
	 *  . stale? D:
	 * 
	 * FEATURES:
	 *  . player versus PC
	 *  . online player versus player
	 *  
	 * MENUS:
	 *  . main menu
	 *  . how to
	 *  . options? statistics? achievements?
	 *  . physics seeds on cup on main menu
	 *  
	 *								-- TODO --
	 */

	private Vector3 mouse;
	private boolean mouseDown;

	private int lastHole;
	private Hole[] holes;
	private int[] prevHoles;
	private Player currentPlayer;
	private Sprite selectionBox;
	private Arm arm;
	private State state;

	public Table() {
		currentPlayer = Player.PLAYER1;
		holes = new Hole[TOTAL_HOLES + 2];
		prevHoles = new int[TOTAL_HOLES + 2];
		mouse = new Vector3();
		mouseDown = false;
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new Tweener());
		setState(State.playerInput);

		// int[] a = {0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0};

		int x = OFFSET_X + (HOLE_SIZE + DISTANCE_X) * (HOLES_PER_PLAYER - 1) + GAP_X;
		int y = OFFSET_Y + (HOLE_SIZE + DISTANCE_Y);
		for (int i = 0; i < TOTAL_HOLES; i++) {
			holes[i] = new Hole(x, y, HOLE_SIZE, HOLE_SIZE, STARTING_SEEDS);
			// holes[i] = new Hole(x, y, HOLE_SIZE, HOLE_SIZE, a[i]);

			if (i != 0) {
				holes[i - 1].setNextHole(holes[i]);
			}

			if (i < HOLES_PER_PLAYER - 1) {
				x -= HOLE_SIZE + DISTANCE_X;
				if (i == 2) {
					x -= GAP_X;
				}
			} else if (i != HOLES_PER_PLAYER - 1) {
				x += HOLE_SIZE + DISTANCE_X;
				if (i == 8) {
					x += GAP_X;
				}
			}

			if (i == HOLES_PER_PLAYER - 1) {
				y -= HOLE_SIZE + DISTANCE_Y;
			}
		}

		holes[TOTAL_HOLES - 1].setNextHole(holes[0]);

		holes[TOTAL_HOLES] = new Hole(50, 134, 130, 46, 0);
		Player.PLAYER1.setHole(holes[TOTAL_HOLES]);
		holes[TOTAL_HOLES + 1] = new Hole(220, 134, 130, 46, 0);
		Player.PLAYER2.setHole(holes[TOTAL_HOLES + 1]);

		selectionBox = new Sprite(GameScreen.selectionBox);
		arm = new Arm(this);

		Tween.to(selectionBox, Tweener.SCALE, .5f).ease(TweenEquations.easeNone).target(1.4f, 1.4f).repeatYoyo(Tween.INFINITY, 0).start(tweenManager);
	}

	public void update(OrthographicCamera camera, float delta) {
		mouse.x = Gdx.input.getX();
		mouse.y = Gdx.input.getY();
		camera.unproject(mouse);
		tweenManager.update(delta);
		arm.update(delta);

		if (state == State.playerInput) {
			if (!mouseDown) {
				for (int i = currentPlayer.firstHole(); i < currentPlayer.lastHole(); i++) {
					if (holes[i].getHitbox().contains(mouse.x, mouse.y)) {
						holes[i].setOver(true);
					} else {
						holes[i].setOver(false);
					}
				}
			}
		} else if (state == State.moveAnimation) {

			// nothing happens? :o :O :o

		} else if (state == State.capturing) {
			if (!isLegalState()) {
				// move didn't place seeds on his side
				Gdx.app.log("ILLEGAL MOVE", "NO GRAND SLAM THO");
				undoMove();
				setState(State.playerInput);
				return;
			}

			// TODO test down left to up left
			int captured;
			while (!currentPlayer.isMyHole(lastHole) && (holes[lastHole].getSeeds() == 2 || holes[lastHole].getSeeds() == 3)) {
				captured = holes[lastHole].getSeeds();
				holes[lastHole].clearSeeds();
				currentPlayer.addScore(captured);
				lastHole--;
				if (lastHole < 0) {
					lastHole = TOTAL_HOLES - 1;
				}
			}

			setState(State.endMove);

		} else if (state == State.endMove) {

			if (isLegalState()) {

				if (isGameOver()) {
					// TODO
					return;
				}
				if (currentPlayer == Player.PLAYER1) {
					currentPlayer = Player.PLAYER2;
				} else {
					currentPlayer = Player.PLAYER1;
				}

			} else {
				// TODO display splash screen
				// GRAND SLAM (should be)
				Gdx.app.log("GRAND SLAM!", "woho!");
				undoMove();
			}

			setState(State.playerInput);
		}
	}

	public void render(SpriteBatch batch) {
		for (int i = 0; i < holes.length; i++) {
			Hole h = holes[i];
			h.render(batch);
			if (h.isOver() || h.isClicked()) {
				selectionBox.setPosition(h.getX(), h.getY());
				selectionBox.draw(batch);
			}

			GameScreen.font.draw(batch, i + "", h.getX() + 22, h.getY() + 44);
		}

		arm.draw(batch);
	}

	private void performMove(int n) {
		if (holes[n].getSeeds() == 0) {
			return;
		}

		for (int i = 0; i < prevHoles.length; i++) {
			prevHoles[i] = holes[i].getSeeds();
		}

		setState(State.moveAnimation);
		lastHole = getLastHoleIndex(n, holes[n].getSeeds());
		arm.startMove(holes[n]);
		Gdx.app.log("next is", Arrays.toString(simulateMove(currentPlayer, n)));
	}

	private boolean isLegalState() {
		for (int i = currentPlayer.getOpponent().firstHole(); i < currentPlayer.getOpponent().lastHole(); i++) {
			if (holes[i].getSeeds() != 0) {
				return true;
			}
		}
		return false;
	}

	private boolean isGameOver() {
		//		if (currentPlayer.getScore() >= SCORE_TO_WIN) {
		//			// PLAYER WON
		//			Gdx.app.log(currentPlayer.toString(), "WON");
		//			return true;
		//		}

		if (currentPlayer.getScore() == SCORE_TO_WIN - 1 && currentPlayer.getOpponent().getScore() == SCORE_TO_WIN - 1) {
			// DRAW
			Gdx.app.log("GAME OVER", "DRAW");
			return true;
		}

		// FIXME looks bugged somewhere
		// TODO simulate each possible move by (?) and see if that is legal
		int[] seeds;
		Player p = currentPlayer.getOpponent();
		for (int i = p.firstHole(); i < p.lastHole(); i++) {
			seeds = simulateMove(p, i);

			for (int j = currentPlayer.firstHole(); j < currentPlayer.lastHole(); j++) {
				if (seeds[j] != 0) {
					Gdx.app.log("Simulation found a valid move at", i + " seed in " + j);
					return false;
				}
			}
		}

		Gdx.app.log("NO VALID MOVES", "GAMEEEEEEE OVVEEEEEER");
		return true;
	}

	public int[] simulateMove(Player p, int n) {
		int[] ret = new int[TOTAL_HOLES];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = holes[i].getSeeds();
		}

		int seeds = ret[n];
		if (seeds == 0) {
			return ret;
		}

		int index = n;
		ret[index] = 0;
		do {
			index++;

			if (index == TOTAL_HOLES) {
				index = 0;
			}

			if (index != n) {
				seeds--;
				ret[index]++;
			}

		} while (seeds > 0);

		while (!p.isMyHole(index) && (ret[index] == 2 || ret[index] == 3)) {
			ret[index] = 0;
			index--;
			if (index < 0) {
				index = TOTAL_HOLES - 1;
			}
		}

		return ret;
	}

	private void undoMove() {
		Gdx.app.log("ERROR! ILLEGAL MOVE!", "ERRORUZNBOOBS");

		for (int i = 0; i < prevHoles.length; i++) {
			holes[i].clearSeeds();
		}

		for (int i = 0; i < prevHoles.length; i++) {
			holes[i].addSeeds(prevHoles[i], true);
		}
	}

	private int getLastHoleIndex(int n, int seeds) {
		return (n + seeds - seeds / TOTAL_HOLES) % TOTAL_HOLES;
	}

	public void setState(State s) {
		this.state = s;
	}

	// ----------------------------- \\
	// ----------------------------- \\
	// ---- INPUT HANDLING CODE ---- \\
	// ----------------------------- \\
	// ----------------------------- \\

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mouseDown = true;
		for (int i = currentPlayer.firstHole(); i < currentPlayer.lastHole(); i++) {
			if (button == Input.Buttons.LEFT && holes[i].isOver()) {
				holes[i].setClicked(true);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mouseDown = false;
		for (int i = currentPlayer.firstHole(); i < currentPlayer.lastHole(); i++) {
			if (holes[i].isClicked() && holes[i].getHitbox().contains(mouse.x, mouse.y)) {
				performMove(i);
				holes[i].setClicked(false);
				holes[i].setOver(false);
				return true;
			} else {
				holes[i].setClicked(false);
				holes[i].setOver(false);
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
