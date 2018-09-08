/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.srlike.game.display.GameScreen;
import com.srlike.game.display.ToroidLevel;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.enemies.Enemy;
import java.util.Random;

/**
 *
 * @author Alex
 */
public abstract class StateAi {
    public static enum AiState{PASSIVE, AGGRO, RECOVERY};
    
    protected AiState state;
    protected Random rand;
    //protected Ship ship;
    protected Vector2 directionToShip;
    protected ScreenObject outer;

    public StateAi(AiState state, ScreenObject holder){
        this.state=state;   //passive by default, may add ability to create enemies that start in other states
        outer=holder;
        rand=new Random();

        directionToShip=new Vector2();
    }

    public abstract void init();

    public void doStuff(float delta){
        Ship ship = GameScreen.getInstance().getShip();
        
        directionToShip.set(ship.getPosition().x-outer.getPosition().x, 
            ship.getPosition().y-outer.getPosition().y);
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
    public Vector2 getDirShip(){return directionToShip;}
    public Random getRand(){return rand;}
    public ScreenObject getOuter(){return outer;}
}

