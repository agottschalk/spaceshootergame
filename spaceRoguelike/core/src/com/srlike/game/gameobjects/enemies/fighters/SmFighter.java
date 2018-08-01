/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies.fighters;

import com.srlike.game.helpers.AssetLoader;

/**
 *
 * @author Alex
 */
public class SmFighter extends Fighter{
    
    
    public SmFighter(float positionX, float positionY) {
        
        super(positionX, positionY, 
                100, 97, //width, height
                20f);
        
        setSprite(AssetLoader.atlas.findRegion("smallfighter"));
        
        subtype=Esubtype.SMALLFIGHTER;
        
        setStartingHp(30);
        setSpeed(130);
        setShotInterval(1.0f);
        setRotationSpeed((float)(0.5*Math.PI));
        
        hp=getStartingHp();
        shotTimer=0;
        
        ai.init();  //ai can't initialize properly until all ship stats are set
    }
    
}
