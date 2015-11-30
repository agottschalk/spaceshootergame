/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author Alex
 */
public class Ship {
    private float rotation;
    private boolean engine;
    
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 direction;
    
    private Circle boundingCircle;
    
    private final float acceleration;
    private final float deceleration;
    private final float speedCap;
    
    public Ship(float levelX, float levelY, float screenX, float screenY){
        //ship motion vectors
        direction=new Vector2(0f, 0f);
        position=new Vector2(screenX, screenY);
        velocity=new Vector2(0f, 0f);
        rotation=0;
        
        //ship motion constants
        acceleration=500f;
        deceleration=-10f;
        speedCap=800f;
        
        boundingCircle=new Circle(position, 32);   //arbitrary radius for now
    }
    
    public void update(float delta){
        if(engine){
            //accelerate
            velocity.x+=acceleration*direction.x*delta;
            velocity.y+=acceleration*direction.y*delta;
        }else{
            //decelerate
            //if (velocity.len()<300){
                velocity.scl(0.985f);
                //deceleration has to stop at 0\
                if(velocity.len()<15){
                    velocity.setZero();
                }
            
        }
        
        if(velocity.len()>speedCap){
            velocity.setLength(speedCap);
        }
        
        Gdx.app.log("vel", ""+velocity.len());
        
        //adjust ship position
        position.x+=velocity.x*delta;
        position.y+=velocity.y*delta;
    }
    
    public void rotate(Vector3 mousePos){
        direction.x=mousePos.x-position.x;
        direction.y=mousePos.y-position.y;
        direction.nor();
        rotation=direction.angle()-90;
    }
    
    public float getRotation(){return rotation;}
    public Vector2 getPosition(){return position;}
    public Circle getBoundingCircle(){return boundingCircle;}
    public boolean getEngine(){return engine;}
    public void setEngine(boolean e){engine=e;}
    
    //for debugging, might not be in final game
    public void setX(float x){position.x=x;}
    public void setY(float y){position.y=y;}
}
