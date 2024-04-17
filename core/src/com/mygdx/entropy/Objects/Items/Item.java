package com.mygdx.entropy.Objects.Items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.entropy.Objects.GameEntity;
import static com.mygdx.entropy.Utils.Constants.PPM;

public class Item extends GameEntity {

    public Body body;

    public Item(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 0f;
        this.body = body;

        body.getFixtureList().first().setUserData(this);
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
