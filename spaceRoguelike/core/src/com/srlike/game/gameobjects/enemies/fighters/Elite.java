/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies.fighters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.srlike.game.display.ToroidLevel;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.enemies.Enemy;
import com.srlike.game.gameobjects.enemies.fighters.Fighter.FighterAi;
import com.srlike.game.gameobjects.environment.Explosion;
import com.srlike.game.helpers.AssetLoader;
import com.srlike.game.gameobjects.enemies.StateAi;
import com.srlike.game.gameobjects.enemies.StateAi.AiState;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class Elite extends Enemy{//class under construction
    private TextureRegion image;
    
    private ArrayList<SmFighter> entourage;
    
    private float speed;
    private float rotationSpeed;
    
    public Elite(float positionX, float positionY, ToroidLevel level){
        super(positionX, positionY, 158, 139, 30f);
        
        image=AssetLoader.atlas.findRegion("elite");
        
        subtype=Esubtype.ELITE;
        
        speed=130;
        rotationSpeed=(float)(0.5*Math.PI);
        setStartingHp(100);//arbitrary for now
        
        addEntourage(level);
        
        ai=new EliteAi(AiState.PASSIVE, level);
        ai.init();
    }
    
    private void addEntourage(ToroidLevel level){
        entourage=new ArrayList<SmFighter>();
        
        entourage.add(new SmFighter(position.x+200, position.y+200, level));
        level.getObjects().add(entourage.get(0));
        
        entourage.add(new SmFighter(position.x-200, position.y+200, level));
        level.getObjects().add(entourage.get(1));
        
        entourage.add(new SmFighter(position.x+200, position.y-200, level));
        level.getObjects().add(entourage.get(2));
        
        entourage.add(new SmFighter(position.x-200, position.y-200, level));
        level.getObjects().add(entourage.get(3));
    }
    
    private void updateEntourage(){
        for(int i=0; i<entourage.size(); i++){
            entourage.get(i).getAi().setTarget(velocity);
        }
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
    public void update(float delta){
        shotTimer-=delta;
        
        ai.doStuff(delta);
        
        super.update(delta);//sets lastdelta, moves according to velocity
    }
    
    @Override
    public void collide(ScreenObject s) {
        switch(s.getType()){//for consistency, all other objects use a switch statement here
            case BULLET: hp-=s.dealDamage();
                break;
        }
    }

    @Override
    public void fireBullet(ArrayList<ScreenObject> level) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void explode(ArrayList<ScreenObject> level) {
        level.add(new Explosion((position.x+(ai.getRand().nextInt(80)-40)), 
                (position.y+ai.getRand().nextInt(50)),
                100, 100, Explosion.expSubtype.YELLOW));
        level.add(new Explosion((position.x+(ai.getRand().nextInt(40))), 
                (position.y-ai.getRand().nextInt(50)),
                100, 100, Explosion.expSubtype.YELLOW));
        level.add(new Explosion((position.x-(ai.getRand().nextInt(40))), 
                (position.y-ai.getRand().nextInt(50)),
                100, 100, Explosion.expSubtype.YELLOW));
    }
    

    @Override
    public void dropPowerups(ArrayList<ScreenObject> level) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //*************************************************************
    private class EliteAi extends StateAi{
        private Vector2 targetVector;
        
        public EliteAi(AiState state, ToroidLevel l){
            super(state, Elite.this, l);
        }
        
        @Override
        public void init() {
            //pick initial starting velocity--doubles as heading
            velocity.x=rand.nextFloat()-0.5f;   //nextFloat returns a number between 0 and 1
            velocity.y=rand.nextFloat()-0.5f;

            velocity.setLength(speed);
            rotation=velocity.angle();
            
            changeHeading();    //will initialize targetvecotr
        }
        
        
        
        //vvvvvvvvvvv**ai actions**vvvvvvvvvvv
        @Override
        protected void passiveAction(float delta){
            if(rand.nextInt(250)==0){
                changeHeading();
            }
            steer(delta, true);
            updateEntourage();  //drives entourage ships so they follow leader
        }
        
        @Override
        protected void aggroAction(float delta){
            targetVector.set(directionToShip);
            rotation=targetVector.angleRad();
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
}
