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
 * objects placed randomly. Currently, this class also handles all collision
 * checking and physics that occur each update cycle - essentially, this is
 * where the main game logic is. This will likely be broken into several classes
 * in the future to make the project more organized.
 *
 * @author Alex
 *
 *
 */
public class ToroidLevel {

    private Random random;

    private Port port;  //starting point for player
    private ArrayList<ScreenObject> gameObjects;        //holds everything
    private ArrayList<Enemy> enemies;
    private int macguffinCount;

    private final int OFFSCREEN_DST = 700;  //distance from ship for an object to be considered
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

    //level contents
    private final int NUM_ASTEROIDS = 1000;
    private final int NUM_PROBES = 50;
    private final int NUM_SM_FIGHTERS = 180;
    private final int NUM_LG_FIGHTERS = 200;
    private final int NUM_MACGUFFINS = 30;
    
    /**
     * Creates an empty level centered around (0,0)
     */
    public ToroidLevel() {
        LEVEL_WIDTH = 10000;
        LEVEL_HEIGHT = 6500;
        SECTOR_WIDTH = LEVEL_WIDTH / 3;
        SECTOR_HEIGHT = LEVEL_HEIGHT / 3;

        rightLvBound = LEVEL_WIDTH / 2;
        leftLvBound = -(LEVEL_WIDTH / 2);
        topLvBound = LEVEL_HEIGHT / 2;
        bottomLvBound = -(LEVEL_HEIGHT / 2);

        rightCtrBound = SECTOR_WIDTH / 2;
        leftCtrBound = -(SECTOR_WIDTH / 2);
        topCtrBound = SECTOR_HEIGHT / 2;
        bottomCtrBound = -(SECTOR_HEIGHT / 2);

        random = new Random();
    }

    //***********************************************
    //Generating Level
    //***********************************************
    
    /**
     * Creates all the objects (asteroids, enemies, etc.) in the level. This
     * overwrites the contents of the existing level.  Trying to use the level
     * before this method has been called may fail as the level will be empty.
     */
    public void generate() {
        Gdx.app.log("Level", "started level");
        
        macguffinCount = 0;
        gameObjects = new ArrayList();
        enemies = new ArrayList();
        port = new Port(0, 0);
        
        gameObjects.add(port);
        generateAsteroids(NUM_ASTEROIDS, LEVEL_WIDTH, LEVEL_HEIGHT);
        generateEnemies();
        generatePowerups(NUM_MACGUFFINS, LEVEL_WIDTH, LEVEL_HEIGHT);

        cleanLevel();
        
        Gdx.app.log("level", "level generated");
    }

    
    /*
    generic generator method:
    params: <T extends ScreenObject>, number, level h, level w
    obj = T.newIinstance() <- need to create no arg constructor for everything
    obj.set position
    gameObjects.add(obj)
    */
    private void generateAsteroids(int number, int width, int height) {
        for (int i = 0; i < number; i++) {
            gameObjects.add(new Asteroid(random.nextInt(width) - (width / 2),
                    random.nextInt(height) - (height / 2)));
        }
    }

    private void generateEnemies() {
        for (int i = 0; i < NUM_PROBES; i++) {
            enemies.add(new Probe(random.nextInt(LEVEL_WIDTH) - (LEVEL_WIDTH / 2),
                    random.nextInt(LEVEL_HEIGHT) - (LEVEL_HEIGHT / 2)));
            gameObjects.add(enemies.get(i));
        }

        for (int i = 0; i < NUM_SM_FIGHTERS; i++) {
            enemies.add(new SmFighter(random.nextInt(LEVEL_WIDTH) - (LEVEL_WIDTH / 2),
                    random.nextInt(LEVEL_HEIGHT) - (LEVEL_HEIGHT / 2)));
            gameObjects.add(enemies.get(enemies.size() - 1));
        }

        for (int i = 0; i < NUM_LG_FIGHTERS; i++) {
            enemies.add(new LgFighter(random.nextInt(LEVEL_WIDTH) - (LEVEL_WIDTH / 2),
                    random.nextInt(LEVEL_HEIGHT) - (LEVEL_HEIGHT / 2)));
            gameObjects.add(enemies.get(enemies.size() - 1));
        }

    }

    private void generatePowerups(int number, int levelWidth, int levelHeight) {
        for (int i = 0; i < number; i++) {
            gameObjects.add(new Macguffin(random.nextInt(levelWidth) - (levelWidth / 2),
                    random.nextInt(levelHeight) - (levelHeight / 2)));
            macguffinCount++;
        }
    }

    /*
    removes game objects from level that have been flagged from level.  Objects
    such as explosions or powerups that spawn after an object is destroyed
    are also created by this method
     */
    private void cleanUp(boolean spawnDrops) {
        //later this will also trigger explosions and powerup drops
        for (int i = 0; i < gameObjects.size(); /*blank*/) {
            if (!gameObjects.get(i).getAlive()) {
                if (spawnDrops) {
                    gameObjects.get(i).explode(gameObjects);
                }

                gameObjects.remove(i);
            } else {
                i++;
            }
        }
    }

    /*
    Removes game objects that spawn overlapping during level creation
     */
    private void cleanLevel() {
        for (ScreenObject s : enemies) {   //moves enemies that spawn on port
            if (s.getType() == ScreenObject.Type.ENEMY) {
                while (overlaps(s, port)) {
                    s.setPosition(random.nextInt(LEVEL_WIDTH) - (LEVEL_WIDTH / 2),
                            random.nextInt(LEVEL_HEIGHT) - (LEVEL_HEIGHT / 2));
                }
            }
        }

        for (ScreenObject s : gameObjects) {      //removes asteroids that spawn on port and enemies
            if (s.getType() == ScreenObject.Type.ASTEROID) {
                for (ScreenObject t : gameObjects) {
                    if ((t.getType() == ScreenObject.Type.ENEMY || t.getType() == ScreenObject.Type.PORT)
                            && overlaps(s, t)) {
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
    /**
     * Handles physics updates (ie. movement, collision) each frame. This will
     * eventually be moved to the Updater class
     * @param delta time since last update
     */
    public void update(float delta) {
        Ship ship = GameScreen.getInstance().getShip();
        
        //using normal 'for' loops, 'for each' creates an iterator each time and causes more gc

        //update all game objects
        ship.update(delta);
        for (int i = 0; i < gameObjects.size(); i++) {
            ScreenObject s = gameObjects.get(i);
            edgeCheck(s);
            if (ship.getPosition().dst2(s.getPosition()) < (OFFSCREEN_DST * OFFSCREEN_DST)) {    //avoid updating OFFSCREEN_DST objects
                s.update(delta);
                if (s.getFiring()) {
                    s.fireBullet(gameObjects);
                }
            }
        }

        checkCollisions();
        cleanUp(true);

        scrollLevel();
    }

    /*
    Checks if an object has moved past the edge of the level and moves it to
    the opposite side if it has to create a 'wraparound' effect
     */
    private void edgeCheck(ScreenObject s) {
        if (s.getPosition().x > rightLvBound) {
            s.setPosition(s.getPosition().x - LEVEL_WIDTH, s.getPosition().y);
        } else if (s.getPosition().x < leftLvBound) {
            s.setPosition(s.getPosition().x + LEVEL_WIDTH, s.getPosition().y);
        }

        if (s.getPosition().y > topLvBound) {
            s.setPosition(s.getPosition().x, s.getPosition().y - LEVEL_HEIGHT);
        } else if (s.getPosition().y < bottomLvBound) {
            s.setPosition(s.getPosition().x, s.getPosition().y + LEVEL_HEIGHT);
        }
    }

    //level scrolling ************************************
    /*
    determines whether the level should shift and in which direction
    */
    private void scrollLevel() {
        Ship ship = GameScreen.getInstance().getShip();
        
        if (ship.getPosition().x > rightCtrBound) {
            scrollRight();
        } else if (ship.getPosition().x < leftCtrBound) {
            scrollLeft();
        }

        if (ship.getPosition().y > topCtrBound) {
            scrollUp();
        } else if (ship.getPosition().y < bottomCtrBound) {
            scrollDown();
        }
    }

    /**
     * Shifts the leftmost third of the level to the right side
     */
    private void scrollRight() {
        //teleport objects ahead of ship
        for (ScreenObject s : gameObjects) {
            if (s.getType() != Type.SHIP
                    && s.getPosition().x < leftCtrBound) {
                s.setPosition(s.getPosition().x + (LEVEL_WIDTH),
                        s.getPosition().y);
            }
        }

        //readjust sector and level boundries
        rightLvBound += SECTOR_WIDTH;
        leftLvBound += SECTOR_WIDTH;
        rightCtrBound += SECTOR_WIDTH;
        leftCtrBound += SECTOR_WIDTH;

    }

    /**
     * Shifts the rightmost third of the level to the left side
     */
    private void scrollLeft() {
        for (ScreenObject s : gameObjects) {
            if (s.getType() == Type.SHIP) {
                continue;
            }
            if (s.getPosition().x > rightCtrBound) {
                s.setPosition(s.getPosition().x - (LEVEL_WIDTH),
                        s.getPosition().y);
            }
        }

        rightLvBound -= SECTOR_WIDTH;
        leftLvBound -= SECTOR_WIDTH;
        rightCtrBound -= SECTOR_WIDTH;
        leftCtrBound -= SECTOR_WIDTH;

    }

    /**
     * Shifts the bottom third of the level to the top
     */
    public void scrollUp() {
        for (ScreenObject s : gameObjects) {
            if (s.getType() == Type.SHIP) {
                continue;
            }
            if (s.getPosition().y < bottomCtrBound) {
                s.setPosition(s.getPosition().x,
                        s.getPosition().y + (LEVEL_HEIGHT));
            }
        }

        topLvBound += SECTOR_HEIGHT;
        bottomLvBound += SECTOR_HEIGHT;
        topCtrBound += SECTOR_HEIGHT;
        bottomCtrBound += SECTOR_HEIGHT;
    }

    /**
     * Shifts the top third of the level to the bottom
     */
    public void scrollDown() {
        for (ScreenObject s : gameObjects) {
            if (s.getType() == Type.SHIP) {
                continue;
            }
            if (s.getPosition().y > topCtrBound) {
                s.setPosition(s.getPosition().x,
                        s.getPosition().y - (LEVEL_HEIGHT));
            }
        }

        topLvBound -= SECTOR_HEIGHT;
        bottomLvBound -= SECTOR_HEIGHT;
        topCtrBound -= SECTOR_HEIGHT;
        bottomCtrBound -= SECTOR_HEIGHT;
    }

    //*****************************************************
    //collision detection
    //*****************************************************
    private void checkCollisions() {
        Ship player = GameScreen.getInstance().getShip();
        
        for (int i = 0; i < gameObjects.size(); i++) {
            ScreenObject firstObject = gameObjects.get(i);
            if(overlaps(firstObject, player)){
                firstObject.collide(player);
                player.collide(firstObject);
            }
            for (int j = i; j < gameObjects.size(); j++) {
                ScreenObject secondObject = gameObjects.get(j);
                if (firstObject.getType() == ScreenObject.Type.ASTEROID 
                        && secondObject.getType() == ScreenObject.Type.ASTEROID) {
                    //continue
                } else if (overlaps(firstObject, secondObject)) {
                    firstObject.collide(secondObject);
                    secondObject.collide(firstObject);
                }
            }
        }
    }

    private boolean overlaps(ScreenObject g, ScreenObject h) {
        float dstSum = g.getBoundingCircle().radius + h.getBoundingCircle().radius;
        return ((g.getPosition().dst2(h.getPosition())) < (dstSum * dstSum));
    }

    @Override
    public String toString() {
        return "level H:" + LEVEL_HEIGHT
                + "\nlevel W:" + LEVEL_WIDTH
                + "\nsector H:" + SECTOR_HEIGHT
                + "\nsector W:" + SECTOR_WIDTH
                + "\nLv top:" + topLvBound
                + "\nLv bottom:" + bottomLvBound
                + "\nLv right:" + rightLvBound
                + "\nLv left:" + leftLvBound
                + "\nSc top:" + topCtrBound
                + "\nSc bottom:" + bottomCtrBound
                + "\nSc right:" + rightCtrBound
                + "\nSc left:" + leftCtrBound;
    }

    //***********************************************
    //getters and setters
    public ArrayList<ScreenObject> getObjects() {
        return gameObjects;
    }
    
    public int getMacguffinCount() {
        return macguffinCount;
    }

    public int getHeight() {
        return LEVEL_HEIGHT;
    }

    public int getWidth() {
        return LEVEL_WIDTH;
    }

    public float getSectorHeight() {
        return SECTOR_HEIGHT;
    }

    public float getSectorWidth() {
        return SECTOR_WIDTH;
    }

    public float getRightBound() {
        return rightLvBound;
    }

    public float getLeftBound() {
        return leftLvBound;
    }

    public float getTopBound() {
        return topLvBound;
    }

    public float getBottomBound() {
        return bottomLvBound;
    }

    public float getCtrRightBound() {
        return rightCtrBound;
    }

    public float getCtrLeftBound() {
        return leftCtrBound;
    }

    public float getCtrTopBound() {
        return topCtrBound;
    }

    public float getCtrBottomBound() {
        return bottomCtrBound;
    }
}
