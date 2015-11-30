/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.badlogic.gdx.Gdx;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.Asteroid;
import java.util.ArrayList;
import java.util.Random;

/**
 * this class keeps track of all game objects during game play(called "world" in tutorial)
 * @author Alex
 */
public class Updater {
    private Random random;
    
    private Ship ship;
    private ArrayList<Asteroid> testAsteroids;
    
    
    public Updater(float screenW, float screenH){
        random=new Random();    //for testing right now, may move this object later
        
        ship=new Ship(0,0, screenW/2, screenH/2);  //level coordinates are just placeholder for now
        
        testAsteroids=new ArrayList<Asteroid>();
        
        generateLevel();
    }
    
    
    public void update(float delta){
        //update game objects
        Gdx.app.log("updater", "update");
        
        ship.update(delta);
    }
    
    public Ship getShip(){return ship;}
    
    public ArrayList getAsteroids(){return testAsteroids;}
    
    private void generateLevel(){
        generateAsteroids();
    }
    
    //offscreen=649 pixels away
    private void generateAsteroids(){
        while(testAsteroids.size()<1000){
            testAsteroids.add(new Asteroid(random.nextInt(10000)-5000, random.nextInt(6500)-3250));
        }
    }
}
