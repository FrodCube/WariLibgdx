package com.frod.wari;

import com.badlogic.gdx.Game;
import com.frod.wari.screens.MainMenu;

public class WariGame extends Game {

	public static final int GAME_WIDTH = 400;
	public static final int GAME_HEIGHT = 300;
	public static final int SCALE = 2;
	public static final int SCREEN_WIDTH = GAME_WIDTH * SCALE;
	public static final int SCREEN_HEIGHT = GAME_HEIGHT * SCALE;

	@Override
	public void create() {
		setScreen(new MainMenu(this));
	}

}
