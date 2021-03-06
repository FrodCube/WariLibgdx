package com.frod.wari.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.frod.wari.WariGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WariGame.SCREEN_WIDTH;
		config.height = WariGame.SCREEN_HEIGHT;
		new LwjglApplication(new WariGame(), config);
	}
}
