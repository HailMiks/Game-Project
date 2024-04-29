package com.mygdx.entropy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.mygdx.entropy.Entropy;

public class MainMenuScreen implements Screen{

    private final Entropy entropy;

    private Stage stage;
    private Skin skin;
    private Image background;
    
    private TextButton buttonPlay, buttonExit;

    private ShapeRenderer shapeRenderer;
    
    public MainMenuScreen(final Entropy entropy) {
        this.entropy = entropy;
        this.stage = new Stage(new FitViewport(Entropy.V_WIDTH, Entropy.V_HEIGHT, entropy.camera));
        this.shapeRenderer = new ShapeRenderer();
        
    }
    
    @Override
    public void show() {
        System.out.println("MAIN MENU");
        Texture bgTexture = entropy.assets.get("images\\background.png", Texture.class);
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        // Create background image
        background = new Image(bgTexture);
        background.setFillParent(true);
        
        // Add background to stage
        stage.addActor(background);

        this.skin = new Skin();
        this.skin.addRegions(entropy.assets.get("ui\\uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", entropy.font);
        this.skin.load(Gdx.files.internal("ui\\uiskin.json"));

        initButton();
    }




    @Override 
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background 
        stage.draw();

        // Draw UI
        stage.act(delta);
        stage.draw();

        // entropy.batch.begin();
        // entropy.font.draw(entropy.batch, "SCREEN: MAIN MENU", 120, 120);
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
        stage.dispose();
        
    }
    

    private void initButton() {
        float buttonWidth = 100; 
        float stageWidth = stage.getWidth();
        float centerX = (stageWidth - buttonWidth) / 2;
    
        buttonPlay = new TextButton("Play", skin, "default");
        buttonPlay.setSize(100, 40);
        buttonPlay.setPosition(centerX, 130); 
        buttonPlay.addAction(sequence(alpha(0),
                parallel(fadeIn(0.5f),
                        moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                entropy.setScreen(entropy.mainGScreen);
                GScreen.music.stop();
                GScreen.ambience.setVolume(0.5f);
            
                Gdx.input.setCursorCatched(true);
            }
        });
    
        buttonExit = new TextButton("Exit", skin, "default");
        buttonExit.setSize(100, 35); 
        buttonExit.setPosition(centerX, 80); 
        buttonExit.addAction(sequence(alpha(0),
                parallel(fadeIn(0.5f),
                        moveBy(0, -20, .5f, Interpolation.pow5Out))));
    
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    
        stage.addActor(buttonPlay);
        stage.addActor(buttonExit);
    }
}    
