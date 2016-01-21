/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.srlike.game.gameobjects.environment.Explosion;

/**
 *
 * @author Alex
 */
public abstract class ScreenObject {
    public static enum Type{SHIP, ASTEROID, BULLET, ENEMY, LARGEENEMY, 
    ENEMYBULLET, EXPLOSION, PORT, MACGUFFIN}
    
    
    protected int width;
    protected int height;
    
    protected Vector2 position;
    protected Vector2 velocity;
    protected float rotation;
    
    protected int hp;
    private boolean alive;
    protected Type type;
    
    protected Circle boundingCircle;
    
    protected Vector2 directionToObject;
    
    
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
    public void setPosition(float x, float y){position.set(x, y);}
    public void setPosition(Vector2 newPos){position.set(newPos);}
    public Vector2 getVelocity(){return velocity;}
    public void setVelocity(float x, float y){velocity.set(x, y);}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public float getRotation(){return rotation;}
    public int getHp(){return hp;}
    public void setHp(int h){hp=h;}
    public boolean getAlive(){return alive;}
    public void setAlive(boolean b){alive=b;}
    public Type getType(){return type;}
    public Circle getBoundingCircle(){return boundingCircle;}
    
    public void findVectorTo(ScreenObject s){
        //doing it this way avoids allocating new memory each time you need to compare objects
        if(directionToObject==null){directionToObject=new Vector2();}
        
        directionToObject.set(s.getPosition().x-this.position.x,
            s.getPosition().y-this.position.y);
    }
    
    //update method, will be partially overwritten by subclases to include other necessary steps
    public void update(float delta){
        position.x+=velocity.x*delta;
        position.y+=velocity.y*delta;
        boundingCircle.setPosition(position);
    }
    
    
    //abstract methods
    public abstract void draw(SpriteBatch batch);   //implementation varies for animated and non animated sprites
    public abstract void collide(ScreenObject s);
    
    public Explosion explode(){return null;} //always error check when using since it can return a null value
    //public abstract Powerup dropPowerup();       //will create this once powerup classes are created

    
    
}
