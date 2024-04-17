package com.mygdx.entropy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.entropy.Entropy;

public class SplashScreen implements Screen {

    private SpriteBatch batch;
    private final Entropy entropy;
    private Stage stage;
    private BitmapFont font;

    public SplashScreen(final Entropy entropy){
        this.entropy = entropy;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
    
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        this.batch.begin();
        font.draw(batch, "SPLASH", 120, 120);
        this.batch.end();
    }

    public void update(float delta) {
        stage.act(delta);
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
        font.dispose();
        batch.dispose();
        stage.dispose();
    }

}
