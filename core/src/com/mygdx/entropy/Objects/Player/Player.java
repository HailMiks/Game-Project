package com.mygdx.entropy.Objects.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.entropy.Utils.Constants;

public class Player extends GameEntity {

    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 2.5f;
    }

    @Override
    public void update() {
        x = body.getPosition().x * Constants.PPM;
        y = body.getPosition().y * Constants.PPM; 

        checkUserInput();
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    private void checkUserInput() {
        velX = 0;
        velY = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.D))
         velX = 1;
        if(Gdx.input.isKeyPressed(Input.Keys.A))
         velX = -1;
        if(Gdx.input.isKeyPressed(Input.Keys.W))
         velY = 1;
        if(Gdx.input.isKeyPressed(Input.Keys.S))
         velY = -1;

        body.setLinearVelocity(velX * speed, velY * speed);
    }
}
