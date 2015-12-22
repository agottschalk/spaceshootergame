/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.enemies.Enemy;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 * 
 * ********************UNDER CONSTRUCTION**************************
 */
public class ToroidLevel {
    private Random random;
    
    private Ship ship;
    private ArrayList<ScreenObject> gameObjects;        //holds everything
    private ArrayList<Enemy> enemies;
    
    
    public ToroidLevel(){
        random=new Random();
        
        ship=new Ship(0,0,112,118);
        
        gameObjects=new ArrayList();
        enemies=new ArrayList();
    }
}
