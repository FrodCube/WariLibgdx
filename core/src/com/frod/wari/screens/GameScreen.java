package com.frod.wari.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.frod.wari.WariGame;
import com.frod.wari.gameElements.Table;

public class GameScreen implements Screen {
	
	private WariGame wari;
	private Table table;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	public static Texture texture;
	public static TextureRegion background;
	public static TextureRegion selectionBox;
	public static TextureRegion seed;
	public static Animation arm;
	public static TextureRegion armWire;
	public static BitmapFont font;

	// TODO debug stuff
	private float timer = 0;
	private float fps = 0;

	public GameScreen(WariGame game) {
		wari = game;
		Gdx.app.log("Game", "Loading...");
		loadAssets();

		table = new Table();
		Gdx.input.setInputProcessor(table);
		camera = new OrthographicCamera();
		camera.setToOrtho(true, WariGame.GAME_WIDTH, WariGame.GAME_HEIGHT);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		Gdx.app.log("Game", "Done Loading!");
	}

	private void loadAssets() {
		texture = new Texture(Gdx.files.internal("wariBoard.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		background = new TextureRegion(texture, 0, 0, WariGame.GAME_WIDTH, WariGame.GAME_HEIGHT);
		background.flip(false, true);
		selectionBox = new TextureRegion(texture, WariGame.GAME_WIDTH, 0, 44, 44);
		selectionBox.flip(false, true);
		seed = new TextureRegion(texture, WariGame.GAME_WIDTH + 44, 0, 8, 8);
		seed.flip(false, true);

		TextureRegion[] frames = new TextureRegion[4];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = new TextureRegion(texture, i * 44, WariGame.GAME_HEIGHT, 44, 44);
			frames[i].flip(false, true);
		}
		arm = new Animation(.03f, frames);
		armWire = new TextureRegion(texture, 0, WariGame.GAME_HEIGHT + 44, 44, 8);
		armWire.flip(false, true);

		font = new BitmapFont(Gdx.files.internal("font.fnt"));
		font.setScale(.2f, -.2f);
	}

	@Override
	public void render(float delta) {
		camera.update();
		table.update(camera, delta);

		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(background, 0, 0);
		font.draw(batch, "FPS: " + fps, 1, 1);
		table.render(batch);
		batch.end();

		// TODO debug code
		timer += delta;
		if (timer > 1) {
			timer = 0;
			fps = (1 / delta);
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// table ? TODO
		batch.dispose();
		texture.dispose();
		font.dispose();
	}

}
