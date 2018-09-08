/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.badlogic.gdx.Gdx;
import com.srlike.game.gameobjects.Ship;
import java.util.ArrayList;
import java.util.Random;

/**
 * Updates the state of all objects shown on screen each update cycle.  Can skip
 * updating certain objects which are not in use (ie. skipping updating objects
 * in the level while the game is paused)
 *
 * @author Alex
 */
public class Updater {

    private Random random;
    //debug tools
    private float fps;

    //private ToroidLevel level;

    public Updater() {
        random = new Random();
        
        Gdx.app.log("updater", "created");
    }

    /**
     * Called once every frame, contains main logic for physics updates 
     * (movement, collision, etc.). In the current build it simply calls the
     * ToroidLevel class' update() method, but in the future the logic for
     * updates will be in this method
     * @param delta 
     */
    public void update(float delta) {

        fps = 1 / delta;

        GameScreen.getInstance().getLevel().update(delta);
    }

    public float getFPS() {
        return fps;
    }
}
