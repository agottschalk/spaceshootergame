/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * this class will load game assets and make them available for the other classes
 * @author Alex
 */
public class AssetLoader {
    public static TextureAtlas atlas;
    
    public static BitmapFont arialFont;
    
    public static void load(){
        atlas=new TextureAtlas(Gdx.files.internal("spriteMap.txt"));
        
        arialFont=new BitmapFont(Gdx.files.internal("arialblack.fnt"));
        
        Gdx.app.log("Asset Loader", "Loaded Assets");
    }
    
    public static void dispose(){
        atlas.dispose();
        
        arialFont.dispose();
    }
}
