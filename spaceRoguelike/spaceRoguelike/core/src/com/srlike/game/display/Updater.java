/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.srlike.game.gameobjects.Ship;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.ScreenObject.Type;
import com.srlike.game.gameobjects.enemies.Enemy;
import com.srlike.game.gameobjects.enemies.EnemyBullet;
import com.srlike.game.gameobjects.enemies.Probe;
import com.srlike.game.gameobjects.enemies.SmFighter;
import com.srlike.game.gameobjects.environment.Asteroid;
import com.srlike.game.gameobjects.environment.Explosion;
import com.srlike.game.gameobjects.environment.Explosion.expSubtype;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * this class keeps track of all game objects during game play(called "world" in tutorial)
 * @author Alex
 */
public class Updater {
    private float fps;
    
    private Random random;
    
    //stuff in level
    private Ship ship;
    private ArrayList<ScreenObject> gameObjects;        //holds everything
    private ArrayList<Enemy> enemies;
    
    
    
    public Updater(float screenW, float screenH){
        random=new Random();    //for testing right now, may move this object later
        
        ship=new Ship(0,0,112,118);
        
        gameObjects=new ArrayList();
        enemies=new ArrayList();
        
        generateLevel();
    }
    
    
    public void update(float delta){
        
        fps=1/delta;
        
        //update game objects
        for(ScreenObject s:gameObjects){
            s.update(delta);
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
    
    private void checkCollisions(){
        for(ScreenObject s: gameObjects){
            Iterator itr=gameObjects.listIterator(gameObjects.indexOf(s));
            while(itr.hasNext()){       
                ScreenObject t=(ScreenObject) itr.next();
                if(s.getType()==Type.ASTEROID && t.getType()==Type.ASTEROID){
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
                                100, 100, expSubtype.YELLOW));
                            break;
                        case ENEMY:
                            gameObjects.add(new Explosion(
                                gameObjects.get(i).getPosition().x, 
                                gameObjects.get(i).getPosition().y,
                                100, 100, expSubtype.YELLOW));
                            break;
                        case BULLET:
                            gameObjects.add(new Explosion(
                                gameObjects.get(i).getPosition().x, 
                                gameObjects.get(i).getPosition().y,
                                40, 40, expSubtype.BLUE));
                            break;
                        case ENEMYBULLET:
                            gameObjects.add(new Explosion(
                                gameObjects.get(i).getPosition().x, 
                                gameObjects.get(i).getPosition().y,
                                40, 40, expSubtype.RED));
                            break;
                    }
                }
                
                gameObjects.remove(i);
            }else{
                i++;
            }
        }
    }
    
    public void shipFire(){
        gameObjects.add(ship.fireBullet());
    }
    
    public float getFPS(){return fps;}
    public Ship getShip(){return ship;}
    public ArrayList getObjects(){return gameObjects;}
    
    
    private void generateLevel(){
        gameObjects.add(ship);
        generateAsteroids();
        generateEnemies();
        //add powerups
        cleanLevel();
    }
    
    private void cleanLevel(){      //removes asteroids that spawn on ship and enemies
        for(ScreenObject s: gameObjects){
            if(s.getType()==Type.ASTEROID){
                Iterator itr=gameObjects.listIterator(gameObjects.indexOf(s));
                while(itr.hasNext()){       
                    ScreenObject t=(ScreenObject) itr.next();
                    if((t.getType()==Type.ENEMY || t.getType()==Type.SHIP)
                            && overlaps(s,t)){
                        s.setAlive(false);
                    }
                }
            }
        }
        
        cleanUp(false);
    }
    
    //offscreen=649 pixels away
    private void generateAsteroids(){
        for(int i=0; i<1000; i++){
            gameObjects.add(new Asteroid(random.nextInt(10000)-5000, 
                    random.nextInt(6500)-3250)); //approx 9 screens by 9 screens
        }
    }
    
    private void generateEnemies(){
        for(int i=0; i<50; i++){
            enemies.add(new Probe(random.nextInt(10000)-5000, 
                    random.nextInt(6500)-3250, ship, random, gameObjects));
            gameObjects.add(enemies.get(i));
        }
        
        for(int i=0; i<180; i++){
            enemies.add(new SmFighter(random.nextInt(10000)-5000, 
                    random.nextInt(6500)-3250, ship, random, gameObjects));
            gameObjects.add(enemies.get(enemies.size()-1));
        }
    }
}
