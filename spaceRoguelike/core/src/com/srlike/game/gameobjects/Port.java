/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.srlike.game.display.ToroidLevel;
import com.srlike.game.helpers.AssetLoader;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class Port extends ScreenObject{
    private TextureRegion image;
    
    public Port(float posX, float posY){
        super(posX, posY, 600, 600, 150f);
        image=AssetLoader.atlas.findRegion("WB_baseu2_d0");
        type=Type.PORT;
    }
    
    
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(image, 
                position.x-(width/2), position.y-(height/2),    //position
                width/2, height/2,                //origin of rotation
                width, height, 
                0.5f, 0.5f,       //scaling
                0);                 //rotation
    }

    @Override
    public void collide(ScreenObject s) {
            
        
    }

    @Override
    public void fireBullet(ArrayList<ScreenObject> level) {}//does not fire bullets

    @Override
    public void explode(ArrayList<ScreenObject> level) {}//does not explode

    @Override
    public void dropPowerups(ArrayList<ScreenObject> level) {}//does not drop powerups
    
    
}
