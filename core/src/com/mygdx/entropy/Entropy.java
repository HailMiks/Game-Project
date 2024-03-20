package com.mygdx.entropy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entropy extends Game {
	SpriteBatch batch;
	Texture img;
	
	public static Entropy INSTANCE;
	private int widthScreen, heightScreen;
	private OrthographicCamera camera;

	public Entropy(){
		INSTANCE = this;
	}	

	@Override
	public void create () {
		this.widthScreen = Gdx.graphics.getWidth();
		this.heightScreen = Gdx.graphics.getHeight();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, widthScreen, heightScreen);
		setScreen(new GScreen(camera));	
	}
}
