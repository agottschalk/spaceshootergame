/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies.fighters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.enemies.Enemy;
import com.srlike.game.gameobjects.enemies.EnemyBullet;
import com.srlike.game.helpers.AssetLoader;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class SmFighter extends Fighter{
    
    
    public SmFighter(float positionX, float positionY, Ship ship, Random rand, 
            ArrayList<ScreenObject> level) {
        
        super(positionX, positionY, 
                100, 97, //width, height
                20f, //collision radius
                ship, rand, level);
        
        setSprite(AssetLoader.atlas.findRegion("smallfighter"));
        
        subtype=Esubtype.SMALLFIGHTER;
        
        setStartingHp(50);
        setSpeed(130);
        setShotInterval(0.6f);
        setRotationSpeed((float)(0.5*Math.PI));
        
        hp=getStartingHp();
        shotTimer=0;
        
        ai.init();
    }
    
}
