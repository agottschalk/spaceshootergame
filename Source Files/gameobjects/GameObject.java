/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Alex
 */
public abstract class GameObject {
    private int width;
    private int height;
    
    private Vector2 position;
    private Vector2 velocity;
    private float rotation;
    
    private Circle boundingCircle;
    
    private Texture spriteMap;  //will eventually be spritemap for entire game, for now just object
    private TextureRegion image;    //sprites pertinent to particular object
    
    public GameObject(
            Texture map, //game spritemap
            float positionX, float positionY, //onscreen starting position, center of sprite
            int width, int height,      //width and height of displayed sprite, helps with drawing
            int regionX1, int regionY1,     //bottom left of texture region
            int regionX2, int regionY2,     //top right of texture region
            float radius){          //radius for collision detection
        
        position=new Vector2(positionX, positionY);
        velocity=new Vector2(0f, 0f);
        rotation=0;
        
        spriteMap=map;
        
        this.width=width;
        this.height=height;
        
        boundingCircle=new Circle(position, radius);
    }
    
    //getters and setters
    public Vector2 getPosition(){return position;}
    public Circle getBoundingCircle(){return boundingCircle;}
    
    //abstract methods
    public abstract void update(float delta);
    public abstract void draw(SpriteBatch batch);
        //default
        /*batch.draw(image,   //texture region
                position.x-(width/2f), position.y-(height/2f),  //position
                width/2, height/2,     //origin of rotation
                width, height,      //size of region
                1f, 1f,         //scaling
                rotation);      //rotation
        */
    
    
}
