package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {

    final Drake game;
    //private Camera camera; //use Drake Class
    //private Viewport viewport; //use Drake Class
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final GlyphLayout glyphLayoutTitle = new GlyphLayout();

    float timeAfterGameOver = 0;


    public GameOverScreen(final Drake game) {
        this.game = game;

        //camera = new OrthographicCamera(); //use Drake Class Camera
        //viewport = new StretchViewport(); //use Drake Class Viewport
    }

    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(.2f, 0, 0, .8f);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        game.fontTitle.getData().setScale(0.5f);

        glyphLayoutTitle.setText(game.fontTitle, "Game");
        game.fontTitle.draw(game.batch, glyphLayoutTitle, (game.WORLD_WIDTH / 2 - glyphLayoutTitle.width / 2), game.WORLD_HEIGHT * 2 / 3);

        glyphLayoutTitle.setText(game.fontTitle, "Over");
        game.fontTitle.draw(game.batch, glyphLayoutTitle, (game.WORLD_WIDTH / 2 - glyphLayoutTitle.width / 2), game.WORLD_HEIGHT * 2 / 3 - 2 * glyphLayoutTitle.height);

        timeAfterGameOver += deltaTime;
        if (timeAfterGameOver > 2) {
            game.font.getData().setScale(0.5f);
            glyphLayout.setText(game.font, "Press SPACE to Play Again");
            game.font.draw(game.batch, glyphLayout, (game.WORLD_WIDTH / 2 - glyphLayout.width / 2), game.WORLD_HEIGHT / 4);

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        }
        game.batch.end();


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