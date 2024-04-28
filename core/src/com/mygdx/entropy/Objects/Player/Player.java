package com.mygdx.entropy.Objects.Player;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.entropy.Objects.GameEntity;
import com.mygdx.entropy.Utils.Constants;

public class Player extends GameEntity {

    private Sound footsteps;
    private long footstepsId;
    private boolean footstepPlaying = false;

    private static final float FRAME_TIME = 1 / 4f;
    private float elapsedTime;
    private TextureAtlas atlas;
    private Animation<TextureRegion> upStill, downStill, leftStill, rightStill, walkUp, walkDown, walkLeft, walkRight;
    private Direction lastDirection = Direction.DOWN;

    public ArrayList<String> inventory;

    public static Body body;

    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 1.8f;
        this.body = body;
        
        // Animation
        this.atlas = new TextureAtlas("player/lumianMove.atlas");
        this.walkUp = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("up_walk"));
        this.walkDown = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("down_walk"));
        this.walkLeft = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("left_walk"));
        this.walkRight = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("right_walk"));
        this.upStill = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("up_still"));
        this.downStill = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("down_still"));
        this.leftStill = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("left_still"));
        this.rightStill = new Animation<TextureRegion>(FRAME_TIME, atlas.findRegions("right_still"));

        inventory = new ArrayList<>();

        // Audio
        footsteps = Gdx.audio.newSound(Gdx.files.internal("audio/walkSFX.mp3"));
        
        body.getFixtureList().first().setUserData(this);
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    @Override
    public void update() {
        x = body.getPosition().x * Constants.PPM;
        y = body.getPosition().y * Constants.PPM; 
        elapsedTime += Gdx.graphics.getDeltaTime();

        boolean isMoving = (velX != 0 || velY != 0);

        // Check the direction of movement
        if (isMoving) {
            if (Math.abs(velX) > Math.abs(velY)) {
                if (velX > 0) {
                    lastDirection = Direction.RIGHT;
                } else {
                    lastDirection = Direction.LEFT;
                }
            } else {
                if (velY > 0) {
                    lastDirection = Direction.UP;
                } else {
                    lastDirection = Direction.DOWN;
                }
            }

            // Play footstep sound if not already playing
            if (!footstepPlaying) {
                footstepsId = footsteps.loop(0.6f, 0.8f, 0);
                footstepPlaying = true;
            }
        } else {
            // Stop footstep sound if the player is not moving
            if (footstepPlaying) {
                footsteps.stop(footstepsId);
                footstepPlaying = false;
            }
        }

        checkUserInput();
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;
        if (velX > 0)
            currentFrame = walkRight.getKeyFrame(elapsedTime, true);
        else if (velX < 0)
            currentFrame = walkLeft.getKeyFrame(elapsedTime, true);
        else if (velY > 0)
            currentFrame = walkUp.getKeyFrame(elapsedTime, true);
        else if (velY < 0)
            currentFrame = walkDown.getKeyFrame(elapsedTime, true);
        else {
            switch (lastDirection) {
                // Use the first frame for the direction
                case UP:
                    currentFrame = upStill.getKeyFrame(0);
                    break;
                case DOWN:
                    currentFrame = downStill.getKeyFrame(0);
                    break;
                case LEFT:
                    currentFrame = leftStill.getKeyFrame(0); 
                    break;
                case RIGHT:
                    currentFrame = rightStill.getKeyFrame(0); 
                    break;
                default:
                    currentFrame = downStill.getKeyFrame(0, true); 
            }
        }        

        batch.draw(currentFrame, x, y);

    }

    public void addItem(String itemName) {
        inventory.add(itemName);
    }
    
    public void removeItem(String itemName) {
        inventory.remove(itemName);
    }

    public ArrayList<String> getInventory() {
        return inventory;
    }

    public TextureRegion getCurrentFrame() {
        if (velX > 0)
            return walkRight.getKeyFrame(elapsedTime, true);
        else if (velX < 0)
            return walkLeft.getKeyFrame(elapsedTime, true);
        else if (velY > 0)
            return walkUp.getKeyFrame(elapsedTime, true);
        else if (velY < 0)
            return walkDown.getKeyFrame(elapsedTime, true);
        else {
            switch (lastDirection) {
                // Return first frame of animation
                case UP:
                    return upStill.getKeyFrame(0, true); 
                case DOWN:
                    return downStill.getKeyFrame(0, true); 
                case LEFT:
                    return leftStill.getKeyFrame(0, true); 
                case RIGHT:
                    return rightStill.getKeyFrame(0, true);
                default:
                    return downStill.getKeyFrame(0, true); 
            }
        }
    }

    private void checkUserInput() {
        velX = 0;
        velY = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
            velX = 1;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
            velX = -1;
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
            velY = 1;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
            velY = -1;


        if (velX > 0)
            lastDirection = Direction.RIGHT;
        else if (velX < 0)
            lastDirection = Direction.LEFT;
        else if (velY > 0)
            lastDirection = Direction.UP;
        else if (velY < 0)
            lastDirection = Direction.DOWN;
        body.setLinearVelocity(velX * speed, velY * speed);

    }

    public void dispose() {
        atlas.dispose();
        footsteps.dispose();   
    }
}
