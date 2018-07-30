/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

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

    private ToroidLevel level;

    public Updater(ToroidLevel level) {
        random = new Random();
        this.level = level;
    }

    public void update(float delta) {

        fps = 1 / delta;

        level.update(delta);
    }

    public void shipFire() {
        level.shipFire();
    }

    public float getFPS() {
        return fps;
    }

    public Ship getShip() {
        return level.getShip();
    }

    public ArrayList getObjects() {
        return level.getObjects();
    }

    public ToroidLevel getLevel() {
        return level;
    }

}
