/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies;

import com.badlogic.gdx.Gdx;
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
    public static enum Esubtype{PROBE, SMALLFIGHTER, LARGEFIGHTER}
    public static enum AiState{PASSIVE, AGGRO, RECOVERY};
    
    
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
            float radius, 
            Ship ship, Random rand, 
            ArrayList level)
    {
        super(positionX, positionY, width, height, radius);
        
        
        type=Type.ENEMY;
        
        acceleration=new Vector2(0,0);
        
        shotTimer=0;
        lastDelta=0;
    }
    
    /*
    *******Enemy's AI***************************************
    */
    protected abstract class StateAi{
        protected AiState state;
        protected Random rand;
        protected Ship ship;
        protected Vector2 directionToShip;
        protected ArrayList<ScreenObject> level;
        
        public StateAi(AiState state, Ship s, Random r, 
                ArrayList<ScreenObject> l){
            this.state=state;   //passive by default, may add ability to create enemies that start in other states
            rand=r;
            ship=s;
            level=l;
            
            directionToShip=new Vector2();
        }
        
        public abstract void init();
        
        public void doStuff(float delta){
            directionToShip.set(ship.getPosition().x-position.x, 
                ship.getPosition().y-position.y);
            universal1(delta);
            switch(state){
                case PASSIVE:
                    passiveAction(delta);
                    break;
                case AGGRO:
                    aggroAction(delta);
                    break;
                case RECOVERY:
                    recoveryAction(delta);
                    break;
            }
            universal2(delta);
        }
        
        protected void universal1(float delta){}
        protected void aggroAction(float delta){}
        protected void passiveAction(float delta){}
        protected void recoveryAction(float delta){}
        protected void universal2(float delta){}
        
        public AiState getState(){return state;}
        public void setState(AiState newState){state=newState;}
        public ArrayList getLevel(){return level;}
        public Vector2 getDirShip(){return directionToShip;}
    }
    /*
    ***************************************************
    */
    
    
    @Override
    public void update(float delta){
        super.update(delta);
        lastDelta=delta;
    }
    
    public  EnemyBullet fireBullet(){return null;}
    
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
