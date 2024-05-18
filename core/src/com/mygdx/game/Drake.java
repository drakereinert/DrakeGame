package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Drake extends Game {

	GameScreen gameScreen;
	MainMenuScreen mainMenuScreen;

	public static Random random = new Random();

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		gameScreen.resize(width,height);

	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}

	@Override
	public void create() {
		gameScreen = new GameScreen();
		mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);
	}
}
