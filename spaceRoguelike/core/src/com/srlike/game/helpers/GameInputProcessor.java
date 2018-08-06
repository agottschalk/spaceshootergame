/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.helpers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.srlike.game.display.GameScreen;

/**
 * Handles inputs while on main gameplay screen
 * @author Alex
 */
public class GameInputProcessor implements InputProcessor {
    
    private Vector3 mousePos;   //position of cursor on screen, used to steer ship
    
    public GameInputProcessor(){
        mousePos=new Vector3(0f,0f,0f);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode==Input.Keys.SPACE){
            GameScreen.getInstance().getShip().engineOn();
        }
        
        if(keycode==Input.Keys.TAB){
            GameScreen.getInstance().getRenderer().toggleShowColliders();
        }
        
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode==Input.Keys.SPACE){
            GameScreen.getInstance().getShip().engineOff();
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button==Buttons.LEFT){
            GameScreen.getInstance().getShip().fireBullet(GameScreen.getInstance().getLevel().getObjects());
        }
        
        
        return true;
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
        GameScreen.getInstance().getRenderer().getGameCam().unproject(mousePos);
        GameScreen.getInstance().getShip().rotate(mousePos);
        
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
}
