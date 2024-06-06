package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Locale;

public class HighScoreScreen implements Screen {

    final Drake game;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final GlyphLayout glyphLayoutTitle = new GlyphLayout();

    private float menuDelayTime = 1f;
    private float timeSinceMenuChange = 0;

    private int currentItem = 0;
    private final String[] menuItems = new String[] {"Main Menu"};
    private boolean upKeyAlreadyPressed = false;
    private boolean downKeyAlreadyPressed = false;

    private int[] highScores;
    private String[] names;

    public HighScoreScreen(final Drake game) {
        this.game = game;
        Save.load();
        highScores = Save.gd.getHighScores();
        names = Save.gd.getNames();
    }

    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        timeSinceMenuChange += deltaTime;
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        game.fontTitle.getData().setScale(.3f);
        glyphLayoutTitle.setText(game.fontTitle, "High Scores");
        game.fontTitle.draw(game.batch, glyphLayoutTitle, (game.WORLD_WIDTH / 2) - (glyphLayoutTitle.width / 2) , game.WORLD_HEIGHT - glyphLayout.height * 1.01f);

        game.font.getData().setScale(0.5f);

        String scoreString;
        for(int i = 0; i < highScores.length; i++) {
            scoreString = String.format("%2d. %7d %s", i + 1, highScores[i], names[i]);
            glyphLayout.setText(game.font, scoreString);
            game.font.draw(game.batch, String.format(scoreString), game.WORLD_WIDTH / 2 - glyphLayout.width / 2, game.WORLD_HEIGHT * 4 / 5 - 25 * i);
        }


        // List of options for user to select at the bottom of the screen
/*        for(int i = 0; i < menuItems.length; i++) {
            if(currentItem == i) game.font.setColor(Color.RED);
            else game.font.setColor(Color.WHITE);
            glyphLayout.setText(game.font, menuItems[i]);
            game.font.draw(game.batch, glyphLayout, (game.WORLD_WIDTH / 2 - glyphLayout.width / 2), (game.WORLD_HEIGHT / 5) - (25 * i));
        }
*/

        boolean upIsPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        if (upIsPressed && !upKeyAlreadyPressed && currentItem > 0) {
            currentItem--;
        }
        upKeyAlreadyPressed = upIsPressed;


        boolean downIsPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        if(downIsPressed && !downKeyAlreadyPressed && currentItem < menuItems.length - 1) {
            currentItem++;
        }
        downKeyAlreadyPressed = downIsPressed;

        if ((Gdx.input.isKeyPressed(Input.Keys.Z) || (Gdx.input.isKeyPressed(Input.Keys.V))) && timeSinceMenuChange > menuDelayTime) {
            game.menuMusic.pause();
            if(currentItem == 0) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        }

        //glyphLayout.setText(game.font, "Press START to begin");
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
        game.menuMusic.dispose();
    }
}