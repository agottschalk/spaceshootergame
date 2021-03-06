/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.srlike.game.display.ToroidLevel;
import com.srlike.game.gameobjects.environment.Explosion;
import com.srlike.game.helpers.AssetLoader;
import java.util.ArrayList;

/**
 * Represents the player's ship in game
 * @author Alex
 */
public class Ship extends ScreenObject {

    private TextureRegion image;

    private boolean engine = false;

    private Vector2 direction;

    private final float acceleration = 500f;
    private final float speedCap = 600f;

    private ToroidLevel level;
    private int macguffinsCollected;

    //level end animation
    private boolean locked = false;   //ship no longer accepts inputs during end animation
    private int framesRemaining = 30;

    public Ship(float positionX, float positionY, //onscreen starting position, center of sprite
            ToroidLevel level) {
        super(positionX, positionY, 112, 118, 32);
        type = Type.SHIP;

        image = AssetLoader.atlas.findRegion("playership");

        this.level = level;

        //ship motion vectors
        direction = new Vector2(0f, 0f);

        collisionDamage = 9999;

        hp = 100;
        macguffinsCollected = 0;
    }

    /**
     * Called every frame, updates ships position and velocity
     *
     * @param delta time since last update
     */
    @Override
    public void update(float delta) {
        if (engine) {
            //accelerate
            velocity.x += acceleration * direction.x * delta;
            velocity.y += acceleration * direction.y * delta;
        } else {
            //decelerate
            velocity.scl((1 - (delta / 2)));    //(1-delta/2) approx= 0.991

            //ship stops when speed is close to 0
            if (velocity.len() < 15) {
                velocity.setZero();
            }
        }
        //cap speed
        if (velocity.len() > speedCap) {
            velocity.setLength(speedCap);
        }

        //adjust ship position
        super.update(delta);
    }

    /**
     * Rotates the ship so that it points towards the mouse cursor
     *
     * @param mousePos position of cursor
     */
    public void rotate(Vector3 mousePos) {
        direction.x = mousePos.x - position.x;
        direction.y = mousePos.y - position.y;
        direction.nor();
        rotation = direction.angle();
    }

    public Vector2 getDirection() {
        return direction;
    }

    /**
     * Turns ship engine on. While on, the ship will accelerate until it
     * reaches its max speed
     */
    public void engineOn(){
        engine = true;
    }
    
    /**
     * Turns ship engine off. While off, the ship will decelerate due to
     * 'space friction' until it stops
     */
    public void engineOff(){
        engine = false;
    }

    /**
     * Number of macguffin powerups collected so far
     * @return macgffins collected
     */
    public int getCollected() {
        return macguffinsCollected;
    }

    /**
     * Adds the ship's image to the current spritebatch for rendering
     *
     * @param batch current spritebatch
     */
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(image, //texture region
                position.x - (width / 2f), position.y - (height / 2f), //position
                width / 2, height / 2, //origin of rotation
                width, height, //size of region
                0.5f, 0.5f, //scaling
                rotation);      //rotation
    }

    /**
     * Performs appropriate action when the ship collides with another object in
     * the game
     *
     * @param other object being collided with
     */
    @Override
    public void collide(ScreenObject other) {
        switch (other.getType()) {
            case ASTEROID:
                hp -= other.dealDamage();
                break;
            case ENEMY:
                hp -= other.dealDamage();
                break;
            case ENEMYBULLET:
                hp -= other.dealDamage();
                break;
            case MACGUFFIN:
                macguffinsCollected++;
                break;
            case PORT:
                if (macguffinsCollected >= level.getMacguffinCount()
                        && boundingCircle.contains(other.getPosition())) {
                    Gdx.app.log("Ship", "level over");
                    //ends level
                }
                break;
        }
    }

    /**
     * Returns a string showing the x and y positions of the ship
     *
     * @return x ad y position of ship
     */
    @Override
    public String toString() {
        return "Ship X:" + position.x
                + "\nShip Y:" + position.y;
    }

    /**
     * Sets the position of the ship within the level
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    @Override
    public void setPosition(float x, float y) {
    }

    /**
     * Creates a new bullet with a heading based on where the ship is pointing
     * and adds it to the current level
     *
     * @param level current level
     */
    @Override
    public void fireBullet(ArrayList<ScreenObject> level) {
        level.add(new ShipBullet(position.x, position.y, 8, 8, direction));
    }

    /**
     * Creates an explosion animation and adds it to the level. Meant to be
     * called when the player dies
     * @param level current level
     */
    @Override
    public void explode(ArrayList<ScreenObject> level) {
        level.add(new Explosion(position.x, position.y, 100, 100,
                Explosion.expSubtype.YELLOW));
    }

    @Override
    public void dropPowerups(ArrayList<ScreenObject> level) {
    }//does not drop powerups
}
