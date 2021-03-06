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
import com.srlike.game.display.ToroidLevel;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.environment.Explosion;
import com.srlike.game.helpers.AssetLoader;
import com.srlike.game.gameobjects.enemies.StateAi.AiState;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class Probe extends Enemy {
    private TextureRegion image;
    
    private final float speedCap;
    
    
    public Probe(float positionX, float positionY, ToroidLevel level) {
        
        super(positionX, positionY, 109, 109, 25f);
        
        image=AssetLoader.atlas.findRegion("probe");
        subtype=Esubtype.PROBE;
        
        speedCap=80;
        setStartingHp(50);
        setShotInterval(1.5f);  //seconds
        
        hp=getStartingHp();
        
        collisionDamage=34;
        
        ai=new ProbeAi(AiState.PASSIVE, level);
        ai.init();
    }

    @Override
    public void dropPowerups(ArrayList<ScreenObject> level) {}  //probe currently does not drop any powerups
    
    
    
    /*
    ********************Probe's AI************************
    */
    private class ProbeAi extends StateAi{
        public ProbeAi(AiState state, ToroidLevel l){
            super(state, Probe.this, l);
        }

        @Override
        public void init() {
            alterHeading(rand.nextInt(4));
        }
        
        //vvvvvvvvvv**ai actions**vvvvvvvvvvv
        @Override
        protected void universal1(float delta){
            alterHeading(rand.nextInt(100));        //small chance of changing direction
        }
        
        @Override
        protected void aggroAction(float delta){    //fires at ship if aggro
            directionToShip.set(ship.getPosition().x-position.x, 
                ship.getPosition().y-position.y);

            shotTimer+=delta;
            if(shotTimer>getShotInterval()){
                shotTimer=0;
                firing=rand.nextBoolean();
            }

            if(position.dst(ship.getPosition())>700){   //turns aggro off if too far away
                state=AiState.PASSIVE;
            }
        }
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        
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
        
        
    }
    /*
    **********************************************************
    */
    
    @Override
    public void update(float delta){
        ai.doStuff(delta);
        
        velocity.x+=acceleration.x*delta;
        velocity.y+=acceleration.y*delta;
        
        if(velocity.len()>speedCap){
            velocity.setLength(speedCap);
        }
        
        super.update(delta);    //ai picks new acceleration, move ship
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
                hp-=s.dealDamage();
                ai.setState(AiState.AGGRO);
                break;
            case SHIP:
                hp-=s.dealDamage();
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
    public void fireBullet(ArrayList<ScreenObject> level) {
        firing=false;
        level.add(new EnemyBullet(position.x, position.y, 8, 8, 
                ai.getDirShip()));
    }
    
    @Override
    public void explode(ArrayList<ScreenObject> level){
        level.add(new Explosion(position.x, position.y, 100, 100, 
                Explosion.expSubtype.YELLOW));
    }
}
