package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Locale;

public class GameOverScreen implements Screen {

    final Drake game;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final GlyphLayout glyphLayoutTitle = new GlyphLayout();

    float timeAfterGameOver = 0;

    float scoreMargin, LeftXPlayer1, RightXPlayer1, CenterXPlayer1, Row1Y, Row2Y, SectionWidth, LeftXPlayer2, RightXPlayer2, CenterXPlayer2;


    public GameOverScreen(final Drake game) {
        this.game = game;
        game.menuMusic.play();
        game.menuMusic.setPosition(43f);
        prepareScoreDisplay();
    }

    private void prepareScoreDisplay() {
        //scale the font to fit world
        game.font.getData().setScale(0.5f);
        int screenSections = 6;
        //Calculate hud margins, etc.
        scoreMargin = game.font.getCapHeight() / 2;
        //Player 1
        LeftXPlayer1 = scoreMargin;
        RightXPlayer1 = game.WORLD_WIDTH * 2/ screenSections - LeftXPlayer1;
        CenterXPlayer1 = game.WORLD_WIDTH / screenSections;
        //Player 2
        LeftXPlayer2 = game.WORLD_WIDTH * (screenSections - 3)/ screenSections + scoreMargin;
        RightXPlayer2 = game.WORLD_WIDTH * (screenSections - 1)/ screenSections - LeftXPlayer2;
        CenterXPlayer2 = game.WORLD_WIDTH * (screenSections -2)/ screenSections;

        Row1Y = game.WORLD_HEIGHT - scoreMargin;
        Row2Y = Row1Y - scoreMargin - game.font.getCapHeight();
        SectionWidth = game.WORLD_WIDTH / screenSections;
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

        game.font.draw(game.batch, "Player 1 Score", CenterXPlayer1, Row1Y, SectionWidth, Align.center, false);
        game.font.draw(game.batch, "Player 2 Score", CenterXPlayer2, Row1Y, SectionWidth, Align.center, false);
        game.font.draw(game.batch, String.format(Locale.getDefault(), "%06d", game.p1Score), CenterXPlayer1, Row2Y, SectionWidth, Align.center, false);
        game.font.draw(game.batch, String.format(Locale.getDefault(), "%06d", game.p2Score), CenterXPlayer2, Row2Y, SectionWidth, Align.center, false);


        timeAfterGameOver += deltaTime;
        if (timeAfterGameOver > 2) {
            game.font.getData().setScale(0.5f);
            glyphLayout.setText(game.font, "Press START to Play Again");
            game.font.draw(game.batch, glyphLayout, (game.WORLD_WIDTH / 2 - glyphLayout.width / 2), game.WORLD_HEIGHT / 4);

            if (Gdx.input.isKeyPressed(Input.Keys.E) || (Gdx.input.isKeyPressed(Input.Keys.Y))) {
                game.menuMusic.stop();
                game.setScreen(new GameScreen(game));
                game.p1Score = 0;
                game.p2Score = 0;
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