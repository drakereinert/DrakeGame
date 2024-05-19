package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class Drake extends Game {

	//Objects needed for every screen
	SpriteBatch batch;
	BitmapFont font;
	BitmapFont fontTitle;

	//Screen objects
	MainMenuScreen mainMenuScreen;
	//GameOverScreen gameOverScreen;

	//World Parameters
	public final float WORLD_WIDTH = 640;
	public final float WORLD_HEIGHT = 360;

	public Camera camera;
	public Viewport viewport;

	public static Random random = new Random();

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		mainMenuScreen.resize(width, height);
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
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
		fontTitle = new BitmapFont();

		//Create a BitmapFont from font file
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("invasion2000/INVASION2000.TTF"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 32;
		fontParameter.borderWidth = 4f;
		fontParameter.color = new Color(1,1,1,.3f);
		fontParameter.borderColor = new Color(0,0,0, 0.2f);
		font = fontGenerator.generateFont(fontParameter);


		FreeTypeFontGenerator.FreeTypeFontParameter fontParameterTitle = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameterTitle.size = 128;
		//fontParameterTitle.borderWidth = 4f;
		fontParameterTitle.color = new Color(.1f,.1f,1,.7f);
		//fontParameterTitle.borderColor = new Color(0,0,0, 0.2f);
		fontParameterTitle.shadowOffsetX = 3;
		fontParameterTitle.shadowOffsetY = 2;
		fontParameterTitle.shadowColor = new Color(.1f, .1f, .8f, .8f);
		fontTitle = fontGenerator.generateFont(fontParameterTitle);

		camera = new OrthographicCamera();
		viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		mainMenuScreen = new MainMenuScreen(this);
		//gameOverScreen = new GameOverScreen(this);
		setScreen(mainMenuScreen);
	}
}
