/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.srlike.game.helpers.AssetLoader;

/**
 *
 * @author Alex
 */

//this may extend powerup class if it is ever created
public class Macguffin extends ScreenObject{
    private Animation animation;
    private TextureRegion[] aniFrames;
    private float runTime;
    
    public Macguffin(float posX, float posY){
        super(posX, posY, 16, 16, 8);
        type=Type.MACGUFFIN;
        
        runTime=0;
        
        aniFrames=new TextureRegion[6];
        for(int i=0; i<aniFrames.length; i++){
            aniFrames[i]=AssetLoader.atlas.findRegion(
                    "sprite_enemy_shot_cluster", i);
        }
        
        animation=new Animation(0.08f, aniFrames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }
    
    
    @Override
    public void update(float delta){
        runTime+=delta;
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(runTime), 
                position.x-(width/2), position.y-(height/2));
    }

    @Override
    public void collide(ScreenObject s) {
        if(s.getType()==Type.SHIP){
            setAlive(false);
        }
    }
}
