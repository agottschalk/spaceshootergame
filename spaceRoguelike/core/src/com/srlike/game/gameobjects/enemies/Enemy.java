/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 */
public abstract class Enemy extends ScreenObject {
    public static enum Esubtype{PROBE, SMALLFIGHTER}
    public static enum AiState{PASSIVE, AGGRO, RECOVERY};
    
    //ai tools
    protected Random rand;
    protected Ship ship;
    protected Vector2 directionToShip;
    private ArrayList<ScreenObject> level;
    
    protected Vector2 acceleration;
    
    protected AiState state;
    protected boolean firing;
    protected Esubtype subtype;
    
    protected float lastDelta;
    

    
    public Enemy(float positionX, float positionY,
            int width, int height,
            float radius, 
            Ship ship, Random rand, 
            ArrayList level)
    {
        super(positionX, positionY, width, height, radius);
        this.rand=rand;
        this.ship=ship;
        this.level=level;
        directionToShip=new Vector2(0,0);
        
        type=Type.ENEMY;
        
        acceleration=new Vector2(0,0);
        
        lastDelta=0;
        state=AiState.PASSIVE;
    }
    
    
    
    @Override
    public void update(float delta){
        super.update(delta);
        lastDelta=delta;
    }
    
    public abstract void changeState(AiState state);
    public abstract EnemyBullet fireBullet();
    
    public AiState getState(){return state;}
    public void setState(AiState newState){state=newState;}
    public boolean getFiring(){return firing;}
    public Esubtype getSubtype(){return subtype;}
    public ArrayList getLevel(){return level;}
    
}
