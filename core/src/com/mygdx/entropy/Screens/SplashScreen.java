package com.mygdx.entropy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.entropy.Entropy;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen implements Screen {

    private final Entropy entropy;
    private Stage stage;
    

    private Image splashImg;

    public SplashScreen(final Entropy entropy){
        this.entropy = entropy;
        this.stage = new Stage(new FitViewport(Entropy.V_WIDTH, Entropy.V_HEIGHT, entropy.camera));   
    }

    @Override
    public void show() {
        System.out.println("SPLASH SCREEN");
        Gdx.input.setInputProcessor(stage);

        Runnable trasitionRunnable = new Runnable() {
            @Override
            public void run() {
                entropy.setScreen(entropy.mainMenuScreen);
            }
        };


        Texture splashText = entropy.assets.get("images\\entropy_img.png", Texture.class);
        splashImg = new Image(splashText); 
        splashImg.setPosition(stage.getWidth() / 2 -300, stage.getHeight() /2 -100);

        //effects for splash screen
        splashImg.addAction(sequence(alpha(0f), 
        fadeIn(2f), fadeOut(1.25f), 
        run(trasitionRunnable)));
      

        stage.addActor(splashImg); 
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        entropy.batch.begin();
        entropy.font.draw(entropy.batch, "SCREEN: SPLASH", 120, 120);
        entropy.batch.end();
        
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
       stage.getViewport().update(width, height, false);
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
        stage.dispose();
    }

}
