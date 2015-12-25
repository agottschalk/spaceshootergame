/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.srlike.game.gameobjects.Ship;
import java.util.ArrayList;


/**
 * this class keeps track of all game objects during game play(called "world" in tutorial)
 * @author Alex
 */
public class Updater {
    //debug
    private float fps;
    
    private ToroidLevel level;
    
    
    public Updater(){
        level=new ToroidLevel(10000, 6500); //approx 9 screens by 9 screens
        level.generate();
    }
    
    
    public void update(float delta){
        
        fps=1/delta;
        
        level.update(delta);
    }
    
    
    public void shipFire(){
        level.shipFire();
    }
    
    
    public float getFPS(){return fps;}
    public Ship getShip(){return level.getShip();}
    public ArrayList getObjects(){return level.getObjects();}
    public ToroidLevel getLevel(){return level;}
    
}
