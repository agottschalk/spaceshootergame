/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.srlike.game.helpers.AssetLoader;

/**
 *
 * @author Alex
 */
public class Ship extends ScreenObject {
    private TextureRegion image;
    
    private boolean engine;
    
    private Vector2 direction;
    
    private final float acceleration;
    private final float speedCap;
    
    public Ship(float positionX, float positionY, //onscreen starting position, center of sprite
        int width, int height)      //width and height of displayed sprite, helps with drawing)
    {
        super(positionX, positionY, width, height, 32);
        type=Type.SHIP;
        
        image=AssetLoader.atlas.findRegion("playership");
        
        
        //ship motion vectors
        direction=new Vector2(0f, 0f);
        
        //ship motion constants
        acceleration=500f;
        speedCap=600f;
        
        hp=100;
    }
    
    @Override
    public void update(float delta){
        if(engine){
            //accelerate
            velocity.x+=acceleration*direction.x*delta;
            velocity.y+=acceleration*direction.y*delta;
        }else{
            //decelerate
            velocity.scl((1-(delta/2)));    //(1-delta/2) approx= 0.991
            
            //ship stops when speed is close to 0
            if(velocity.len()<15){
                velocity.setZero();
            }
        }
        //cap speed
        if(velocity.len()>speedCap){
            velocity.setLength(speedCap);
        }
        
        //adjust ship position
        super.update(delta);
    }
    
    public void rotate(Vector3 mousePos){
        direction.x=mousePos.x-position.x;
        direction.y=mousePos.y-position.y;
        direction.nor();
        rotation=direction.angle();
    }
    
    public ShipBullet fireBullet(){return new ShipBullet(position.x, 
                position.y, 8, 8, direction);}
    
    public Vector2 getDirection(){return direction;}
    public boolean getEngine(){return engine;}
    public void setEngine(boolean e){engine=e;}
    
    
    //for debugging, might not be in final game
    public void setX(float x){position.x=x;}
    public void setY(float y){position.y=y;}

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(image,   //texture region
                position.x-(width/2f), position.y-(height/2f),  //position
                width/2, height/2,     //origin of rotation
                width, height,      //size of region
                0.5f, 0.5f,         //scaling
                rotation);      //rotation
    }

    @Override
    public void collide(ScreenObject s) {
        if(s.getType()==Type.ASTEROID 
                || s.getType()==Type.ENEMY){
            hp-=25;
        }else if(s.getType()==Type.ENEMYBULLET){
            hp-=10;
        }
    }
    
}
