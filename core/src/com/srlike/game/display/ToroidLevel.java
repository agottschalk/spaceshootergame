/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.srlike.game.gameobjects.ScreenObject;
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
 *
 * @author Alex
 * 
 * ********************UNDER CONSTRUCTION**************************
 */
public class ToroidLevel {
    private Random random;
    
    private Ship ship;
    private ArrayList<ScreenObject> gameObjects;        //holds everything
    private ArrayList<Enemy> enemies;
    
    private int levelHeight;
    private int levelWidth;
    private float sectorHeight;
    private float sectorWidth;
    
    private float topLvBound;
    private float bottomLvBound;
    private float rightLvBound;
    private float leftLvBound;
    private float topCtrBound;
    private float bottomCtrBound;
    private float rightCtrBound;
    private float leftCtrBound;
    
    public ToroidLevel(int levelW, int levelH){
        levelWidth=levelW;
        levelHeight=levelH;
        sectorWidth=levelWidth/3;
        sectorHeight=levelHeight/3;
        
        rightLvBound=levelWidth/2;
        leftLvBound=-(levelWidth/2);
        topLvBound=levelHeight/2;
        bottomLvBound=-(levelHeight/2);
        
        rightCtrBound=sectorWidth/2;
        leftCtrBound=-(sectorWidth/2);
        topCtrBound=sectorHeight/2;
        bottomCtrBound=-(sectorHeight/2);
        
        random=new Random();
        
        ship=new Ship(0,0,112,118);
        
        gameObjects=new ArrayList();
        enemies=new ArrayList();
    }
    
    //***********************************************
    //Generating Level
    //***********************************************
    
    public void generate(){
        gameObjects.add(ship);
        generateAsteroids();
        generateEnemies();
        //add powerups
        cleanUp(false);
    }
    
    
    private void generateAsteroids(){
        for(int i=0; i<1000; i++){
            gameObjects.add(new Asteroid(random.nextInt(levelWidth)-(levelWidth/2), 
                    random.nextInt(levelHeight)-(levelHeight/2)));
        }
    }
    
    private void generateEnemies(){
        for(int i=0; i<50; i++){
            enemies.add(new Probe(random.nextInt(levelWidth)-(levelWidth/2), 
                    random.nextInt(levelHeight)-(levelHeight/2), ship, random, 
                    gameObjects));
            gameObjects.add(enemies.get(i));
        }
        
        for(int i=0; i<180; i++){
            enemies.add(new SmFighter(random.nextInt(levelWidth)-(levelWidth/2), 
                    random.nextInt(levelHeight)-(levelHeight/2), ship, random, 
                    gameObjects));
            gameObjects.add(enemies.get(enemies.size()-1));
        }
        
        for(int i=0; i<200; i++){
            enemies.add(new LgFighter(random.nextInt(levelWidth)-(levelWidth/2), 
                    random.nextInt(levelHeight)-(levelHeight/2), ship, random, 
                    gameObjects));
            gameObjects.add(enemies.get(enemies.size()-1));
        }
        
    }
    
    private void cleanUp(boolean spawnDrops){
        //later this will also trigger explosions and powerup drops
        for(int i=0; i<gameObjects.size(); /*blank*/){
            if(!gameObjects.get(i).getAlive()){
                if(spawnDrops){
                    switch (gameObjects.get(i).getType()){
                        case ASTEROID:
                            gameObjects.add(new Explosion(
                                gameObjects.get(i).getPosition().x, 
                                gameObjects.get(i).getPosition().y,
                                100, 100, Explosion.expSubtype.YELLOW));
                            break;
                        case ENEMY:
                            gameObjects.add(new Explosion(
                                gameObjects.get(i).getPosition().x, 
                                gameObjects.get(i).getPosition().y,
                                100, 100, Explosion.expSubtype.YELLOW));
                            break;
                        case BULLET:
                            gameObjects.add(new Explosion(
                                gameObjects.get(i).getPosition().x, 
                                gameObjects.get(i).getPosition().y,
                                40, 40, Explosion.expSubtype.BLUE));
                            break;
                        case ENEMYBULLET:
                            gameObjects.add(new Explosion(
                                gameObjects.get(i).getPosition().x, 
                                gameObjects.get(i).getPosition().y,
                                40, 40, Explosion.expSubtype.RED));
                            break;
                    }
                }
                
                gameObjects.remove(i);
            }else{
                i++;
            }
        }
    }
    
    
    
    
    //***************************************************
    //updating level
    //***************************************************
    
    public void update(float delta){
        //update game objects
        for(ScreenObject s:gameObjects){
            if(ship.getPosition().dst(s.getPosition())<700){    //avoid updating offscreen objects
                s.update(delta);
            }
        }
        
        for(Enemy e: enemies){
            if(e.getFiring()){
                EnemyBullet b=e.fireBullet();
                if(b != null){      //quick error check since all enemies have this method but not all shoot
                    gameObjects.add(b);
                }
            }
        } 
            
        checkCollisions();
        cleanUp(true);
    }
    
    public void shipFire(){
        gameObjects.add(ship.fireBullet());
    }
    
    
    
    
    
    
    
    //*****************************************************
    //collision detection
    //*****************************************************
    private void checkCollisions(){
        for(ScreenObject s: gameObjects){
            Iterator itr=gameObjects.listIterator(gameObjects.indexOf(s));
            while(itr.hasNext()){       
                ScreenObject t=(ScreenObject) itr.next();
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
        return ((g.getPosition().dst(h.getPosition())) < 
                (g.getBoundingCircle().radius+h.getBoundingCircle().radius));
    }
    
    
    
    
    
    //***********************************************
    //getters and setters
    
    public Ship getShip(){return ship;}
    public ArrayList getObjects(){return gameObjects;}
    public ArrayList getEnemies(){return enemies;}
}
