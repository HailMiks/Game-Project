package com.mygdx.entropy.Objects.Items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.entropy.Objects.GameEntity;
import static com.mygdx.entropy.Utils.Constants.PPM;

public class PictureFrame extends GameEntity {

    private TextureAtlas atlas;
    
    public Body body;

    private AtlasRegion picture;

    public PictureFrame(float width, float height, Body body) {
        super(width, height, body);
        
        this.atlas = new TextureAtlas("items/item.atlas");
        this.picture = atlas.findRegion("pictureItem"); 
        
        Fixture fixture = body.getFixtureList().first();
        fixture.setUserData(this);
        fixture.setSensor(true);
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM; 
    }

    @Override
    public void render(SpriteBatch batch) {     
    }

    public TextureRegion getTexture() {
        return picture;
    }

    public void dispose() {  
        atlas.dispose();
        picture.getTexture().dispose();
    }
}
