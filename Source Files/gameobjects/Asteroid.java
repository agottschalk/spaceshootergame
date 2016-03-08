/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Alex
 */
public class Asteroid {
    private Vector2 position;
    
    private Circle boundingCircle;
    
    public Asteroid(float positionX, float positionY){
        position=new Vector2(positionX, positionY);
        
        boundingCircle=new Circle(positionX, positionY, 25f);    //arbitrary radius for now
    }
    
    public Vector2 getPosition(){return position;}
    
    public Circle getBoundingCircle(){return boundingCircle;}
}
