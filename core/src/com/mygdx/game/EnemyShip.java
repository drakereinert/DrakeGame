package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

class EnemyShip extends Ship{

    Vector2 directionVector;
    float timeSinceLastDirectionChange = 0;
    float directionChangeFrequency = 0.75f;


    public EnemyShip(float xCenter, float yCenter,
                     float width, float height,
                     float movementSpeed, int shield,
                     float laserWidth, float laserHeight,
                     float laserMovementSpeed, float timeBetweenShots,
                     TextureRegion shipTexture,
                     TextureRegion shieldTexture,
                     TextureRegion laserTextureRegion) {
        super(xCenter, yCenter, width, height, movementSpeed, shield,
                laserWidth, laserHeight, laserMovementSpeed, timeBetweenShots,
                shipTexture, shieldTexture, laserTextureRegion);

        directionVector = new Vector2(Drake.random.nextFloat() * 2 - 1, Drake.random.nextFloat() * 2 - 1);
    }

//------------------------------------------------------------------------

    public Vector2 getDirectionVector() {
        return directionVector;
    }

//------------------------------------------------------------------------


    private void randomizeDirectionVector() {
        double bearing = Drake.random.nextDouble() * 6.283185; //0 to 2*PI
        directionVector.x = (float)Math.sin(bearing);
        directionVector.y = (float)Math.cos(bearing);
    }


//------------------------------------------------------------------------


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            randomizeDirectionVector();
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }
    }


//------------------------------------------------------------------------


    @Override
    public Laser[] fireLasers() {
        Laser[] laser = new Laser[1];
        laser[0] = new Laser(boundingBox.x + boundingBox.width/2, boundingBox.y - laserHeight,
                laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);

        timeSinceLastShot = 0;

        return laser;
    }


//------------------------------------------------------------------------


    @Override
    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if (shield > 0) {
            batch.draw(shieldTextureRegion, boundingBox.x, boundingBox.y - boundingBox.height * 0.2f, boundingBox.width, boundingBox.height);
        }
    }
}
