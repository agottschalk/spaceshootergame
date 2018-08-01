/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.srlike.game.gameobjects.Macguffin;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.enemies.Probe;
import com.srlike.game.gameobjects.enemies.fighters.LgFighter;
import com.srlike.game.gameobjects.enemies.fighters.SmFighter;
import com.srlike.game.gameobjects.environment.Asteroid;
import java.util.ArrayList;
import java.util.Random;

/**
 * Singleton class representing a 2d level in the shape of a toroid - the top
 * loops around and connects with the bottom, and the left connects to the
 * right. This implementation works by moving the contents of the level ahead of
 * the ship whenever it nears an edge, so that the objects that were behind it
 * are now in front and the level appears to be repeating itself.
 *
 * @author Productivity
 */
public class SingletonToroid {

    //bounds of # for determining whether to shift level or not
    private float topBound, bottomBound, leftBound, rightBound;

    //level specs - hard coded for now, will later turn into presets, easier to
    //make and edit multiple levels
    private final int LEVEL_WIDTH = 10000;
    private final int LEVEL_HEIGHT = 6500;

    private final int NUM_ASTEROIDS = 1000;
    private final int NUM_PROBES = 50;
    private final int NUM_SM_FIGHTERS = 180;
    private final int NUM_LG_FIGHTERS = 200;
    private final int NUM_MACGUFFINS = 30;

    private ArrayList<ScreenObject> contents;

    private SingletonToroid() {
    }

    public static SingletonToroid getInstance() {
        return SingletonToroidHolder.INSTANCE;
    }

    private static class SingletonToroidHolder {

        private static final SingletonToroid INSTANCE = new SingletonToroid();
    }

    /**
     * Creates a new level, overwriting the old one if present. This method must
     * be called at least once before the other methods can be used.  The center
     * of the level will always be position (0,0).
     */
    public void newLevel() {
        Random random = new Random();

        //asteroids
        for (int i = 0; i < NUM_ASTEROIDS; i++) {
            contents.add(new Asteroid(random.nextInt(LEVEL_WIDTH) - (LEVEL_WIDTH / 2),
                    random.nextInt(LEVEL_HEIGHT) - (LEVEL_HEIGHT / 2)));
        }
        
        //probes
        for (int i = 0; i < NUM_PROBES; i++) {
            contents.add(new Probe(random.nextInt(LEVEL_WIDTH) - (LEVEL_WIDTH / 2),
                    random.nextInt(LEVEL_HEIGHT) - (LEVEL_HEIGHT / 2)));
        }
        
        //small fighters
        for (int i = 0; i < NUM_SM_FIGHTERS; i++) {
            contents.add(new SmFighter(random.nextInt(LEVEL_WIDTH) - (LEVEL_WIDTH / 2),
                    random.nextInt(LEVEL_HEIGHT) - (LEVEL_HEIGHT / 2)));
        }
        
        //large fighters
        for (int i = 0; i < NUM_LG_FIGHTERS; i++) {
            contents.add(new LgFighter(random.nextInt(LEVEL_WIDTH) - (LEVEL_WIDTH / 2),
                    random.nextInt(LEVEL_HEIGHT) - (LEVEL_HEIGHT / 2)));
        }
        
        //collectables
        for (int i = 0; i < NUM_MACGUFFINS; i++) {
            contents.add(new Macguffin(random.nextInt(LEVEL_WIDTH) - (LEVEL_WIDTH / 2),
                    random.nextInt(LEVEL_HEIGHT) - (LEVEL_HEIGHT / 2)));
        }
    }
}
