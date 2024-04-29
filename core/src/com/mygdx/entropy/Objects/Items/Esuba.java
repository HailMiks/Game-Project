package com.mygdx.entropy.Objects.Items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.entropy.Objects.GameEntity;
import static com.mygdx.entropy.Utils.Constants.PPM;

public class Esuba extends GameEntity {

    public Body body;
    private TextureAtlas atlas;
    private AtlasRegion esuba;
    private AtlasRegion corpse;

    public Esuba(float width, float height, Body body) {
        super(width, height, body);

        this.atlas = new TextureAtlas("items/item.atlas");
        this.esuba = atlas.findRegion("esuba"); 
        this.corpse = atlas.findRegion("corpse"); 
        
        Fixture fixture = body.getFixtureList().first();
        fixture.setUserData(this);
        fixture.setSensor(false);
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM; 
    }

    public TextureRegion getTexture() {
        return esuba;
    }

    public TextureRegion getDadTexture() {
        return corpse;
    }

    @Override
    public void render(SpriteBatch batch) {     
    }

    public void dispose() {  
        atlas.dispose();
        body.getWorld().destroyBody(body);
        body.setUserData(null);
        body = null;
        esuba = null;
        corpse = null;
        atlas = null;
    }
}
