package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

class GameScreen implements Screen {

    final Drake game;

    //screen
    //private Camera camera; //Use Drake Class Camerea
    //private Viewport viewport; //Use Drake Class Viewport


    //graphics
    //private SpriteBatch batch; No longer needed, batch object created in Drake class.
    private TextureAtlas textureAtlas;
    private Texture explosionTexture;

    //private Texture background;
    private TextureRegion[] backgrounds;
    private float backgroundHeight;   //height of background in World units

    private TextureRegion player1shipTextureRegion, player2shipTextureRegion, playerShieldTextureRegion,
            enemyShipTextureRegion, enemyShieldTextureRegion, player1LaserTextureRegion, player2LaserTextureRegion,
            enemyLaserTextureRegion;

    //timing
    private float[] backgroundOffsets = {0,0,0,0};
    private float backgroundMaxScrollingSpeed;
    private float timeBetweenEnemySpawns = 3f;
    private float enemySpawnTimer = 0;


    //world parameters
    //private final float WORLD_WIDTH = 640;
    //private final float WORLD_HEIGHT = 360;

    //game objects
    private PlayerShip player1Ship;
    private PlayerShip player2Ship;
    private LinkedList<EnemyShip> enemyShipLinkedList;

    boolean spaceAlreadyPressed = false;

    private LinkedList<Laser> player1LaserList;
    private LinkedList<Laser> player2LaserList;
    private LinkedList<Laser> enemyLaserList;
    private LinkedList<Explosion> explosionList;

    private int p1Score = 0;
    private int p2Score = 0;

    //Heads-Up Display
    //BitmapFont font; //Moved to Drake Class
    float hudVerticalMargin, hudLeftX, hudRightX, hudCenterX, hudRow1Y, hudRow2Y, hudSectionWidth;

//------------------------------------------------------------------------

    GameScreen(final Drake game) {
        this.game = game;
        //this.game.camera = new OrthographicCamera();
        //this.game.viewport = new StretchViewport(this.game.WORLD_WIDTH, this.game.WORLD_HEIGHT, this.game.camera);

        //set up the texture atlas
        textureAtlas = new TextureAtlas("images.atlas");

        //setting up the background
        backgrounds = new TextureRegion[4];
        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");

        backgroundHeight = this.game.WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed = (float)(this.game.WORLD_HEIGHT)/ 4;

        //initialize texture regions
        player1shipTextureRegion = textureAtlas.findRegion("playerShip1_blue");
        player2shipTextureRegion = textureAtlas.findRegion("playerShip1_red");
        enemyShipTextureRegion = textureAtlas.findRegion("enemyshipscout1");
        playerShieldTextureRegion = textureAtlas.findRegion("playershield");
        enemyShieldTextureRegion = textureAtlas.findRegion("enemyshield");
        player1LaserTextureRegion = textureAtlas.findRegion("p1laser");
        player2LaserTextureRegion = textureAtlas.findRegion("p2laser");
        enemyLaserTextureRegion = textureAtlas.findRegion("enemylaser");
        enemyShieldTextureRegion.flip(false, true);
        explosionTexture = new Texture("playerexplosion.png");


        //set up game objects
        player1Ship = new PlayerShip(this.game.WORLD_WIDTH / 3, this.game.WORLD_HEIGHT/4, 20, 20,
                100, 3,
                1.4f, 8, 250, 0.1f,
                player1shipTextureRegion, playerShieldTextureRegion, player1LaserTextureRegion);

        player2Ship = new PlayerShip(this.game.WORLD_WIDTH * 2/3, this.game.WORLD_HEIGHT/4, 20, 20,
                100, 3,
                1.4f, 8, 250, 0.5f,
                player2shipTextureRegion, playerShieldTextureRegion, player2LaserTextureRegion);


        enemyShipLinkedList = new LinkedList<>();



        player1LaserList = new LinkedList<>();
        player2LaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();
        explosionList = new LinkedList<>();

        //batch = new SpriteBatch(); No longer needed, batch object created in Drake class.

        prepareHud();
    }

//------------------------------------------------------------------------

    private void prepareHud() {
        //Create a BitmapFont from font file
/*
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("invasion2000/INVASION2000.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 128;
        fontParameter.borderWidth = 4f;
        fontParameter.color = new Color(1,1,1,0.3f);
        fontParameter.borderColor = new Color(0,0,0, 0.3f);

        game.font = fontGenerator.generateFont(fontParameter);
*/
        //scale the font to fit world
        game.font.getData().setScale(0.5f);

        //Calculate hud margins, etc.
        hudVerticalMargin = game.font.getCapHeight() / 2;
        hudLeftX = hudVerticalMargin;
        hudRightX = game.WORLD_WIDTH * 2/3 - hudLeftX;
        hudCenterX = game.WORLD_WIDTH / 3;
        hudRow1Y = game.WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - game.font.getCapHeight();
        hudSectionWidth = game.WORLD_WIDTH / 3;
    }

//------------------------------------------------------------------------

    @Override
    public void dispose() {
    }

//------------------------------------------------------------------------

    @Override
    public void hide() {
    }

//------------------------------------------------------------------------

    @Override
    public void resume() {

    }

//------------------------------------------------------------------------

    @Override
    public void pause() {
    }

//------------------------------------------------------------------------

    @Override
    public void resize(int width, int height) {
         game.viewport.update(width, height, true);
         game.batch.setProjectionMatrix(game.camera.combined);
    }

//------------------------------------------------------------------------

    @Override
    public void render(float deltaTime) {

        game.batch.begin();

        //scrolling background
        renderBackground(deltaTime);

        detectInput(deltaTime);
        player1Ship.update(deltaTime);
        player2Ship.update(deltaTime);

        spawnEnemyShips(deltaTime);

        ListIterator<EnemyShip> enemyShipListIterator = enemyShipLinkedList.listIterator();
        while (enemyShipListIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            moveEnemy(enemyShip, deltaTime);
            enemyShip.update(deltaTime);
            enemyShip.draw(game.batch);
        }


        //player ships
        player1Ship.draw(game.batch);
        player2Ship.draw(game.batch);

        //lasers
        renderLasers(deltaTime);

        //detect collisions between lasers and ships
        detectCollisions();

        //explosions
        renderExplosions(deltaTime);

        //hud rendering
        updateAndRenderHUD();

        game.batch.end();
    }

//------------------------------------------------------------------------

    private void updateAndRenderHUD() {
        //Render TOP row
        game.font.draw(game.batch, "Score", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        game.font.draw(game.batch, "Shield", hudCenterX, hudRow1Y, hudSectionWidth, Align.center, false);
        game.font.draw(game.batch, "Lives", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false);

        //Render 2nd Row
        game.font.draw(game.batch, String.format(Locale.getDefault(), "%06d", p1Score), hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false);
        game.font.draw(game.batch, String.format(Locale.getDefault(), "%02d", player1Ship.shield), hudCenterX, hudRow2Y, hudSectionWidth, Align.center, false);
        game.font.draw(game.batch, String.format(Locale.getDefault(), "%02d", player1Ship.lives), hudRightX, hudRow2Y, hudSectionWidth, Align.right, false);
    }

//------------------------------------------------------------------------


    private void spawnEnemyShips(float deltaTime) {
        enemySpawnTimer += deltaTime;

        if(enemySpawnTimer > timeBetweenEnemySpawns) {
            enemyShipLinkedList.add(new EnemyShip(
                    (int) (Drake.random.nextDouble() * game.WORLD_WIDTH) + 20,
                    (int) (Drake.random.nextDouble() * (.5f * game.WORLD_HEIGHT) + (.5f * game.WORLD_HEIGHT) - 20),
                    20, 20,
                    120, 1,
                    1.2f, 14, 150, 0.8f,
                    enemyShipTextureRegion, enemyShieldTextureRegion, enemyLaserTextureRegion));
            enemySpawnTimer -= timeBetweenEnemySpawns;
            if (timeBetweenEnemySpawns > 2) {
                timeBetweenEnemySpawns -= .1f;
            } else if (timeBetweenEnemySpawns > 1) {
                timeBetweenEnemySpawns -= .05f;
            }
        }
    }


//------------------------------------------------------------------------

    private void detectInput(float deltaTime) {
        //keyboard input

        //strategy: determine the max distance the ship can move
        //check each key that matters and move accordingly

        float leftLimit, rightLimit, upLimit, downLimit;
        float p2leftLimit, p2rightLimit, p2upLimit, p2downLimit;


        leftLimit = -player1Ship.boundingBox.x;
        downLimit = -player1Ship.boundingBox.y;
        rightLimit = game.WORLD_WIDTH - player1Ship.boundingBox.x - player1Ship.boundingBox.width;
        upLimit = (float)game.WORLD_HEIGHT/2 - player1Ship.boundingBox.y - player1Ship.boundingBox.height;

        p2leftLimit = -player2Ship.boundingBox.x;
        p2downLimit = -player2Ship.boundingBox.y;
        p2rightLimit = game.WORLD_WIDTH - player2Ship.boundingBox.x - player2Ship.boundingBox.width;
        p2upLimit = (float)game.WORLD_HEIGHT/2 - player2Ship.boundingBox.y - player2Ship.boundingBox.height;

        if (Gdx.input.isKeyPressed(Input.Keys.D) && rightLimit > 0) {
            float xChange = player1Ship.movementSpeed * deltaTime;
            xChange = Math.min(xChange, rightLimit);
            player1Ship.translate(xChange, 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && upLimit > 0) {
            float yChange = player1Ship.movementSpeed * deltaTime;
            yChange = Math.min(yChange, upLimit);
            player1Ship.translate(0f, yChange);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && leftLimit < 0) {
            float xChange = -player1Ship.movementSpeed * deltaTime;
            xChange = Math.max(xChange, leftLimit);
            player1Ship.translate(xChange, 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) && downLimit < 0) {
            float yChange = -player1Ship.movementSpeed * deltaTime;
            yChange = Math.max(yChange, downLimit);
            player1Ship.translate(0f, yChange);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.L) && p2rightLimit > 0) {
            float xChange = player2Ship.movementSpeed * deltaTime;
            xChange = Math.min(xChange, p2rightLimit);
            player2Ship.translate(xChange, 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.I) && p2upLimit > 0) {
            float yChange = player2Ship.movementSpeed * deltaTime;
            yChange = Math.min(yChange, p2upLimit);
            player2Ship.translate(0f, yChange);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.J) && p2leftLimit < 0) {
            float xChange = -player2Ship.movementSpeed * deltaTime;
            xChange = Math.max(xChange, p2leftLimit);
            player2Ship.translate(xChange, 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.K) && p2downLimit < 0) {
            float yChange = -player2Ship.movementSpeed * deltaTime;
            yChange = Math.max(yChange, p2downLimit);
            player2Ship.translate(0f, yChange);
        }
    }

//------------------------------------------------------------------------

    private void moveEnemy(EnemyShip enemyShip, float deltaTime) {

        //strategy: determine the max distance the ship can move
        float leftLimit, rightLimit, upLimit, downLimit;

        leftLimit = -enemyShip.boundingBox.x;
        downLimit = (float)game.WORLD_HEIGHT/2 - enemyShip.boundingBox.y;
        rightLimit = game.WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        upLimit = game.WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        if (xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        if (yMove > 0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        enemyShip.translate(xMove, yMove);
    }

//------------------------------------------------------------------------

    private void renderBackground(float deltaTime) {
        //update position of background images
        backgroundOffsets[0] += (deltaTime * backgroundMaxScrollingSpeed) / 8;
        backgroundOffsets[1] += (deltaTime * backgroundMaxScrollingSpeed) / 4;
        backgroundOffsets[2] += (deltaTime * backgroundMaxScrollingSpeed) / 2;
        backgroundOffsets[3] += (deltaTime * backgroundMaxScrollingSpeed);

        //draw each background layer
        for (int layer = 0; layer < backgroundOffsets.length; layer ++) {
            if (backgroundOffsets[layer] > game.WORLD_HEIGHT) {
                backgroundOffsets[layer] =  0;
            }
            game.batch.draw(backgrounds[layer],0,-backgroundOffsets[layer], game.WORLD_WIDTH, game.WORLD_HEIGHT);
            game.batch.draw(backgrounds[layer],0,-backgroundOffsets[layer] + game.WORLD_HEIGHT, game.WORLD_WIDTH, game.WORLD_HEIGHT);
        }
    }

//------------------------------------------------------------------------

    private void renderLasers(float deltaTime) {
        //create new lasers
        //player lasers

       //Auto fire code - not used
            if (player2Ship.canFireLaser()) {
            Laser[] lasers = player2Ship.fireLasers();
            for (Laser laser : lasers) {
                player2LaserList.add(laser);
            }
          }

        boolean spaceIsPressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if (spaceIsPressed && !spaceAlreadyPressed) {
            Laser[] lasers = player1Ship.fireLasers();
            for (Laser laser : lasers) {
                player1LaserList.add(laser);
            }
        }

        spaceAlreadyPressed = spaceIsPressed;

/*        Allows user to hold shoot key to fire lasers.
          if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1Ship.canFireLaser()) {
            Laser[] lasers = player1Ship.fireLasers();
            for (Laser laser : lasers) {
                player1LaserList.add(laser);
            }
        }
*/
        //enemy lasers
        ListIterator<EnemyShip> enemyShipListIterator = enemyShipLinkedList.listIterator();
        while (enemyShipListIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            if (enemyShip.canFireLaser()) {
                Laser[] lasers = enemyShip.fireLasers();
                for (Laser laser : lasers) {
                    enemyLaserList.add(laser);
                }
        }
        }

        //draw lasers
        //remove old lasers
        ListIterator<Laser> iterator = player1LaserList.listIterator();
        while(iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(game.batch);
            laser.boundingBox.y += laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y > game.WORLD_HEIGHT) {
                iterator.remove();
            }
        }

        iterator = player2LaserList.listIterator();
        while(iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(game.batch);
            laser.boundingBox.y += laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y > game.WORLD_HEIGHT) {
                iterator.remove();
            }
        }

        iterator = enemyLaserList.listIterator();
        while(iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(game.batch);
            laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y + laser.boundingBox.height < 0) {
                iterator.remove();
            }
        }
    }

//------------------------------------------------------------------------

    private void detectCollisions() {
        //for each player laser, check whether it intersects an enemy ship
        ListIterator<Laser> laserListIterator = player1LaserList.listIterator();
        ListIterator<Laser> p2laserListIterator = player2LaserList.listIterator();

        while(laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();

            ListIterator<EnemyShip> enemyShipListIterator = enemyShipLinkedList.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();
            if(enemyShip.intersects(laser.boundingBox)) {
                //contact with enemy ship
                if (enemyShip.hit(laser)) {
                    enemyShipListIterator.remove();
                    explosionList.add(new Explosion(explosionTexture,
                            new Rectangle(enemyShip.boundingBox), 0.7f));
                    p1Score += 100;
                }
                laserListIterator.remove();
                break;
            }
            }
        }

        while(p2laserListIterator.hasNext()) {
            Laser laser = p2laserListIterator.next();

            ListIterator<EnemyShip> enemyShipListIterator = enemyShipLinkedList.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();
                if(enemyShip.intersects(laser.boundingBox)) {
                    //contact with enemy ship
                    if (enemyShip.hit(laser)) {
                        enemyShipListIterator.remove();
                        explosionList.add(new Explosion(explosionTexture,
                                new Rectangle(enemyShip.boundingBox), 0.7f));
                        p2Score += 100;
                    }
                    p2laserListIterator.remove();
                    break;
                }
            }
        }

        //for each enemy laser, check whether it intersects a player ship
        laserListIterator = enemyLaserList.listIterator();
        while(laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            if(player1Ship.intersects(laser.boundingBox)) {
                //Laser contact with player ship
                if (player1Ship.hit(laser)) {
                    explosionList.add(new Explosion(explosionTexture,
                            new Rectangle(player1Ship.boundingBox), 1.6f));
                    player1Ship.lives --;
                    if (player1Ship.lives > 0) {
                        player1Ship.shield = 3;
                    }
                    else {
                        player1Ship.boundingBox.set(0, game.WORLD_HEIGHT, 0, 0);
                    }
                }
                laserListIterator.remove();
            }
            if(player2Ship.intersects(laser.boundingBox)) {
                //Laser contact with player ship
                if (player2Ship.hit(laser)) {
                    explosionList.add(new Explosion(explosionTexture,
                            new Rectangle(player2Ship.boundingBox), 1.6f));
                    player2Ship.lives --;
                    if (player2Ship.lives > 0) {
                        player2Ship.shield = 3;
                    }
                    else {
                        player2Ship.boundingBox.set(0, game.WORLD_HEIGHT, 0, 0);
                    }
                }
                laserListIterator.remove();
            }
        }
        if (player1Ship.lives == 0 && player2Ship.lives == 0) {

        }
    }

//------------------------------------------------------------------------

    private void renderExplosions(float deltaTime) {
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while (explosionListIterator.hasNext()) {
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime);
            if (explosion.isFinished()) {
                explosionListIterator.remove();
            }
            else {
                explosion.draw(game.batch);
            }
        }
    }

//------------------------------------------------------------------------

    @Override
    public void show() {
    }
}