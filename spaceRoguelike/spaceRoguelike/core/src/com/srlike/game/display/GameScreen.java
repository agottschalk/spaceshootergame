/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.srlike.game.display.Renderer;
import com.srlike.game.display.Updater;
import com.srlike.game.helpers.InputHandler;

/**
 *
 * @author Alex
 */
public class GameScreen implements Screen {
    private Updater updater;
    private Renderer renderer;
    
    private float screenW, screenH;
    
    public GameScreen(){
        screenW=Gdx.graphics.getWidth();
        screenH=Gdx.graphics.getHeight();
        
        updater=new Updater(screenW, screenH);
        renderer=new Renderer(updater, screenW, screenH);
        
        Gdx.input.setInputProcessor(new InputHandler(updater, renderer));
        
    }
    
    @Override
    public void show() {
    }

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
    
}
