package com.mygdx.entropy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.entropy.Entropy;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen implements Screen {

    private final Entropy entropy;
    private Stage stage;
    private BitmapFont font;
    private Image splashImg;
    private SpriteBatch batch;


    public SplashScreen(final Entropy entropy){
        this.entropy = entropy;
        this.stage = new Stage(new FitViewport(Entropy.V_WIDTH, Entropy.V_HEIGHT, entropy.camera));   
        this.batch = new SpriteBatch(); 
    }

    @Override
    public void show() {
        System.out.println("SPLASH SCREEN");
        Gdx.input.setInputProcessor(stage);

        Runnable transitionRunnable = new Runnable() {
            @Override
            public void run() {
                entropy.setScreen(entropy.mainMenuScreen);
            }
        };


        Texture splashText = entropy.assets.get("images\\entropy_img.png", Texture.class);
        splashImg = new Image(splashText); 
        splashImg.setPosition(stage.getWidth() / 2 - 300, stage.getHeight() /2 -100);

        //effects for splash screen
        splashImg.addAction(sequence(alpha(0f), 
        fadeIn(3f), fadeOut(3f), 
        run(transitionRunnable)));

        stage.addActor(splashImg); 
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
        font = new BitmapFont(); 
        font.getData().setScale(1f);
        font.setColor(Color.WHITE);

        batch.setProjectionMatrix(entropy.camera.combined); 

        batch.begin();
        GlyphLayout layout = new GlyphLayout(font, "This game contains violence and gore, and can cause fear, depression,"); // Compute the layout
        float textX = (stage.getWidth() - layout.width) / 2; 
        float textY = stage.getHeight() / 2 + layout.height - 250; 
        font.draw(batch, "This game contains violence and gore, and can cause fear, depression,", textX, textY);
        layout.setText(font, "heart failure and suicide. Users and viewers discretion is advised.");
        textX = (stage.getWidth() - layout.width) / 2; 
        font.draw(batch, "heart failure and suicide. Users and viewers discretion is advised.", textX, textY - layout.height - 5);
        batch.end();
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
        batch.dispose();
        font.dispose();
    }

}
