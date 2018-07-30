/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.srlike.game.display.ToroidLevel;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.enemies.StateAi.AiState;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 */
public abstract class Enemy extends ScreenObject {
    public static enum Esubtype{PROBE, SMALLFIGHTER, LARGEFIGHTER, ELITE}
    //public static enum AiState{PASSIVE, AGGRO, RECOVERY};
    
    
    private int startingHp;
    private float shotInterval;
    
    protected float shotTimer;
    
    protected Vector2 acceleration;
    
    protected boolean firing;
    protected Esubtype subtype;
    
    protected float lastDelta;
    
    protected StateAi ai;
    
    public Enemy(float positionX, float positionY,
            int width, int height,
            float radius)
    {
        super(positionX, positionY, width, height, radius);
        
        
        type=Type.ENEMY;
        
        acceleration=new Vector2(0,0);
        
        shotTimer=0;
        lastDelta=0;
    }
    
    @Override
    public void update(float delta){
        super.update(delta);
        lastDelta=delta;
    }
    
    
    public AiState getState(){
        if(ai!=null){
            return ai.getState();}
        else{
            Gdx.app.log("err", "no ai");
            return null;
        }
    }
    public void setState(AiState newState){
        if (ai!=null){
            ai.setState(newState);
        }
    }
    
    public boolean getFiring(){return firing;}
    public Esubtype getSubtype(){return subtype;}
    
    protected void setStartingHp(int hp){startingHp=hp;}
    protected int getStartingHp(){return startingHp;}
    protected void setShotInterval(float sInterv){shotInterval=sInterv;}
    protected float getShotInterval(){return shotInterval;}
}
