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

/**
 *
 * @author Alex
 */
public class Port extends ScreenObject{
    private TextureRegion image;
    private Ship ship;
    private ToroidLevel level;
    
    public Port(float posX, float posY, Ship ship, ToroidLevel level){
        super(posX, posY, 600, 600, 150f);
        image=AssetLoader.atlas.findRegion("WB_baseu2_d0");
        this.level=level;
        this.ship=ship;
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
    
    
}
