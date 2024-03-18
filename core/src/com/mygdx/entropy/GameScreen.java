package com.mygdx.entropy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.entropy.Entity.Player;

public class GameScreen implements Screen {
    Stage stage;
    SpriteBatch batch;
    Texture playerTexture;

    OrthographicCamera camera;

    float zoom = 0.35f;

    private TiledMap map;
    OrthogonalTiledMapRenderer renderer;

    private Player player;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch = new SpriteBatch();

        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        renderer.getBatch().begin();
        player.update(delta); // Update player's position based on its velocity
        player.draw(renderer.getBatch()); // Draw the player sprite
        renderer.getBatch().end();
    }

    @Override
    public void show() {
    	map = new TmxMapLoader().load("maps/map1.tmx");
    	
    	renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = zoom; 

        player = new Player(new Sprite(new Texture("down_walk.png")), (TiledMapTileLayer) map.getLayers().get(0));
        player.setPosition(1 * player.getCollisionLayer().getTileWidth(), 1 * player.getCollisionLayer().getTileHeight());

        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }
    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.getTexture().dispose(); 
        map.dispose();
        renderer.dispose();
    }
    
}
