package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract class Ship {

    //ship characteristics
    float movementSpeed; //world units per second
    int shield;;
    float xCenter;

    //position & dimension
    Rectangle boundingBox;

    //laser information
    float laserWidth, laserHeight;
    float laserMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;

    //graphics information
    TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;



    //Constructor
    public Ship(float xCenter, float yCenter,
                float width, float height,
                float movementSpeed, int shield,
                float laserWidth, float laserHeight, float laserMovementSpeed, float timeBetweenShots,
                TextureRegion shipTexture, TextureRegion shieldTexture,
                TextureRegion laserTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.boundingBox = new Rectangle(xCenter - width/2, yCenter - height/2, width, height);
        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.shipTextureRegion = shipTexture;
        this.shieldTextureRegion = shieldTexture;
        this.laserTextureRegion = laserTextureRegion;
        this.xCenter = xCenter;
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser() {
        return ((timeSinceLastShot - timeBetweenShots) >= 0);
    }

    public abstract Laser[] fireLasers();

    public boolean intersects(Rectangle otherRectangle) {
        return boundingBox.overlaps(otherRectangle);
    }


    public boolean hit(Laser laser) {
        if (shield > 0) {
            shield--;
            return false;
        }
        return true;
    }

    public void translate (float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }

    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if (shield > 0) {
            batch.draw(shieldTextureRegion, boundingBox.x, boundingBox.y + boundingBox.height * 0.2f, boundingBox.width, boundingBox.height);
        }
    }

    public float getXCenter() {
        return xCenter;
    }
}