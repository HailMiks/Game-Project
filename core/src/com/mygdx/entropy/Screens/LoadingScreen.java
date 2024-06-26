package com.mygdx.entropy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.entropy.Utils.Constants;
import com.mygdx.entropy.Entropy;

public class LoadingScreen implements Screen{

    private final Entropy entropy;

    
    private ShapeRenderer shapeRenderer;
    private float progress;

    public LoadingScreen(final Entropy entropy) {
        this.entropy = entropy;
        this.shapeRenderer = new ShapeRenderer();
    }

    private void queueAssets() {

        entropy.assets.load("images\\entropy_img.png", Texture.class);
        entropy.assets.load("ui\\uiskin.atlas", TextureAtlas.class);
        entropy.assets.load("images\\background.png", Texture.class);
    }

    @Override
    public void show() {
        System.out.println("LOADING SCREEN");
        this.progress = 0f;
        queueAssets();
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, entropy.assets.getProgress(), .1f);
        if(entropy.assets.update() && progress >= entropy.assets.getProgress() - .001f){
            entropy.setScreen(entropy.splashScreen);
        }
        
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        // Set the viewport
        entropy.camera.position.set(Entropy.V_WIDTH / 2f, Entropy.V_HEIGHT / 2f, 0); // Center the camera
        entropy.camera.update();
        shapeRenderer.setProjectionMatrix(entropy.camera.combined);

        shapeRenderer.begin(ShapeType.Filled);

        float rectWidth = entropy.camera.viewportWidth / 3;
        float rectHeight = 5;
        float centerX = (entropy.camera.viewportWidth - rectWidth) / 2;
        float rectY = entropy.camera.viewportHeight / 3;

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(centerX, rectY, rectWidth, rectHeight);

        float progressWidth = progress * rectWidth;

        shapeRenderer.setColor(Color.valueOf(Constants.color));
        shapeRenderer.rect(centerX, rectY, progressWidth, rectHeight);

        shapeRenderer.end();
    
        // entropy.batch.begin();
        // entropy.font.draw(entropy.batch, "SCREEN: LOAD", 120, 120);
        // entropy.batch.end();
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
       shapeRenderer.dispose();
       entropy.assets.unload("images\\entropy_img.png");
       entropy.assets.unload("ui\\uiskin.atlas");
       entropy.assets.unload("images\\background.png");
       entropy.assets.unload("ui\\uiskin.json");
       entropy.assets.unload("ui\\uiskin.png");
    }
    
}
