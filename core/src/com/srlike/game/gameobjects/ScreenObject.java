/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Alex
 */
public abstract class ScreenObject {
    public enum Type{SHIP, ASTEROID, BULLET, ENEMY, ENEMYBULLET, EXPLOSION}
    
    
    protected int width;
    protected int height;
    
    protected Vector2 position;
    protected Vector2 velocity;
    protected float rotation;
    
    protected int hp;
    private boolean alive;
    protected Type type;
    
    protected Circle boundingCircle;
    
    
    public ScreenObject(
            float positionX, float positionY, //onscreen starting position, center of sprite
            int width, int height,      //width and height of displayed sprite, helps with drawing
            float radius)           //radius for collision detection
    {
        position=new Vector2(positionX, positionY);
        velocity=new Vector2(0f, 0f);
        rotation=0;
        this.width=width;
        this.height=height;
        alive=true;
        
        boundingCircle=new Circle(position, radius);
        
        //some objects will have sprites, some will have animations, will handle this in their own constructor
    }
    
    //getters and setters
    public Vector2 getPosition(){return position;}
    public Vector2 getVelocity(){return velocity;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public float getRotation(){return rotation;}
    public int getHp(){return hp;}
    public void setHp(int h){hp=h;}
    public boolean getAlive(){return alive;}
    public void setAlive(boolean b){alive=b;}
    public Type getType(){return type;}
    public Circle getBoundingCircle(){return boundingCircle;}
    
    
    //update method, will be partially overwritten by subclases to include other necessary steps
    public void update(float delta){
        position.x+=velocity.x*delta;
        position.y+=velocity.y*delta;
        boundingCircle.setPosition(position);
    }
    
    
    //abstract methods
    public abstract void draw(SpriteBatch batch);   //implementation varies for animated and non animated sprites
    public abstract void collide(ScreenObject s);
    
    //public abstract Explosion expode();       //this method could return appropriate explosion when object dies, but not all objects explode.  Should it be universal?
    //public abstract Powerup dropPowerup();       //will create this once powerup classes are created

    
    
}
