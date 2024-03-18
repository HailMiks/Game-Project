package com.mygdx.entropy.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {
    
    // Movement velocity
    private Vector2 velocity = new Vector2();
 
    private float speed = 50.0f; 
    private TiledMapTileLayer collisionLayer;

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    public Player(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;

        velocity.set(0, 0);
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
    }

    public void draw(SpriteBatch spriteBatch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }
    public void update(float delta) {

        // Old position
        float oldX = getX(), oldY = getY(), tiledWidth = collisionLayer.getTileWidth(), tiledHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        // X position
        setX(getX() + velocity.x * delta);

        // Horizontal collision detection
        if (velocity.x < 0) { // Left
            // Left side
            collisionX = collisionLayer.getCell((int) ((getX() + 4) / tiledWidth), (int) ((getY() + getHeight() / 2) / tiledHeight))
                .getTile().getProperties().containsKey("blocked");
        } else if (velocity.x > 0) { // Right
            // Right side
            collisionX = collisionLayer.getCell((int) ((getX() + getWidth() - 4) / tiledWidth), (int) ((getY() + getHeight() / 2) / tiledHeight))
                .getTile().getProperties().containsKey("blocked");
        }

        // React to Collision on X
        if(collisionX) {
            setX(oldX);
            velocity.x = 0;
        }

        // Y position
        setY(getY() + velocity.y * delta);
        
        // Vertical collision detection
        if (velocity.y < 0) { // Down
            // Bottom side
            collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tiledWidth), (int) (getY() / tiledHeight))
                .getTile().getProperties().containsKey("blocked");
        } else if (velocity.y > 0) { // Up
            // Top side
            collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tiledWidth), (int) ((getY() + getHeight() - 8) / tiledHeight))
                .getTile().getProperties().containsKey("blocked");
        }
        
        // React to Collision on Y
        if(collisionY) {
            setY(oldY);
            velocity.y = 0;
        }
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Keys.A:
                leftPressed = true;
                velocity.x = -speed;
                break;
            case Keys.D:
                rightPressed = true;
                velocity.x = speed;
                break;
            case Keys.W:
                upPressed = true;
                velocity.y = speed;
                break;
            case Keys.S:
                downPressed = true;
                velocity.y = -speed;
                break;
        }
        return true; 
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Keys.A:
                leftPressed = false;
                break;
            case Keys.D:
                rightPressed = false;
                break;
            case Keys.W:
                upPressed = false;
                break;
            case Keys.S:
                downPressed = false;
                break;
        }
        
        // Update velocity based on pressed keys
        if (leftPressed) {
            velocity.x = -speed;
        } else if (rightPressed) {
            velocity.x = speed;
        } else {
            velocity.x = 0;
        }
        
        if (upPressed) {
            velocity.y = speed;
        } else if (downPressed) {
            velocity.y = -speed;
        } else {
            velocity.y = 0;
        }
        
        return true; 
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
