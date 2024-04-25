package com.mygdx.entropy.Objects.Items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.entropy.Objects.GameEntity;
import static com.mygdx.entropy.Utils.Constants.PPM;

public class Esuba extends GameEntity {

    public Body body;

    public Esuba(float width, float height, Body body) {
        super(width, height, body);
        
        Fixture fixture = body.getFixtureList().first();
        fixture.setUserData(this);
        fixture.setSensor(false);
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM; 
    }

    @Override
    public void render(SpriteBatch batch) {     
    }

    public void dispose() {  
    }
}
