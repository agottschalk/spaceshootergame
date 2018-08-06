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
    
    private OrthographicCamera camera;
    private OrthographicCamera hudCam;
    
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    
    
    
    public Renderer(float screenW, float screenH){
        this.screenW=screenW;
        this.screenH=screenH;
        
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
    }
    
    /**
     * Draws the current frame.
     */
    public void render(){
        GameScreen game = GameScreen.getInstance();
        Ship player = game.getShip();
        
        //render game screen image
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   //clear screen
        
        camera.position.set(player.getPosition().x, player.getPosition().y, 0f);
        camera.update();
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        //drawing game screen
        for(ScreenObject s:game.getLevel().getObjects()){
            s.draw(batch);
        }
        
        player.draw(batch);
        
        //drawing hud
        batch.setProjectionMatrix(hudCam.combined);
        GameScreen.getInstance().getHud().writeInfo(batch);
        
        batch.end();
        if(showColliders){
            drawCollisionBoxes();
        }
    }
    
    private void drawCollisionBoxes(){
        ToroidLevel level = GameScreen.getInstance().getLevel();
        ArrayList<ScreenObject> screenObjects = level.getObjects();
        
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Line);
        
        //level bounds
        shapeRenderer.setColor(0,0,1,1);
        shapeRenderer.rect(level.getCtrLeftBound(), 
                level.getCtrBottomBound(), 
                level.getSectorWidth(), 
                level.getSectorHeight());
        
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
        Ship player = GameScreen.getInstance().getShip();
        shapeRenderer.setColor(0,1,1,1);
        shapeRenderer.circle(player.getBoundingCircle().x, 
                player.getBoundingCircle().y, 
                player.getBoundingCircle().radius);
        
        //port
        shapeRenderer.setColor(0,1,0,1);
        for (ScreenObject s:screenObjects){
            if(s.getType()==Type.PORT){
                shapeRenderer.circle(s.getBoundingCircle().x, 
                    s.getBoundingCircle().y, s.getBoundingCircle().radius);
                break;
            }
        }
        
        
        shapeRenderer.end();
    }
    
    /**
     * Returns the camera object used in drawing the game (as opposed to the
     * hud or menu elements on the screen)
     * @return game camera
     */
    public OrthographicCamera getGameCam(){return camera;}
    
    /**
     * Toggles whether or not colliders are visible while playing the game.
     * By default, they are not.
     */
    public void toggleShowColliders(){
        showColliders = !showColliders;
    }
}
