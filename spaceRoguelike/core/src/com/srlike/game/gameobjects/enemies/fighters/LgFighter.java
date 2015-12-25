/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies.fighters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.enemies.EnemyBullet;
import com.srlike.game.helpers.AssetLoader;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class LgFighter extends Fighter{
    
    
    public LgFighter(float positionX, float positionY, Ship ship, Random rand, 
            ArrayList<ScreenObject> level) {
        
        super(positionX, positionY, 
                112, 96, 22,
                ship, rand, level);
        
        setSprite(AssetLoader.atlas.findRegion("lgFighter"));
        
        subtype=Esubtype.LARGEFIGHTER;
        
        setStartingHp(70);
        setSpeed(120);
        setShotInterval(0.6f);
        setRotationSpeed(100);
        
        hp=getStartingHp();
        shotTimer=0;
        
        ai.init();
    }
    
    
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(getSprite(),   //texture region
            position.x-(width/2f), position.y-(height/2f),  //position
            width/2, height/2,     //origin of rotation
            width, height,      //size of region
            0.5f, 0.5f,         //scaling
            rotation);      //rotation
    }
    
    //lg will fire stronger bullets than small, may change this method in superclass
    @Override
    public EnemyBullet fireBullet() {
        shotTimer=getShotInterval(); //reset timer
        firing=false;
        return new EnemyBullet(position.x, position.y, 8, 8, velocity);
    }
}
