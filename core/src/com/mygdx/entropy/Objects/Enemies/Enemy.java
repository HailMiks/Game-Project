package com.mygdx.entropy.Objects.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.entropy.Objects.GameEntity;
import static com.mygdx.entropy.Utils.Constants.PPM;

public class Enemy extends GameEntity {

    private static final float FRAME_TIME = 1 / 12f;
    private TextureAtlas atlas;
    private Animation<TextureRegion> idle;
    private float elapsedTime;
    enum State { ROAMING, CHASING }
    State state;
    
    private float speed;
    private float chaseRadius;

    public static Body body;

    public Enemy(float width, float height, Body body, float speed, float chaseRadius) {
        super(width, height, body);
        this.speed = 0f;
        this.body = body;
        this.chaseRadius = chaseRadius;

        // Animation
        this.atlas = new TextureAtlas("player/monster.atlas");
        this.idle = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("enemy"));

        body.getFixtureList().first().setUserData(this);
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM; 
        elapsedTime += Gdx.graphics.getDeltaTime();

    }

    @Override
    public void render(SpriteBatch batch) {     
    }

    public TextureRegion getCurrentFrame() {
        if (velX == 0 && velY == 0) 
            return idle.getKeyFrame(elapsedTime, true);
        else return null;
    }

    public void dispose() {  
        atlas.dispose();
    }
}
