/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects.enemies;

import com.badlogic.gdx.Gdx;
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
public class SmFighter extends Enemy{
    private TextureRegion image;
    
    private final int startingHp;
    private final float speed;
    private final float shotInterval;
    private final float rotationSpeed;
    
    private float shotTimer;
    
    
    public SmFighter(float positionX, float positionY, Ship ship, Random rand, 
            ArrayList<ScreenObject> level) {
        
        super(positionX, positionY, 
                100, 97, //width, height
                20f, //collision radius
                ship, rand, level);
        
        image=AssetLoader.atlas.findRegion("smallfighter");
        
        subtype=Esubtype.SMALLFIGHTER;
        
        startingHp=50;
        speed=130;
        shotInterval=0.6f;
        rotationSpeed=90;
        
        hp=startingHp;
        shotTimer=0;
        
        chooseDirection();
    }
    
    private void chooseDirection(){
        //pick initial starting velocity
        velocity.x=rand.nextFloat()-0.5f;   //nextFloat is between 0 and 1
        velocity.y=rand.nextFloat()-0.5f;
        
        velocity.setLength(speed);
        rotation=velocity.angle();
    }
    
    private void changeDirection(float delta, boolean chasing){
        float target=velocity.angle(directionToShip);
        if(chasing){
            if(target>0){
                //counterclockwise
                velocity.rotate(rotationSpeed*delta);
            }else{
                //clockwise
                velocity.rotate(rotationSpeed*delta*(-1));
            }
        }else{
            if(target>0){
                //clockwise
                velocity.rotate(rotationSpeed*delta*(-1));
            }else{
                //counterclockwise
                velocity.rotate(rotationSpeed*delta);
            }
        }
    }
    
    @Override
    public void update(float delta){
        shotTimer-=delta;
        directionToShip.set(ship.getPosition().x-position.x, 
                ship.getPosition().y-position.y);
        
        if(position.dst(ship.getPosition())<700){   //if too far offscreen, do nothing
            //ai
            switch(state){
                case AGGRO:
                    changeDirection(delta, true);
                    if((shotTimer<0)
                        && ((directionToShip.angle()-velocity.angle()) < 15)
                        && ((directionToShip.angle()-velocity.angle()) > (-15))){

                        firing=true;
                    }
                    if(position.dst(ship.getPosition())<175){
                        state=AiState.RECOVERY;
                    }
                    break;
                case RECOVERY:
                    changeDirection(delta, false);
                    if(position.dst(ship.getPosition())>250){
                        state=AiState.AGGRO;
                    }
                    break;
            }

            //move
            velocity.setLength(speed);
            rotation=velocity.angle();
            super.update(delta);
        }
    }
    
    /*
    ai states:
    passive--not interested in player, just moving, tries to avoid collisions
    aggro--actively chasing after player and firing
    dodge--during aggro, turning around to avoid crashing into player
    */
    
    @Override
    public EnemyBullet fireBullet() {
        shotTimer=shotInterval; //reset timer
        firing=false;
        return new EnemyBullet(position.x, position.y, 8, 8, directionToShip);
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
                state=AiState.AGGRO;
                break;
            case ASTEROID:
                //don't go through asteroid
                
                //position.x+=velocity.x*lastDelta*(-1);  //bounce off asteroid
                //position.y+=velocity.y*lastDelta*(-1);
                
                simpleDeflect(s);
                
                //deflect(s);
                
                break;
            }
        
        //death check
        if(hp<1){
            setAlive(false);
        }
    }
    /*
    private void deflect(ScreenObject s){   //make a test level and debug this method
        Vector2 lineToS=new Vector2(s.getPosition().x-position.x, 
            s.getPosition().y-position.y);
        Gdx.app.log("line to s", lineToS.toString());
        Gdx.app.log("this velocity", this.velocity.toString());
        Vector2 deflection=velocity.cpy();  //will become the vector for how far to deflect ship once calculations are done
        Gdx.app.log("s position", s.getPosition().toString());
        Gdx.app.log("this position", this.position.toString());
        
        if(velocity.angle(lineToS)<0){
            deflection.rotate90(1);     //clockwise
        }else{
            deflection.rotate90(0);       //counterclockwise
        }
        Gdx.app.log("angle", ""+velocity.angle(lineToS));
        Gdx.app.log("deflection 1", deflection.toString());
        
        double h2Sqr=Math.pow(
                boundingCircle.radius+s.getBoundingCircle().radius, 2);
        double cos1Sqr=Math.pow(scalarProj(lineToS, deflection), 2);
        //Gdx.app.log("cos short", ""+cos1Sqr);
        double sineSqr=Math.pow(scalarProj(lineToS, velocity), 2);
        //Gdx.app.log("sine", ""+sineSqr);
        
        double cos2=Math.sqrt(h2Sqr-sineSqr);
        float deflectionDist=(float)(cos2-Math.sqrt(cos1Sqr));
        
        deflection.setLength(deflectionDist);
        
        position.add(deflection);
        Gdx.app.log("deflection 2", deflection.toString());
    }
    */
    
    private void simpleDeflect(ScreenObject s){
        Vector2 lineFromS=new Vector2(position.x-s.getPosition().x, 
            position.y-s.getPosition().y);
        float oldDst=lineFromS.len();
        float newDst=this.boundingCircle.radius+s.getBoundingCircle().radius;
        lineFromS.setLength(newDst-oldDst);
        
        position.add(lineFromS);
    }
    
    /*
    private float scalarProj(Vector2 a, Vector2 b){
        return a.dot(b)/b.len();
    }
    
    private Vector2 vectProj(Vector2 a, Vector2 b){
        Vector2 proj=b.cpy();
        proj.scl(a.dot(b)/b.dot(b));
        return proj;
    }
    */
    
    @Override
    public void changeState(AiState state){
        this.state=state;
        switch (state){
            case AGGRO:
                //do stuff
                break;
        }
    }
    
}
