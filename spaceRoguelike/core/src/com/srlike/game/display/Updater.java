/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.srlike.game.gameobjects.Ship;
import java.util.ArrayList;
import java.util.Random;


/**
 * Initializes the level and calls the update cycle once every frame
 * @author Alex
 */
public class Updater {
    private Random random;
    //debug
    private float fps;
    
    private ToroidLevel level;
    private Hud hud;
    
    
    public Updater(){
        random=new Random();
        level=new ToroidLevel(10000, 6500, random); //approx 9 screens by 9 screens
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
