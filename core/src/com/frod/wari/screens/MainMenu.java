package com.frod.wari.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class MainMenu implements Screen {

	private Stage stage;
	private Table table;
	private Skin skin;

	public MainMenu() {
		// TEMP CODE. JUST TESTING STUFF AND FIGURING OUT HOW SCENE2D UI WORKS
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		table = new Table();
		table.setFillParent(true);
		
		Image background;

		TextButton button = new TextButton("BLAH really long message lulz", skin, "default");
		table.add(button);
		table.row();
		
		Label nameLabel = new Label("Name:", skin);
	    TextField nameText = new TextField("", skin);
	    Label addressLabel = new Label("Address:", skin);
	    TextField addressText = new TextField("", skin);
	    
	    table.add(nameLabel);
	    table.add(nameText).width(200);
	    table.row();
	    table.add(addressLabel);
	    table.add(addressText).width(200);

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
	}

}
