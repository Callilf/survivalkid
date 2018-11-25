package com.calliltbn.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.calliltbn.SurvivalKidGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Survival Kid";
		config.width = 854;
		config.height = 480;
		config.fullscreen = false;
		config.foregroundFPS = 60;
		new LwjglApplication(new SurvivalKidGame(), config);
	}
}
