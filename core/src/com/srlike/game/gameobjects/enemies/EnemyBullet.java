/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.helpers.AssetLoader;

/**
 *
 * @author Alex
 */
public class EnemyBullet extends ScreenObject {
    private final float speed;
    
    private TextureRegion image;
    
    
    public EnemyBullet(float positionX, float positionY, 
            int width, int height,      //8 pixels by 8 pixels
            Vector2 direction) 
    {
        super(positionX, positionY, width, height, 2);
        image=AssetLoader.atlas.findRegion("redbullet");
        
        type=Type.ENEMYBULLET;
        
        speed=300;
        velocity.set(direction);
        velocity.setLength(speed);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(image, position.x-(width/2), position.y-(height/2));
    }

    @Override
    public void collide(ScreenObject s) {
        if(s.getType()==Type.ASTEROID
                || s.getType()==Type.SHIP){
            setAlive(false);
        }
    }
    
}
