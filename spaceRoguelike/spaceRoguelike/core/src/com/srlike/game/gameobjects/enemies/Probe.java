/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.helpers.AssetLoader;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class Probe extends Enemy {
    private TextureRegion image;
    
    private final float speedCap;
    private final int startingHp;
    
    private final float shotInterval;
    private float shotTimer;
    
    
    public Probe(float positionX, float positionY, Ship ship, Random rand, 
            ArrayList<ScreenObject> level) {
        
        super(positionX, positionY, 109, 109, 25f, ship, rand, level);
        
        image=AssetLoader.atlas.findRegion("probe");
        subtype=Esubtype.PROBE;
        
        speedCap=80;
        startingHp=50;
        shotInterval=1.5f;  //seconds
        
        hp=startingHp;
        shotTimer=0;
        
        alterHeading(rand.nextInt(4));
    }
    
    
    @Override
    public void update(float delta){
        alterHeading(rand.nextInt(100));        //small chance of changing direction
        
        velocity.x+=acceleration.x*delta;
        velocity.y+=acceleration.y*delta;
        
        if(velocity.len()>speedCap){
            velocity.setLength(speedCap);
        }
        
        super.update(delta);
        
        if(state==AiState.AGGRO){      //need to turn aggro off if too far away
            directionToShip.set(ship.getPosition().x-position.x, 
                ship.getPosition().y-position.y);
            
            shotTimer+=delta;
            if(shotTimer>shotInterval){
                shotTimer=0;
                firing=rand.nextBoolean();
            }
            
            if(position.dst(ship.getPosition())>700){
                state=AiState.PASSIVE;
            }
        }
    }
    
    private void alterHeading(int direction){
        switch(direction){
            case 0:            //left
                acceleration.x=-30;
                acceleration.y=0;
                break;
            case 1:            //right
                acceleration.x=30;
                acceleration.y=0;
                break;
            case 2:            //down
                acceleration.x=0;
                acceleration.y=-30;
                break;
            case 3:           //up
                acceleration.x=0;
                acceleration.y=30;
                break;
        }
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(image, 
                position.x-(width/2), position.y-(height/2),    //position
                width/2, height/2,                //origin of rotation
                width, height, 
                0.75f, 0.75f,       //scaling
                0);                 //rotation
    }
    
    @Override
    public void collide(ScreenObject s) {
        switch(s.getType()){
            case BULLET:
                hp-=5;
                state=AiState.AGGRO;
                break;
            case SHIP:
                hp-=startingHp;
                break;
            case ASTEROID:
                position.x+=velocity.x*lastDelta*(-1);  //bounce off asteroid
                position.y+=velocity.y*lastDelta*(-1);
                
                velocity.setZero();
                
                acceleration.x*=(-1);   //reverse direction
                acceleration.y*=(-1);
                
                break;
        }
        
        if(hp<1){
            setAlive(false);
        }
    }
    
    @Override
    public void changeState(AiState state){
        this.state=state;
        if(state==AiState.AGGRO){
            firing=true;
        }
    }

    @Override
    public EnemyBullet fireBullet() {
        firing=false;
        return new EnemyBullet(position.x, position.y, 8, 8, directionToShip);
    }
    
}
