package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Drake extends Game {

	//Objects needed for every screen
	SpriteBatch batch;
	BitmapFont font;

	//Screen objects
	MainMenuScreen mainMenuScreen;

	//World Parameters
	public final float WORLD_WIDTH = 640;
	public final float WORLD_HEIGHT = 360;

	public static Random random = new Random();

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		mainMenuScreen.resize(width, height);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);
	}
}
