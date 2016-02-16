/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies.fighters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.srlike.game.display.ToroidLevel;
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
    private int shotCount;
    
    public LgFighter(float positionX, float positionY, ToroidLevel level) {
        
        super(positionX, positionY, 
                112, 96, 22, level);
        
        setSprite(AssetLoader.atlas.findRegion("lgFighter"));
        
        subtype=Esubtype.LARGEFIGHTER;
        
        setStartingHp(40);
        setSpeed(120);
        setShotInterval(1.2f);
        setRotationSpeed((float)(0.6*Math.PI));
        
        hp=getStartingHp();
        shotTimer=0;
        shotCount=0;
        
        ai.init();  //ai can't initialize properly until all ship stats are set
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
    public void fireBullet(ArrayList<ScreenObject> level) {
        if(shotTimer<0){
            level.add(new EnemyBullet(position.x, position.y, 8, 8, velocity));
            
            if(shotCount<2){
                shotCount++;
                shotTimer=0.07f;
            } else {
                shotCount=0;
                shotTimer=getShotInterval();    //reset timer
                firing=false;
            }
        }
    }
}
