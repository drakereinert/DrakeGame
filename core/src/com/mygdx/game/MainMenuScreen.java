package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Drake game;
    //private Camera camera; //use Drake Class
    //private Viewport viewport; //use Drake Class
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final GlyphLayout glyphLayoutTitle = new GlyphLayout();


    public MainMenuScreen(final Drake game) {
        this.game = game;

        //camera = new OrthographicCamera(); //use Drake Class Camera
        //viewport = new StretchViewport(); //use Drake Class Viewport


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        glyphLayoutTitle.setText(game.fontTitle, "DRAKE");
        game.fontTitle.draw(game.batch, glyphLayoutTitle, (game.WORLD_WIDTH / 2) - (glyphLayoutTitle.width / 2) , game.WORLD_HEIGHT * 2 / 3);

        game.font.getData().setScale(0.5f);
        glyphLayout.setText(game.font, "Press SPACE to begin");
        game.font.draw(game.batch, glyphLayout, (game.WORLD_WIDTH / 2 - glyphLayout.width / 2), game.WORLD_HEIGHT / 3);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
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