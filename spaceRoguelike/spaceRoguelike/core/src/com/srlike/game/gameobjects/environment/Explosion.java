/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.environment;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.helpers.AssetLoader;

/**
 *
 * @author Alex
 */
public class Explosion extends ScreenObject {
    public static enum expSubtype{YELLOW, BLUE, RED}
    
    private Animation animation;
    private TextureRegion[] aniFrames;
    private float runTime;
    
    public Explosion(float positionX, float positionY,
            int width, int height, 
            expSubtype subtype)
    {
        super(positionX, positionY, width, height, 1);
        
        runTime=0;
        type=Type.EXPLOSION;
        
        
        
        switch(subtype){
            case YELLOW:
                aniFrames=new TextureRegion[30];
                for(int i=0; i<30; i++){
                    aniFrames[i]=AssetLoader.atlas.findRegion("exYelFrag", i+1);
                }
                break;
            case BLUE:
                aniFrames=new TextureRegion[10];
                for(int i=0; i<10; i++){
                    aniFrames[i]=AssetLoader.atlas.findRegion(
                            "blueExplosion", i+1);
                }
                break;
            case RED:
                aniFrames=new TextureRegion[10];
                for(int i=0; i<10; i++){
                    aniFrames[i]=AssetLoader.atlas.findRegion(
                            "redExplosion", i+1);
                }
                break;
        }
        
        animation=new Animation(0.03f, aniFrames);
    }
    
    @Override
    public void update(float delta){
        runTime+=delta;
        if(animation.isAnimationFinished(runTime)){
            setAlive(false);
        }
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(runTime), 
                position.x-(width/2), position.y-(height/2));
    }

    @Override
    public void collide(ScreenObject s) {
        //do nothing
    }
    
}
