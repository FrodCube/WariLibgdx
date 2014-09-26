package com.frod.wari.gameElements;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frod.wari.Tweener;
import com.frod.wari.WariGame;
import com.frod.wari.screens.GameScreen;

public class Arm extends Sprite {

	public static final int ARM_SIZE = 44;
	public static final int WIRE_HEIGHT = 8;

	private float time = 0;
	private float x0, y0;
	private boolean animate;

	private int seeds;
	private Hole startingHole;
	private Hole nextHole;
	private Table table;

	private final TweenCallback onHoleArrive = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (nextHole == null) {
				seeds = startingHole.getSeeds();
				startingHole.empty();
				nextHole = startingHole.getNextHole();
				startAnimation(false);
			} else {
				seeds--;
				nextHole.addSeed(false);

				if (seeds == 0) {
					reset();
					return;
				}

				if (nextHole.getNextHole() == startingHole) {
					nextHole = nextHole.getNextHole();
				}
				nextHole = nextHole.getNextHole();
			}

			moveUpDownToHole(nextHole).start(Table.tweenManager);
		}
	};

	public Arm(Table table) {
		setPosition((WariGame.GAME_WIDTH - ARM_SIZE) / 2, -ARM_SIZE);
		this.table = table;
	}

	public void update(float delta) {
		if (animate) {
			time += delta;
		}
	}

	public void draw(SpriteBatch batch) {
		x0 = getX();
		y0 = getY();
		if (animate) {
			batch.draw(GameScreen.arm.getKeyFrame(time), x0, y0);
		} else {
			batch.draw(GameScreen.arm.getKeyFrame(0), x0, y0);
		}

		while (y0 > 0) {
			y0 -= WIRE_HEIGHT;
			batch.draw(GameScreen.armWire, x0, y0);
		}
	}

	public void startAnimation(boolean reverse) {
		animate = true;
		time = 0;
		if (reverse) {
			GameScreen.arm.setPlayMode(Animation.PlayMode.REVERSED);
		} else {
			GameScreen.arm.setPlayMode(Animation.PlayMode.NORMAL);
		}
	}

	public void resetAnimation() {
		animate = false;
		time = 0;
		GameScreen.arm.setPlayMode(Animation.PlayMode.NORMAL);
	}

	public void startMove(Hole h) {
		resetAnimation();
		startingHole = h;
		moveUpDownToHole(startingHole).start(Table.tweenManager);
	}

	public void reset() {
		nextHole = startingHole = null;
		startAnimation(true);
		Tween.to(this, Tweener.POSITION, 1.0f).target((WariGame.GAME_WIDTH - ARM_SIZE) / 2, -ARM_SIZE).ease(TweenEquations.easeInOutQuad).start(Table.tweenManager);
		table.setState(Table.State.capturing);
	}

	public Timeline moveUpDownToHole(Hole h) {
		return Timeline.createSequence().push(moveUp()).push(moveToHole(h)).push(moveDown()).setCallbackTriggers(TweenCallback.END).setCallback(onHoleArrive);
	}

	public Tween moveUp() {
		return Tween.to(this, Tweener.POSITION, 0.15f).targetRelative(0, -20).ease(TweenEquations.easeInOutQuad);
	}

	public Tween moveToHole(Hole h) {
		return Tween.to(this, Tweener.POSITION, .4f).target(h.getX(), h.getY() - ARM_SIZE / 2 - 20).ease(TweenEquations.easeInOutQuad);
	}

	public Tween moveDown() {
		return Tween.to(this, Tweener.POSITION, 0.15f).targetRelative(0, 20).ease(TweenEquations.easeInOutQuad);
	}

	public void setSeeds(int seeds) {
		this.seeds = seeds;
	}

}
