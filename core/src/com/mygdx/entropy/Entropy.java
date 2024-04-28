package com.mygdx.entropy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.entropy.Screens.GScreen;
import com.mygdx.entropy.Screens.LoadingScreen;
import com.mygdx.entropy.Screens.MainMenuScreen;
import com.mygdx.entropy.Screens.SplashScreen;

public class Entropy extends Game {
	public SpriteBatch batch;
	Texture img;
	
	public BitmapFont font;
	public static Entropy INSTANCE;

    public static int V_WIDTH = 800;
	public static int V_HEIGHT = 600;

	private int widthScreen, heightScreen;
	public OrthographicCamera camera;

	public Entropy(){
		INSTANCE = this;
	}	

	
  

	public AssetManager assets;
	
	public LoadingScreen loadingScreen;
	public SplashScreen splashScreen;
	public MainMenuScreen mainMenuScreen;
	public GScreen mainGScreen;
	
	@Override
	public void create () {
		assets = new AssetManager();
		
		this.widthScreen = Gdx.graphics.getWidth();
		this.heightScreen = Gdx.graphics.getHeight();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, widthScreen, heightScreen);
		//setScreen(new GScreen(camera));	
		
		initFont();

	
    	batch = new SpriteBatch();

		loadingScreen = new LoadingScreen(this);
		splashScreen = new SplashScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		mainGScreen = new GScreen(camera);

		setScreen(loadingScreen);	
    
	}

	@Override
	public void dispose () {
        batch.dispose();
        img.dispose();
		assets.dispose();
		font.dispose();
		loadingScreen.dispose();
		splashScreen.dispose();
		mainMenuScreen.dispose();
		mainGScreen.dispose();
    }

	private void initFont() {
	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font\\HelpMe.ttf"));
	FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

	params.size =24;
	params.color = Color.RED;
	font = generator.generateFont(params);
	
	}
}
