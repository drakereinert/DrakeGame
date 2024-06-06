package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Locale;

public class GameOverScreen implements Screen {

    final Drake game;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final GlyphLayout glyphLayoutTitle = new GlyphLayout();

    private boolean p1NewHighScore;
    private boolean p2NewHighScore;
    private char[] newName;
    private int currentChar;

    private final String[] menuItems = new String[] {"Main Menu"};
    private int currentItem = 0;
    float timeAfterGameOver = 0;

    float scoreMargin, LeftXPlayer1, RightXPlayer1, CenterXPlayer1, Row1Y, Row2Y, Row3Y, SectionWidth, LeftXPlayer2, RightXPlayer2, CenterXPlayer2;


    public GameOverScreen(final Drake game) {
        this.game = game;
        game.menuMusic.play();
        game.menuMusic.setPosition(43f);
        prepareScoreDisplay();
        p1NewHighScore = Save.gd.isHighScore(Save.gd.getTempScorep1());
        if (p1NewHighScore) {
            newName = new char[] {'A', 'A', 'A'};
            currentChar = 0;
        }


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
        Row3Y = Row2Y - scoreMargin - game.font.getCapHeight();
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




        game.font.setColor(1,1,1,.7f);
        game.font.draw(game.batch, "Player 1 Score", CenterXPlayer1, Row1Y, SectionWidth, Align.center, false);
        game.font.draw(game.batch, "Player 2 Score", CenterXPlayer2, Row1Y, SectionWidth, Align.center, false);
        game.font.draw(game.batch, String.format(Locale.getDefault(), "%6d", game.p1Score), CenterXPlayer1, Row2Y, SectionWidth, Align.center, false);
        game.font.draw(game.batch, String.format(Locale.getDefault(), "%6d", game.p2Score), CenterXPlayer2, Row2Y, SectionWidth, Align.center, false);


        timeAfterGameOver += deltaTime;
        if (timeAfterGameOver > 2) {
            game.font.getData().setScale(0.5f);
            for(int i = 0; i < menuItems.length; i++) {
                if(currentItem == i) game.font.setColor(Color.RED);
                else game.font.setColor(Color.WHITE);
                glyphLayout.setText(game.font, menuItems[i]);
                game.font.draw(game.batch, glyphLayout, (game.WORLD_WIDTH / 2 - glyphLayout.width / 2), (game.WORLD_HEIGHT / 4) - (25 * i));
            }

            if (Gdx.input.isKeyPressed(Input.Keys.Z) || (Gdx.input.isKeyPressed(Input.Keys.V))) {
                game.menuMusic.stop();
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        }

        if(!p1NewHighScore && !p2NewHighScore) {
            game.batch.end();
            return;
        }

        game.font.setColor(1,1,1,1);
        if(p1NewHighScore) game.font.draw(game.batch, String.format(Locale.getDefault(), "%s", "NEW HIGH SCORE"), CenterXPlayer1, Row3Y, SectionWidth, Align.center, false);
        if(p2NewHighScore) game.font.draw(game.batch, String.format(Locale.getDefault(), "%s", "NEW HIGH SCORE"), CenterXPlayer2, Row3Y, SectionWidth, Align.center, false);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}