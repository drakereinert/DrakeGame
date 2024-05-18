package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class PlayerShip extends Ship {

    int lives;

    public PlayerShip(float xCenter, float yCenter,
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
        lives = 3;
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] laser = new Laser[1];
        laser[0] = new Laser(boundingBox.x + boundingBox.width/2, boundingBox.y + boundingBox.height,
                laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);

        timeSinceLastShot = 0;

        return laser;
    }

    @Override
    public void draw(Batch batch) {
        if (lives > 0) {
            batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        }

        if (shield > 0) {
            batch.draw(shieldTextureRegion, boundingBox.x, boundingBox.y + boundingBox.height * 0.2f, boundingBox.width, boundingBox.height);
        }
    }
}