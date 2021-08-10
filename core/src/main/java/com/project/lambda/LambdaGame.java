package com.project.lambda;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class LambdaGame extends BaseGame {
	@Override
	public void create() {
		super.create();
		setScreen(new LevelScreen());
	}
}