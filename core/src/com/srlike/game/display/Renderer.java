/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srlike.game.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.srlike.game.gameobjects.ScreenObject;
import com.srlike.game.gameobjects.ScreenObject.Type;
import com.srlike.game.gameobjects.Ship;
import com.srlike.game.helpers.AssetLoader;
import java.util.ArrayList;

/**
 * this class handles all the drawing and display processes
 * @author Alex
 */
public class Renderer {
    //debug
    private boolean showColliders;
    
    private float screenW, screenH;
    
    private Updater updater;
    
    private OrthographicCamera camera;
    private OrthographicCamera hudCam;
    
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    
    private Ship ship;
    private ArrayList<ScreenObject> screenObjects;
    
    
    
    public Renderer(Updater updater, float screenW, float screenH){
        this.screenW=screenW;
        this.screenH=screenH;
        
        this.updater=updater;   //renderer can get all game objects from the updater
        
        camera=new OrthographicCamera(screenW, screenH);
        camera.setToOrtho(false, screenW, screenH);
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        camera.update();
        
        hudCam=new OrthographicCamera(screenW, screenH);
        hudCam.setToOrtho(false, screenW, screenH);
        hudCam.position.set(hudCam.viewportWidth/2f, hudCam.viewportHeight/2f, 0);
        hudCam.update();
        
        batch=new SpriteBatch();
        shapeRenderer=new ShapeRenderer();
        
        showColliders=false;
        
        loadObjects();
    }
    
    
    private void loadObjects(){
        ship=updater.getShip();
        screenObjects=updater.getObjects();
    }
    
    public void render(){
        //render game screen image
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.position.set(ship.getPosition().x, ship.getPosition().y, 0f);
        camera.update();
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        //drawing game screen
        for(ScreenObject s:screenObjects){
            s.draw(batch);
        }
        
        
        //hud
        batch.setProjectionMatrix(hudCam.combined);
        AssetLoader.arialFont.draw(batch, "Shields: "+ship.getHp(), 30,50);
        AssetLoader.arialFont.draw(batch, "FPS: "+updater.getFPS(), 230,50);
        
        batch.end();
        if(showColliders){
            drawCollisionBoxes();
        }
    }
    
    public void drawCollisionBoxes(){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Line);
        
        //level bounds
        shapeRenderer.setColor(0,0,1,1);
        shapeRenderer.rect(updater.getLevel().getCtrLeftBound(), 
                updater.getLevel().getCtrBottomBound(), 
                updater.getLevel().getSectorWidth(), 
                updater.getLevel().getSectorHeight());
        
        //asteroids
        shapeRenderer.setColor(1,1,0,1);
        for (ScreenObject s:screenObjects){
            if(s.getType()==Type.ASTEROID){
                shapeRenderer.circle(s.getBoundingCircle().x, 
                    s.getBoundingCircle().y, s.getBoundingCircle().radius);
            }
        }
        
        //enemies
        shapeRenderer.setColor(1,0,1,1);
        for (ScreenObject s:screenObjects){
            if(s.getType()==Type.ENEMY){
                shapeRenderer.circle(s.getBoundingCircle().x, 
                    s.getBoundingCircle().y, s.getBoundingCircle().radius);
            }
        }
        
        //ship
        shapeRenderer.setColor(0,1,1,1);
        shapeRenderer.circle(ship.getBoundingCircle().x, 
                ship.getBoundingCircle().y, ship.getBoundingCircle().radius);
        
        
        shapeRenderer.end();
    }
    
    public OrthographicCamera getCam(){return camera;}
    
    public void toggleColliders(){
        if(showColliders){
            showColliders=false;
        }else{
            showColliders=true;
        }
    }
}
