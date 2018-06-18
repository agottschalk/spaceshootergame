/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.badlogic.gdx.Gdx;
import com.srlike.game.gameobjects.Macguffin;
import com.srlike.game.gameobjects.Port;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.ScreenObject.Type;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.enemies.Enemy;
import com.srlike.game.gameobjects.enemies.EnemyBullet;
import com.srlike.game.gameobjects.enemies.Probe;
import com.srlike.game.gameobjects.enemies.fighters.LgFighter;
import com.srlike.game.gameobjects.enemies.fighters.SmFighter;
import com.srlike.game.gameobjects.environment.Asteroid;
import com.srlike.game.gameobjects.environment.Explosion;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Creates a 2d level with the player starting in the center and all other
 * objects placed randomly.  Currently, this class also handles all collision
 * checking and physics that occur each update cycle - essentially, this is
 * where the main game logic is.  This will likely be broken into several
 * classes in the future to make the project more organized.
 * @author Alex
 * 
 * 
 */
public class ToroidLevel {
    private Random random;
    
    private Ship ship;  //player's ship
    private Port port;  //starting point for player
    private ArrayList<ScreenObject> gameObjects;        //holds everything
    private ArrayList<Enemy> enemies;
    private int macguffinCount;
    
    private final int OFFSCREEN_DST;  //distance from ship for an object to be considered
                            //'offscreen'
    
    //dimensions of level and squares of #
    private final int LEVEL_HEIGHT;
    private final int LEVEL_WIDTH;
    private final float SECTOR_HEIGHT;
    private final float SECTOR_WIDTH;
    
    //current positions of level and lines of #
    private float topLvBound;
    private float bottomLvBound;
    private float rightLvBound;
    private float leftLvBound;
    private float topCtrBound;
    private float bottomCtrBound;
    private float rightCtrBound;
    private float leftCtrBound;
    
    
    
    public ToroidLevel(int levelW, int levelH, Random r){
        LEVEL_WIDTH=levelW;
        LEVEL_HEIGHT=levelH;
        SECTOR_WIDTH=LEVEL_WIDTH/3;
        SECTOR_HEIGHT=LEVEL_HEIGHT/3;
        
        rightLvBound=LEVEL_WIDTH/2;
        leftLvBound=-(LEVEL_WIDTH/2);
        topLvBound=LEVEL_HEIGHT/2;
        bottomLvBound=-(LEVEL_HEIGHT/2);
        
        rightCtrBound=SECTOR_WIDTH/2;
        leftCtrBound=-(SECTOR_WIDTH/2);
        topCtrBound=SECTOR_HEIGHT/2;
        bottomCtrBound=-(SECTOR_HEIGHT/2);
        
        OFFSCREEN_DST=700;   //arbitrary for now, maybe switch to a rectangle to better approximate screen?
        
        random=r;
        
        ship=new Ship(0,0,this);
        port=new Port(0, 0, ship, this);
        
        macguffinCount=0;
        
        gameObjects=new ArrayList();
        enemies=new ArrayList();
    }
    
    //***********************************************
    //Generating Level
    //***********************************************
    
    public void generate(){
        gameObjects.add(port);
        gameObjects.add(ship);
        generateAsteroids();
        generateEnemies();
        generatePowerups();
        
        cleanLevel();
    }
    
    
    private void generateAsteroids(){
        for(int i=0; i<1000; i++){
            gameObjects.add(new Asteroid(random.nextInt(LEVEL_WIDTH)-(LEVEL_WIDTH/2), 
                    random.nextInt(LEVEL_HEIGHT)-(LEVEL_HEIGHT/2)));
        }
    }
    
    private void generateEnemies(){
        for(int i=0; i<50; i++){
            enemies.add(new Probe(random.nextInt(LEVEL_WIDTH)-(LEVEL_WIDTH/2), 
                    random.nextInt(LEVEL_HEIGHT)-(LEVEL_HEIGHT/2), this));
            gameObjects.add(enemies.get(i));
        }
        
        for(int i=0; i<180; i++){
            enemies.add(new SmFighter(random.nextInt(LEVEL_WIDTH)-(LEVEL_WIDTH/2), 
                    random.nextInt(LEVEL_HEIGHT)-(LEVEL_HEIGHT/2), this));
            gameObjects.add(enemies.get(enemies.size()-1));
        }
        
        for(int i=0; i<200; i++){
            enemies.add(new LgFighter(random.nextInt(LEVEL_WIDTH)-(LEVEL_WIDTH/2), 
                    random.nextInt(LEVEL_HEIGHT)-(LEVEL_HEIGHT/2), this));
            gameObjects.add(enemies.get(enemies.size()-1));
        }
        
    }
    
    
    private void generatePowerups(){
        for(int i=0; i<30; i++){
            gameObjects.add(new Macguffin(random.nextInt(LEVEL_WIDTH)-(LEVEL_WIDTH/2), 
                    random.nextInt(LEVEL_HEIGHT)-(LEVEL_HEIGHT/2)));
            macguffinCount++;
        }
    }
    
    /*
    removes game objects from level that have been flagged from level.  Objects
    such as explosions or powerups that spawn after an object is destroyed
    are also created by this method
    */
    private void cleanUp(boolean spawnDrops){
        //later this will also trigger explosions and powerup drops
        for(int i=0; i<gameObjects.size(); /*blank*/){
            if(!gameObjects.get(i).getAlive()){
                if(spawnDrops){
                    gameObjects.get(i).explode(gameObjects);
                }
                
                gameObjects.remove(i);
            }else{
                i++;
            }
        }
    }
    
    
    /*
    Removes game objects that spawn overlapping during level creation
    */
    private void cleanLevel(){
        for(ScreenObject s:enemies){   //moves enemies that spawn on port
            if(s.getType()==ScreenObject.Type.ENEMY){
                while(overlaps(s, port)){
                    s.setPosition(random.nextInt(LEVEL_WIDTH)-(LEVEL_WIDTH/2), 
                    random.nextInt(LEVEL_HEIGHT)-(LEVEL_HEIGHT/2));
                }
            }
        }
        
        for(ScreenObject s: gameObjects){      //removes asteroids that spawn on port and enemies
            if(s.getType()==ScreenObject.Type.ASTEROID){
                for(ScreenObject t: gameObjects){
                    if((t.getType()==ScreenObject.Type.ENEMY || t.getType()==ScreenObject.Type.PORT)
                            && overlaps(s,t)){
                        s.setAlive(false);
                    }
                }
            }
        }
        
        cleanUp(false);
    }
    
    
    //***************************************************
    //updating level
    //***************************************************
    
    /*
    Main game loop logic, called once every cycle
    */
    public void update(float delta){
        //using normal 'for' loops, 'for each' creates an iterator each time and causes more gc
        
        
        //update all game objects
        for(int i=0; i<gameObjects.size(); i++){
            ScreenObject s=gameObjects.get(i);
            edgeCheck(s);
            if(ship.getPosition().dst2(s.getPosition())<(OFFSCREEN_DST*OFFSCREEN_DST)){    //avoid updating OFFSCREEN_DST objects
                s.update(delta);
                if(s.getFiring()){s.fireBullet(gameObjects);}
            }
        }
        
        
        scrollLevel();
        checkCollisions();
        cleanUp(true);
    }
    
    
    /*
    Checks if an object has moved past the edge of the level and moves it to
    the opposite side if it has to create a 'wraparound' effect
    */
    private void edgeCheck(ScreenObject s){
        if(s.getPosition().x>rightLvBound){
            s.setPosition(s.getPosition().x-LEVEL_WIDTH, s.getPosition().y);
        }else if(s.getPosition().x<leftLvBound){
            s.setPosition(s.getPosition().x+LEVEL_WIDTH, s.getPosition().y);
        }
        
        if(s.getPosition().y>topLvBound){
            s.setPosition(s.getPosition().x, s.getPosition().y-LEVEL_HEIGHT);
        }else if(s.getPosition().y<bottomLvBound){
            s.setPosition(s.getPosition().x, s.getPosition().y+LEVEL_HEIGHT);
        }
    }
    
    
    
    //level scrolling ************************************
    private void scrollLevel(){
        if(ship.getPosition().x>rightCtrBound){
            scrollRight();
        }else if(ship.getPosition().x<leftCtrBound){
            scrollLeft();
        }
        
        if(ship.getPosition().y>topCtrBound){
            scrollUp();
        }else if(ship.getPosition().y<bottomCtrBound){
            scrollDown();
        }
    }
    
    private void scrollRight(){
        //teleport objects ahead of ship
        for(ScreenObject s:gameObjects){
            if(s.getType()!=Type.SHIP &&
                s.getPosition().x<leftCtrBound){
                s.setPosition(s.getPosition().x+(LEVEL_WIDTH), 
                        s.getPosition().y);
            }
        }
        
        //readjust sector and level boundries
        rightLvBound+=SECTOR_WIDTH;
        leftLvBound+=SECTOR_WIDTH;
        rightCtrBound+=SECTOR_WIDTH;
        leftCtrBound+=SECTOR_WIDTH;
        
    }
    
    private void scrollLeft(){
        for(ScreenObject s:gameObjects){
            if(s.getType()==Type.SHIP){continue;}
            if(s.getPosition().x>rightCtrBound){
                s.setPosition(s.getPosition().x-(LEVEL_WIDTH), 
                        s.getPosition().y);
            }
        }
        
        rightLvBound-=SECTOR_WIDTH;
        leftLvBound-=SECTOR_WIDTH;
        rightCtrBound-=SECTOR_WIDTH;
        leftCtrBound-=SECTOR_WIDTH;
        
    }
    
    private void scrollUp(){
        for(ScreenObject s:gameObjects){
            if(s.getType()==Type.SHIP){continue;}
            if(s.getPosition().y<bottomCtrBound){
                s.setPosition(s.getPosition().x, 
                        s.getPosition().y+(LEVEL_HEIGHT));
            }
        }
        
        topLvBound+=SECTOR_HEIGHT;
        bottomLvBound+=SECTOR_HEIGHT;
        topCtrBound+=SECTOR_HEIGHT;
        bottomCtrBound+=SECTOR_HEIGHT;
    }
    
    private void scrollDown(){
        for(ScreenObject s:gameObjects){
            if(s.getType()==Type.SHIP){continue;}
            if(s.getPosition().y>topCtrBound){
                s.setPosition(s.getPosition().x, 
                        s.getPosition().y-(LEVEL_HEIGHT));
            }
        }
        
        topLvBound-=SECTOR_HEIGHT;
        bottomLvBound-=SECTOR_HEIGHT;
        topCtrBound-=SECTOR_HEIGHT;
        bottomCtrBound-=SECTOR_HEIGHT;
    }
    
    
    
    
    
    
    public void shipFire(){
        ship.fireBullet(gameObjects);
    }
    
    
    
    
    
    
    
    //*****************************************************
    //collision detection
    //*****************************************************
    private void checkCollisions(){
        for(int i=0; i<gameObjects.size(); i++){
            ScreenObject s=gameObjects.get(i);
            for(int j=i; j<gameObjects.size(); j++){       
                ScreenObject t=gameObjects.get(j);
                if(s.getType()==ScreenObject.Type.ASTEROID && t.getType()==ScreenObject.Type.ASTEROID){
                    //continue
                }else if(overlaps(s, t)){
                    s.collide(t);
                    t.collide(s);
                }
            }
        }
    }
    
    private boolean overlaps(ScreenObject g, ScreenObject h){
        float dstSum=g.getBoundingCircle().radius+h.getBoundingCircle().radius;
        return ((g.getPosition().dst2(h.getPosition())) < (dstSum*dstSum));
    }
    
    
    
    @Override
    public String toString(){
        return "level H:"+LEVEL_HEIGHT
                +"\nlevel W:"+LEVEL_WIDTH
                +"\nsector H:"+SECTOR_HEIGHT
                +"\nsector W:"+SECTOR_WIDTH
                +"\nLv top:"+topLvBound
                +"\nLv bottom:"+bottomLvBound
                +"\nLv right:"+rightLvBound
                +"\nLv left:"+leftLvBound
                +"\nSc top:"+topCtrBound
                +"\nSc bottom:"+bottomCtrBound
                +"\nSc right:"+rightCtrBound
                +"\nSc left:"+leftCtrBound;
    }
    
    //***********************************************
    //getters and setters
    
    public Ship getShip(){return ship;}
    public ArrayList getObjects(){return gameObjects;}
    public ArrayList getEnemies(){return enemies;}
    public int getMacguffinCount(){return macguffinCount;}
    public Random getRand(){return random;}
    
    public int getHeight(){return LEVEL_HEIGHT;}
    public int getWidth(){return LEVEL_WIDTH;}
    public float getSectorHeight(){return SECTOR_HEIGHT;}
    public float getSectorWidth(){return SECTOR_WIDTH;}
    
    public float getRightBound(){return rightLvBound;}
    public float getLeftBound(){return leftLvBound;}
    public float getTopBound(){return topLvBound;}
    public float getBottomBound(){return bottomLvBound;}
    public float getCtrRightBound(){return rightCtrBound;}
    public float getCtrLeftBound(){return leftCtrBound;}
    public float getCtrTopBound(){return topCtrBound;}
    public float getCtrBottomBound(){return bottomCtrBound;}
}
