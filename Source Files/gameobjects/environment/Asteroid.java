/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.environment;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.helpers.AssetLoader;

/**
 *
 * @author Alex
 */
public class Asteroid extends ScreenObject {
    
    private TextureRegion image;
    
    
    public Asteroid(float positionX, float positionY)
    {
        super(positionX, positionY, 120, 120, 25f);
        type=Type.ASTEROID;
        
        image=AssetLoader.atlas.findRegion("test_asteroid");
        
        hp=100;
    }
    
    //getters and setters
    
    
    @Override
    public void update(float delta){
        super.update(delta);
        
        if(hp<1){       //hp<=0
            setAlive(false);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(image, position.x-(width/2), position.y-(height/2));
    }

    @Override
    public void collide(ScreenObject s) {
        switch(s.getType()){
            case SHIP: hp=0;
                break;
            case BULLET:  hp-=5;
                break;
            case ENEMYBULLET:  hp-=10;
                break;
        }
        
        //death check
        if(hp<1){
            setAlive(false);
        }
    }
    
}
