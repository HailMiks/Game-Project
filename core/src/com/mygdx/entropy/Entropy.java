package com.mygdx.entropy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entropy extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		GameScreen gs = new GameScreen();
		setScreen(gs);
	}
}
