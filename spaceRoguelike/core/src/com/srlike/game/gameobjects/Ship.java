/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.srlike.game.display.ToroidLevel;
import com.srlike.game.gameobjects.environment.Explosion;
import com.srlike.game.helpers.AssetLoader;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class Ship extends ScreenObject {
    private TextureRegion image;
    
    private boolean engine=false;
    
    private Vector2 direction;
    
    private final float acceleration=500f;
    private final float speedCap=600f;
    
    private ToroidLevel level;
    private int macguffinsCollected;
    
    //level end animation
    private boolean locked=false;
    private int framesRemaining=30;
    
    public Ship(float positionX, float positionY, //onscreen starting position, center of sprite
        ToroidLevel level)      //width and height of displayed sprite, helps with drawing)
    {
        super(positionX, positionY, 112, 118, 32);
        type=Type.SHIP;
        
        image=AssetLoader.atlas.findRegion("playership");
        
        this.level=level;
        
        //ship motion vectors
        direction=new Vector2(0f, 0f);
        
        collisionDamage=9999;
        
        hp=100;
        macguffinsCollected=0;
    }
    
    @Override
    public void update(float delta){
        if(engine){
            //accelerate
            velocity.x+=acceleration*direction.x*delta;
            velocity.y+=acceleration*direction.y*delta;
        }else{
            //decelerate
            velocity.scl((1-(delta/2)));    //(1-delta/2) approx= 0.991
            
            //ship stops when speed is close to 0
            if(velocity.len()<15){
                velocity.setZero();
            }
        }
        //cap speed
        if(velocity.len()>speedCap){
            velocity.setLength(speedCap);
        }
        
        //adjust ship position
        super.update(delta);
    }
    
    public void rotate(Vector3 mousePos){
        direction.x=mousePos.x-position.x;
        direction.y=mousePos.y-position.y;
        direction.nor();
        rotation=direction.angle();
    }
    
    
    public Vector2 getDirection(){return direction;}
    public boolean getEngine(){return engine;}
    public void setEngine(boolean e){engine=e;}
    public int getCollected(){return macguffinsCollected;}
    
    //for debugging, might not be in final game
    //public void setX(float x){position.x=x;}
    //public void setY(float y){position.y=y;}

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
            case ASTEROID:
                hp-=s.dealDamage();
                break;
            case ENEMY:
                hp-=s.dealDamage();
                break;
            case ENEMYBULLET:
                hp-=s.dealDamage();
                break;
            case MACGUFFIN:
                macguffinsCollected++;
                break;
            case PORT:
                if(macguffinsCollected>=level.getMacguffinCount()
                        && boundingCircle.contains(s.getPosition())){
                    Gdx.app.log("Ship", "level over");
                //ends level
                }
                break;
        }
    }
    
    @Override
    public String toString(){
        return "Ship X:"+position.x
                +"\nShip Y:"+position.y;
    }
    
    @Override
    public void setPosition(float x, float y){}

    @Override
    public void fireBullet(ArrayList<ScreenObject> level) {
        level.add(new ShipBullet(position.x, position.y, 8, 8, direction));
    }

    @Override
    public void explode(ArrayList<ScreenObject> level) {
        level.add(new Explosion(position.x, position.y, 100, 100, 
                Explosion.expSubtype.YELLOW));
    }

    @Override
    public void dropPowerups(ArrayList<ScreenObject> level) {}//does not drop powerups
}
