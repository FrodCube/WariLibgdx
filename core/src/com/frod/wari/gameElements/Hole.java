package com.frod.wari.gameElements;

import java.util.ArrayList;
import java.util.Stack;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.frod.wari.Tweener;
import com.frod.wari.WariGame;
import com.frod.wari.screens.GameScreen;

public class Hole {

	public static final int SEED_OFFSET = 10;

	public static Stack<Sprite> seedPool;

	private int x, y;
	private int width, height;
	private Rectangle hitbox;
	private boolean over, clicked;

	private Hole next;

	private ArrayList<Sprite> seeds;

	static {
		seedPool = new Stack<Sprite>();
		for (int i = 0; i < Table.TOTAL_HOLES * Table.STARTING_SEEDS; i++) {
			Sprite s = new Sprite(GameScreen.seed);
			s.setPosition(WariGame.GAME_WIDTH / 2, WariGame.GAME_HEIGHT / 2);
			seedPool.add(s);
		}
	}

	public Hole(int x, int y, int width, int height, int seeds) {
		this.x = x;
		this.y = y;
		this.width = width - 2 * SEED_OFFSET;
		this.height = height - 2 * SEED_OFFSET;
		this.hitbox = new Rectangle(x, y, width, height);
		this.seeds = new ArrayList<Sprite>();
		this.over = false;
		this.clicked = false;
		addSeeds(seeds, true);
	}

	public void render(SpriteBatch batch) {
		GameScreen.font.draw(batch, getSeeds() + "", x + 2, y + 2);

		if (!seeds.isEmpty()) {
			for (Sprite s : seeds) {
				s.draw(batch);
			}
		}
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public int getSeeds() {
		return seeds.size();
	}

	public void empty() {
		if (!seeds.isEmpty()) {
			boolean addedCallback = false;
			TweenCallback callback = new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					clearSeeds();
				}
			};

			for (Sprite s : seeds) {
				Tween t = Tween.to(s, Tweener.POSITION, .09f).target(x + (width + s.getWidth()) / 2, y - 10 + (height + s.getHeight()) / 2).ease(TweenEquations.easeOutExpo);
				if (!addedCallback) {
					t.setCallbackTriggers(TweenCallback.END).setCallback(callback);
					addedCallback = true;
				}
				t.start(Table.tweenManager);
			}
		}
	}

	public void clearSeeds() {
		if (!seeds.isEmpty()) {
			for (Sprite s : seeds) {
				seedPool.push(s);
			}

			seeds.clear();
		}
	}

	public void addSeed(boolean useTween) {

		Sprite s = seedPool.pop();
		int x0 = MathUtils.random(width - (int) s.getWidth()) + SEED_OFFSET;
		int y0 = MathUtils.random(height - (int) s.getHeight()) + SEED_OFFSET;
		float r = MathUtils.random() * 360;

		if (useTween) {
			Tween.to(s, Tweener.POSITION, .5f).target(x + x0, y + y0).ease(TweenEquations.easeInOutQuad).start(Table.tweenManager);
			Tween.to(s, Tweener.ROTATION, .5f).target(r).ease(TweenEquations.easeInOutQuad).start(Table.tweenManager);
		} else {
			s.setPosition(x + x0, y + y0);
			s.rotate(r);
		}

		// TODO antialiasing D:
		seeds.add(s);
	}

	public void addSeeds(int n, boolean useTween) {
		for (int i = 0; i < n; i++) {
			addSeed(useTween);
		}
	}

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

	public boolean isClicked() {
		return clicked;
	}

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Hole getNextHole() {
		return next;
	}

	public void setNextHole(Hole h) {
		this.next = h;
	}

}
