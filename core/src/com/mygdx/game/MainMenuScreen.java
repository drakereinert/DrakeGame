package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final Drake game;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final GlyphLayout glyphLayoutTitle = new GlyphLayout();

    float menuDelayTime;
    float timeSinceMenuChange;

    private int currentItem;
    private String[] menuItems;
    private boolean upKeyAlreadyPressed;
    private boolean downKeyAlreadyPressed;

    public MainMenuScreen(final Drake game) {
        this.game = game;
        game.p1Score = 0;
        game.p2Score = 0;
        menuDelayTime = .5f;
        timeSinceMenuChange = 0;
        game.menuMusic.play();
        menuItems = new String[] {
                "Play", "High Scores", "Quit"
        };
        currentItem = 0;
        upKeyAlreadyPressed = false;
        downKeyAlreadyPressed = false;
    }

    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        timeSinceMenuChange += deltaTime;
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        game.fontTitle.getData().setScale(1f);
        glyphLayoutTitle.setText(game.fontTitle, "DRAKE");
        game.fontTitle.draw(game.batch, glyphLayoutTitle, (game.WORLD_WIDTH / 2) - (glyphLayoutTitle.width / 2) , game.WORLD_HEIGHT * 2 / 3);

        game.font.getData().setScale(0.5f);
        for(int i = 0; i < menuItems.length; i++) {
            if(currentItem == i) game.font.setColor(Color.RED);
            else game.font.setColor(Color.WHITE);
            glyphLayout.setText(game.font, menuItems[i]);
            game.font.draw(game.batch, glyphLayout, (game.WORLD_WIDTH / 2 - glyphLayout.width / 2), (game.WORLD_HEIGHT / 3) - (25 * i));
        }


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
                game.setScreen(new GameScreen(game));
                dispose();
            } else if (currentItem == 1) {
                game.setScreen(new HighScoreScreen(game));
                dispose();
            } else if (currentItem == 2) {
                Gdx.app.exit();
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