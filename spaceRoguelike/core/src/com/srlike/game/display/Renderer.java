/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.srlike.game.gameobjects.Asteroid;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.helpers.AssetLoader;
import java.util.ArrayList;

/**
 * this class handles all the drawing and display processes
 * @author Alex
 */
public class Renderer {
    private float screenW, screenH;
    
    private Updater updater;
    
    private OrthographicCamera camera;
    
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    
    private Ship ship;
    private ArrayList<Asteroid> testAsteroids;
    
    private TextureRegion shipSprite;
    private TextureRegion asteroidSprite;
    
    
    public Renderer(Updater updater, float screenW, float screenH){
        this.screenW=screenW;
        this.screenH=screenH;
        
        this.updater=updater;   //renderer can get all game objects from the updater
        
        camera=new OrthographicCamera(screenW, screenH);
        camera.setToOrtho(false, screenW, screenH);
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        camera.update();
        
        batch=new SpriteBatch();
        
        
        shapeRenderer=new ShapeRenderer();
        
        
        loadSprites();
        loadObjects();
    }
    
    private void loadSprites(){
        shipSprite=new TextureRegion(AssetLoader.shipTexture);
        asteroidSprite=new TextureRegion(AssetLoader.asteroidTexture);
        //bigShipS=new Sprite(AssetLoader.bigShipTexture);
        //bigShipS.setPosition(0, 0);
    }
    
    private void loadObjects(){
        ship=updater.getShip();
        testAsteroids=updater.getAsteroids();
    }
    
    public void render(){
        //render game screen image
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.position.set(ship.getPosition().x, ship.getPosition().y, 0f);
        camera.update();
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        //draw asteroids
        for (Asteroid a:testAsteroids){
            batch.draw(asteroidSprite, 
                    a.getPosition().x-(asteroidSprite.getRegionWidth()/2), 
                    a.getPosition().y-(asteroidSprite.getRegionHeight()/2));
        }
        
        //draw ship
        batch.draw(shipSprite, ship.getPosition().x-(shipSprite.getRegionWidth()/2), 
                ship.getPosition().y-(shipSprite.getRegionHeight()/2), 
                shipSprite.getRegionWidth()/2, shipSprite.getRegionHeight()/2, 
                shipSprite.getRegionWidth(), shipSprite.getRegionHeight(), 
                1, 1, ship.getRotation());
        
        
        //hud
        AssetLoader.arialFont.draw(batch, "testing text", screenW/2, screenH/2);
        batch.end();
        
        drawCollisionBoxes();
    }
    
    public void drawCollisionBoxes(){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Line);
        
        //asteroids
        shapeRenderer.setColor(1,1,0,1);
        for (Asteroid a:testAsteroids){
            shapeRenderer.circle(a.getPosition().x, a.getPosition().y, a.getBoundingCircle().radius);
        }
        
        //ship
        shapeRenderer.setColor(1,0,1,1);
        shapeRenderer.circle(ship.getPosition().x, ship.getPosition().y, 
                ship.getBoundingCircle().radius);
        
        
        shapeRenderer.end();
    }
    
    public OrthographicCamera getCam(){return camera;}
    
    
    
    
    
}
