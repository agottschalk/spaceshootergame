/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.helpers.GameInputProcessor;

/**
 * Screen for main gameplay (as opposed to menus, title, etc.) Includes the hud
 * that displays while in the level
 *
 * The GameScreen represents the top of the hierarchy for accessing components
 * of the game such as the player's ship, or the level and objects within it.
 * The Gamescreen itself can be accessed by its static getInstance() method and
 * its subcomponents accessed by calling the respective get methods.
 *
 * @author Alex
 */
public class GameScreen implements Screen {

    private static volatile GameScreen instance;

    private Updater updater;
    private Renderer renderer;
    private Hud hud;
    private ToroidLevel level;
    private Ship ship;

    private float screenW, screenH;

    private GameScreen() {
        Gdx.app.log("GameScreen", "Making screen");
        
        screenW = Gdx.graphics.getWidth();
        screenH = Gdx.graphics.getHeight();

        ship = new Ship(0, 0);
        level = new ToroidLevel();
        updater = new Updater();
        renderer = new Renderer(screenW, screenH);
        hud = new Hud();
        
        Gdx.app.log("GameScreen", "Done making screen");
    }

    /**
     * Returns the current screen of the GameScreen. If there is not one, a new
     * GameScreen will be created.
     *
     * @return screen of GameScreen
     */
    public static GameScreen getInstance() {
        GameScreen screen = GameScreen.instance;
        if (screen == null) {
            synchronized (GameScreen.class) {
                screen = GameScreen.instance;
                if (screen == null) {
                    GameScreen.instance = screen = new GameScreen();
                }
            }
        }

        return screen;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new GameInputProcessor());
        level.generate();
    }

    /**
     * Called once every frame. Resolves physics updates and collisions then
     * draws all objects on screen in their new states
     *
     * @param delta time since last render
     */
    @Override
    public void render(float delta) {
        updater.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    /**
     * Returns the updater object associated with this GameScreen
     *
     * @return updater
     */
    public Updater getUpdater() {
        return updater;
    }

    /**
     * Returns the renderer object associated with this GameScreen
     *
     * @return renderer
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Returns the hud object associated with this GameScreen
     *
     * @return hud
     */
    public Hud getHud() {
        return hud;
    }

    /**
     * Returns the ship object representing the player
     *
     * @return player's ship
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Returns the level currently being played
     *
     * @return current level
     */
    public ToroidLevel getLevel() {
        return level;
    }
}
