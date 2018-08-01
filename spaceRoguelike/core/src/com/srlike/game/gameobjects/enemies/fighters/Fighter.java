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
import com.srlike.game.display.ToroidLevel;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.enemies.Enemy;
import com.srlike.game.gameobjects.enemies.EnemyBullet;
import com.srlike.game.gameobjects.environment.Explosion;
import com.srlike.game.helpers.AssetLoader;
import com.srlike.game.gameobjects.enemies.StateAi;
import com.srlike.game.gameobjects.enemies.StateAi.AiState;
import java.util.ArrayList;
import java.util.Random;

/**
 * Base class for fighter type enemies.  Fighters fly along an initial
 * trajectory which changes periodically.  When engaged, that trajectory changes
 * to always point towards the player and the fighter begins firing continuously.
 * When the fighter gets close to the player, it will veer away to try and 
 * avoid colliding before circling back for another attack, simulating a dogfight.
 * @author Alex
 */
public class Fighter extends Enemy{
    private TextureRegion image;
    
    
    private float speed;
    private float rotationSpeed;
    
    protected Vector2 lastPos;
    
    private boolean following;
    
    public Fighter(float positionX, float positionY, 
            int width, int height, float radius) {
        this(positionX, positionY, width, height, radius, false);
    }
    
    public Fighter(float positionX, float positionY, 
            int width, int height, float radius, boolean following) {
        
        super(positionX, positionY, 
                width, height, radius);
        
        collisionDamage=34;
        
        this.following=following;
        ai=new FighterAi(AiState.PASSIVE);
        
        lastPos=new Vector2(position);
    }

    @Override
    public void dropPowerups(ArrayList<ScreenObject> level) {}  //fighter currently does not drop any powerups    
    
    
    /*
    ******************Fighter's AI****************************
    */
    
    /*
    ai states:
    passive--not interested in player, just moving, tries to avoid collisions
    aggro--actively chasing after player and firing
    dodge--during aggro, turning around to avoid crashing into player
    */
    protected class FighterAi extends StateAi{
        private Vector2 targetVector;
        
        public FighterAi(AiState state){
            super(state, Fighter.this);
        }
        
        @Override
        public void init() {
            //pick initial starting velocity--doubles as heading
            velocity.x=rand.nextFloat()-0.5f;   //nextFloat returns a number between 0 and 1
            velocity.y=rand.nextFloat()-0.5f;

            velocity.setLength(speed);
            rotation=velocity.angle();
            
            changeHeading();
        }
        
        
        
        //vvvvvvvvvvv**ai actions**vvvvvvvvvvv
        @Override
        protected void passiveAction(float delta){
            if(!following && rand.nextInt(250)==0){
                changeHeading();
            }
            turnShip(delta, true);
        }
        
        @Override
        protected void aggroAction(float delta){
            targetVector.set(directionToShip);
                turnShip(delta, true);
                if((shotTimer<0)
                    && ((directionToShip.angle()-velocity.angle()) < 15)
                    && ((directionToShip.angle()-velocity.angle()) > (-15))){

                    firing=true;
                }
                if(position.dst(ship.getPosition())<175){
                    state=AiState.RECOVERY;
                }
        }
        
        @Override
        protected void recoveryAction(float delta){
            targetVector.set(directionToShip);
                turnShip(delta, false);
                if(position.dst(ship.getPosition())>250){
                    state=AiState.AGGRO;
                }
        }
        
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        
        private void changeHeading(){
            if(targetVector==null){
                targetVector=new Vector2();
            }
            targetVector.x=rand.nextInt(10)-5;
            targetVector.y=rand.nextInt(10)-5;
        }
        
        private void turnShip(float delta, boolean chasing){
            //angles here are in radians
            float target=velocity.angleRad(targetVector);
            float turnAngle;
            
            if(target==0){
                turnAngle=0;
            }else{
                turnAngle=rotationSpeed*delta*(target/Math.abs(target));
            
                if(Math.abs(target)<Math.abs(turnAngle)){
                    turnAngle=target;
                }
            }
            
            if(chasing){
                velocity.rotateRad(turnAngle);
            }else{
                velocity.rotateRad(-turnAngle);
            }
        }
        
        //allows fighter to be steered by external source
        public void setTarget(float x, float y){
            targetVector.set(x, y);
        }
        
        public void setTarget(Vector2 v){
            setTarget(v.x, v.y);
        }
    }
    /*
    ********************************************************
    */
    
    
    
    
    
    @Override
    public void update(float delta){
        shotTimer-=delta;
        
        ai.doStuff(delta);

        //move
        velocity.setLength(speed);
        rotation=velocity.angle();

        lastPos.set(position);  //will help ship know when it is stuck

        super.update(delta);    //sets last delta and changes position according to velocity
    }
    
    
    @Override
    public void fireBullet(ArrayList<ScreenObject> level) {
        shotTimer=getShotInterval(); //reset timer
        firing=false;
        level.add(new EnemyBullet(position.x, position.y, 8, 8, 
                ai.getDirShip())); //velocity doubles as heading
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(image,   //texture region
            position.x-(width/2f), position.y-(height/2f),  //position
            width/2, height/2,     //origin of rotation
            width, height,      //size of region
            0.5f, 0.5f,         //scaling
            rotation);      //rotation
    }

    @Override
    public void collide(ScreenObject other) {
        switch(other.getType()){
            case SHIP: hp-=other.dealDamage();
                break;
            case BULLET:  hp-=other.dealDamage();
                ai.setState(AiState.AGGRO);
                break;
            case ASTEROID:
                //don't go through asteroid
                simpleDeflect(other);
                break;
            }
        
        //death check
        if(hp<1){
            setAlive(false);
        }
    }
    
    @Override
    public void explode(ArrayList<ScreenObject> level){
        level.add(new Explosion(position.x, position.y, 100, 100, 
                Explosion.expSubtype.YELLOW));
    }
    
    /*
    when the fighter runs into a solid object that does not destroy is, it is
    deflected to the side
    */
    protected void simpleDeflect(ScreenObject s){
        Vector2 positionChange=new Vector2(setVectorTo(s));
        float oldDst=positionChange.len();
        float newDst=this.boundingCircle.radius+s.getBoundingCircle().radius;
        positionChange.setLength(oldDst-newDst);
        positionChange.scl(-1);
        position.add(positionChange);
    }
    
    
    
    
    protected void setSprite(TextureRegion region){image=region;}
    protected TextureRegion getSprite(){return image;}
    protected void setSpeed(float speed){this.speed=speed;}
    protected float getSpeed(){return speed;}
    protected void setRotationSpeed(float rotSpeed){rotationSpeed=rotSpeed;}
    
    public FighterAi getAi(){return (FighterAi)ai;}//fighters only--fighter ai has specific methods allowing it to be controlled by outside objects
    
}
