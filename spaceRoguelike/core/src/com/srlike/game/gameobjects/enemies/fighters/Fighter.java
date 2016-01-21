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
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.enemies.Enemy;
import com.srlike.game.gameobjects.enemies.EnemyBullet;
import com.srlike.game.gameobjects.environment.Explosion;
import com.srlike.game.helpers.AssetLoader;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class Fighter extends Enemy{
    private TextureRegion image;
    
    
    private float speed;
    
    private float rotationSpeed;
    
    protected Vector2 lastPos;
    
    
    public Fighter(float positionX, float positionY, 
            int width, int height, float radius,
            Ship ship, Random rand, 
            ArrayList<ScreenObject> level) {
        
        super(positionX, positionY, 
                width, height, radius,
                ship, rand, 
                level);
        
        ai=new FighterAi(AiState.PASSIVE, ship, rand, level);
        lastPos=new Vector2(position);
    }
    
    
    
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
        
        public FighterAi(AiState state, Ship s, Random r, 
                ArrayList<ScreenObject> l){
            super(state, s, r, l);
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
            if(rand.nextInt(250)==0){
                changeHeading();
            }
            steer(delta, true);
        }
        
        @Override
        protected void aggroAction(float delta){
            targetVector.set(directionToShip);
                steer(delta, true);
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
                steer(delta, false);
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
        
        private void steer(float delta, boolean chasing){
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
    public EnemyBullet fireBullet() {
        shotTimer=getShotInterval(); //reset timer
        firing=false;
        return new EnemyBullet(position.x, position.y, 8, 8, ai.getDirShip()); //velocity doubles as heading
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
    public void collide(ScreenObject s) {
        switch(s.getType()){
            case SHIP: hp=0;
                break;
            case BULLET:  hp-=5;
                ai.setState(AiState.AGGRO);
                break;
            case ASTEROID:
                //don't go through asteroid
                simpleDeflect(s);
                break;
            }
        
        //death check
        if(hp<1){
            setAlive(false);
        }
    }
    
    @Override
    public Explosion explode(){
        return new Explosion(position.x, position.y, 100, 100, 
                Explosion.expSubtype.YELLOW);
    }
    
    protected void simpleDeflect(ScreenObject s){
        Vector2 lineFromS=new Vector2(position.x-s.getPosition().x, 
            position.y-s.getPosition().y);
        float oldDst=lineFromS.len();
        float newDst=this.boundingCircle.radius+s.getBoundingCircle().radius;
        lineFromS.setLength(newDst-oldDst);
        
        position.add(lineFromS);
    }
    
    
    
    
    protected void setSprite(TextureRegion region){image=region;}
    protected TextureRegion getSprite(){return image;}
    protected void setSpeed(float speed){this.speed=speed;}
    protected float getSpeed(){return speed;}
    protected void setRotationSpeed(float rotSpeed){rotationSpeed=rotSpeed;}
}
