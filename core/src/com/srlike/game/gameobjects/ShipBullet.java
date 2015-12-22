/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.srlike.game.helpers.AssetLoader;

/**
 *
 * @author Alex
 */
public class ShipBullet extends ScreenObject {
    private final float speed;      //gonna make this a constant
    
    private TextureRegion image;
    
    
    public ShipBullet(float positionX, float positionY, 
            int width, int height,      //8 pixels by 8 pixels
            Vector2 direction)      //ship's direction vector
    {
        super(positionX, positionY, width, height, 2);
        type=Type.BULLET;
        
        speed=700;
        
        //convert ship direction to bullet velocity
        velocity.set(direction);
        velocity.setLength(speed);
        
        image=AssetLoader.atlas.findRegion("bluebullet");
    }
    
    @Override
    public void draw(SpriteBatch batch) {   //will make this more general later
        batch.draw(image, position.x-(width/2), position.y-(height/2));
    }

    @Override
    public void collide(ScreenObject s) {
        if(s.getType()==Type.ASTEROID
                || s.getType()==Type.ENEMY){
            setAlive(false);
        }
    }
    
}
