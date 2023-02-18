package com.game.student;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.student.Simulator;
import com.game.student.Const;

public class DesktopLauncher {
	// RUN THIS CODE TO RUN OUR ORIGINAL CODE
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Const.V_WIDTH / 2;
		config.height = Const.V_HEIGHT / 2;
		config.foregroundFPS = 100;
		config.resizable = true;
		config.addIcon("icon_128.png", Files.FileType.Internal);
		config.addIcon("icon_32.png", Files.FileType.Internal);

		new LwjglApplication(new Simulator(), config);
	}
}
