/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.srlike.game.display.Renderer;
import com.srlike.game.display.ScrollHandler;
import com.srlike.game.display.Updater;
import com.srlike.game.gameobjects.Ship;

/**
 *
 * @author Alex
 */
public class InputHandler implements InputProcessor {
    
    private Updater updater;
    private Renderer renderer;
    private OrthographicCamera camera;
    
    private ScrollHandler scroller; //will probably need to call methods from this later
    
    private Ship ship;
    private Vector3 mousePos;   //helps in steering ship
    
    public InputHandler(Updater updater, Renderer renderer){
        this.updater=updater;
        this.renderer=renderer;
        
        camera=this.renderer.getCam();
        
        ship=this.updater.getShip();
        mousePos=new Vector3(0f,0f,0f);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode==Input.Keys.SPACE){
            ship.setEngine(true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode==Input.Keys.SPACE){
            ship.setEngine(false);
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePos.x=screenX;
        mousePos.y=screenY;
        camera.unproject(mousePos);
        ship.rotate(mousePos);
        
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
}
