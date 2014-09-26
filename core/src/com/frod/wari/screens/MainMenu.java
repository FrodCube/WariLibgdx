package com.frod.wari.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frod.wari.WariGame;

public class MainMenu implements Screen {
	
	private WariGame wari;
	private Stage stage;
	private Table table;
	private Skin skin;

	public MainMenu(WariGame game) {
		this.wari = game;
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		table = new Table();
		table.setFillParent(true);
		
		Image background;
		
		Label title = new Label("WARI", skin);	    
	    table.add(title).spaceBottom(150).row();
	    
		TextButton buttonStart = new TextButton("New Game", skin, "default");
		buttonStart.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				wari.setScreen(new GameScreen(wari));
			}
		});
		table.add(buttonStart).padTop(40);
		table.row();
		
		TextButton buttonExit = new TextButton("Exit", skin, "default");
		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO this might not work on mobile / cause problems with static texture variables
				Gdx.app.exit();
			}
		});
		table.add(buttonExit).padTop(40);
		table.row();

		stage.addActor(table);
		
		table.debug();
	}

	@Override
	public void render(float delta) {
		stage.act(delta);

		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
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
		// TODO
	}

}
