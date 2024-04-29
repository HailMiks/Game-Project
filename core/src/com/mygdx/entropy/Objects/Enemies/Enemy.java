package com.mygdx.entropy.Objects.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.entropy.Objects.GameEntity;
import static com.mygdx.entropy.Utils.Constants.PPM;

import java.util.ArrayList;

public class Enemy extends GameEntity {

    private static final float FRAME_TIME = 1 / 12f;
    private TextureAtlas atlas;
    private Animation<TextureRegion> idle;
    private float elapsedTime;
    
    public float speed;
    private float baseSpeed;
    private static float chaseRadius = 40f; 
    private static final float startDistance = 40f; 
    private Vector2 playerPosition;
    private boolean isActive;
    // private float timeSinceLastPrint = 0f; 
    public boolean speedIncreased = false;

    
    public static Body body;

    public Enemy(float width, float height, Body body, float speed) {
        super(width, height, body);
        Enemy.body = body;
        this.baseSpeed = speed;

        // Animation
        this.atlas = new TextureAtlas("player/monster.atlas");
        this.idle = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("enemy"));

        body.getFixtureList().first().setUserData(this);
    }

    public void startTrackingPlayer(Vector2 playerPosition) {
        this.playerPosition = playerPosition;

        float startAngle = MathUtils.random(MathUtils.PI2); // Random angle in radians
        float startX = playerPosition.x + startDistance * MathUtils.cos(startAngle);
        float startY = playerPosition.y + startDistance * MathUtils.sin(startAngle);
        new Vector2(startX, startY);

        // Activate enemy and start tracking player
        isActive = true;
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM; 
        elapsedTime += Gdx.graphics.getDeltaTime();

        if (isActive) {
            float dist = body.getPosition().dst(playerPosition);
    
            if (dist <= chaseRadius) {
                isActive = true;
        
                // Chase
                Vector2 direction = playerPosition.sub(body.getPosition());
                body.setLinearVelocity(direction.nor().scl(speed));
        
                // // Print distance every second
                // timeSinceLastPrint += Gdx.graphics.getDeltaTime();
                // if (timeSinceLastPrint >= 1f) {
                //     System.out.println("Distance to player: " + dist);
                //     timeSinceLastPrint = 0f; // Reset timer
                // }
            } 
        }
    }

    public void handleInventorySize(ArrayList<String> inventory) {
        if (inventory.size() > 0) {
            speed = baseSpeed + (inventory.size()) * 0.35f; // Increase speed based on the size of the inventory
        }
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

    public char[] getSpeed() {
        return String.valueOf(speed).toCharArray();
    }
}