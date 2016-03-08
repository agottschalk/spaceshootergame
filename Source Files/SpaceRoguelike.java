package com.srlike.game;


import com.srlike.game.display.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.srlike.game.helpers.AssetLoader;

public class SpaceRoguelike extends Game {

    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new GameScreen());
    }
	
    @Override
    public void dispose(){
        super.dispose();
        AssetLoader.dispose();
    }
}
