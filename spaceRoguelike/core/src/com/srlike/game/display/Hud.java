/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.helpers.AssetLoader;

/**
 * This class controls the HUD for the game.  Currently it is all text, but in
 * the future there will be graphics and icons as well
 * @author Alex
 */
public class Hud {
    //private Updater updater;
    
    private boolean showLevelStats=false;
    private boolean showShipExtraStats=false;
    
    public Hud(){
        Gdx.app.log("hud", "created");
    }
    
    public void writeInfo(SpriteBatch batch){
        //Gdx.app.log("Hud", "writeinfo");
        
        Ship ship = GameScreen.getInstance().getShip();
        ToroidLevel level = GameScreen.getInstance().getLevel();
        
        AssetLoader.arialFont.draw(batch, "Shields: "+ship.getHp(), 30,50);
        AssetLoader.arialFont.draw(batch, "FPS: "+GameScreen.getInstance().getUpdater().getFPS(), 230,50);
        AssetLoader.arialFont.draw(batch, 
                "Macguffins: "+level.getMacguffinCount(), 530, 50);
        AssetLoader.arialFont.draw(batch, "Collected: "+ship.getCollected(), 
                760, 50);
        
        if(showLevelStats){
            AssetLoader.arialFont.draw(batch, level.toString(), 30, 700);
        }
        
        if(showShipExtraStats){
            AssetLoader.arialFont.draw(batch, ship.toString(), 30, 200);
        }
    }
}
