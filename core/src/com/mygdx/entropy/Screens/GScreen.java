package com.mygdx.entropy.Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.entropy.Utils.Constants;
import com.mygdx.entropy.Utils.ContactListen;
import com.mygdx.entropy.Utils.TileMapHelper;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.mygdx.entropy.Objects.Enemies.Enemy;
import com.mygdx.entropy.Objects.Player.Player;
import com.mygdx.entropy.Objects.Items.Button;
import com.mygdx.entropy.Objects.Items.Crayons;
import com.mygdx.entropy.Objects.Items.Crow;
import com.mygdx.entropy.Objects.Items.Esuba;
import com.mygdx.entropy.Objects.Items.Needle;
import com.mygdx.entropy.Objects.Items.PictureFrame;
import com.mygdx.entropy.Objects.Items.Threads;

public class GScreen extends ScreenAdapter {

    private ExtendViewport viewport;
    private final float WORLD_WIDTH = 1920; // Replace with your desired world width
    private final float WORLD_HEIGHT = 1080;

    // Box2D
    private World world;
    private Box2DDebugRenderer box2dDebugRenderer;
    private RayHandler rayHandler;
    private boolean renderDebug = true;
    ContactListen contactListener;

    // Assets and Map
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;
    private TextureAtlas atlas;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    public static Music music;
    public static Music ambience, scream;
    private Sound lightSound, grab, crowSound;
    private boolean playedScream = false;
    long soundId;

    private PointLight light;
    private Texture crowInv, buttonInv, needleInv, crayonsInv, threadsInv, pictureFrameInv;

    // Game Objects
    private Player player;
    private Enemy enemy;
    private Esuba esuba;
    private Needle needle;
    private Button button;
    private Crayons crayons;
    private PictureFrame pictureFrame;
    private Threads threads;
    private Crow crow;

    public GScreen(OrthographicCamera camera) {
        
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.camera = camera;
        this.batch = new SpriteBatch();
        
        this.contactListener = new ContactListen();

        // Items
        crowInv = new Texture("crow.png");
        buttonInv = new Texture("button.png");
        crayonsInv = new Texture("crayons.png");
        needleInv = new Texture("needle.png");
        pictureFrameInv = new Texture("picture.png");
        threadsInv = new Texture("thread.png");

        // Box2D
        this.world = new World(new Vector2(0, 0), false);
        this.world.setContactListener(contactListener);
        this.box2dDebugRenderer = new Box2DDebugRenderer(
            false,
            false,
            false,
            true,
            false,
            false);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0f, 0f, 0f, 1f);
        rayHandler.setBlurNum(3);
        RayHandler.useDiffuseLight(true);
        rayHandler.setCulling(false);
        rayHandler.setShadows(true);

        this.tileMapHelper = new TileMapHelper(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();

        initLight();

        // Enemy AI
        Vector2 playerPos = player.getBody().getPosition();
        enemy.startTrackingPlayer(playerPos);

        // Audio
        GScreen.music = Gdx.audio.newMusic(Gdx.files.internal("audio/music_box.wav"));
        GScreen.ambience = Gdx.audio.newMusic(Gdx.files.internal("audio/ambience.mp3"));
        lightSound = Gdx.audio.newSound(Gdx.files.internal("audio/matchStick.mp3"));
        scream = Gdx.audio.newMusic(Gdx.files.internal("audio/screaming.mp3"));
        grab = Gdx.audio.newSound(Gdx.files.internal("audio/grab.mp3"));
        crowSound = Gdx.audio.newSound(Gdx.files.internal("audio/crow.mp3"));

        music.setLooping(true);
        ambience.setLooping(true);
        music.setVolume(0.5f);
        scream.setVolume(0.5f);
        scream.setLooping(true);
        ambience.setVolume(0.2f);
        ambience.play();
        crowSound.play();
        music.play();
    }

    private void update() {
        world.step(1 / 60f, 6, 2);
        cameraUpdate();
    
        light.setPosition(player.getBody().getPosition().x * Constants.PPM, player.getBody().getPosition().y * Constants.PPM);
    
        toggleLight(light);
    
        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();
        
        if (light.isActive()) {
            enemy.update();
        }
    
        float dist = player.getBody().getPosition().dst(enemy.getBody().getPosition());

        if (dist < 5f) {
            if (!playedScream) {
                scream.setVolume(0.5f);
                scream.play();
                playedScream = true;
            }
        } else if (dist > 5f && dist <= 15f) {
            float maxVolume = 0.5f; 

            float distanceThreshold = 5.0f; 
            float volume = maxVolume - Math.max(0, Math.min(maxVolume, (dist - distanceThreshold) / 10.0f));
            scream.setVolume(volume);
        } else {
            if (playedScream) {
                scream.stop();
                playedScream = false;
            }
        }

    
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            renderDebug = !renderDebug;
        }
    }  

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = Math.round(player.getBody().getPosition().x * Constants.PPM * 10) / 10f;
        position.y = Math.round(player.getBody().getPosition().y * Constants.PPM * 10) / 10f;
        camera.position.set(position);
        camera.update();
        camera.zoom = Constants.zoom;
    }

    @Override
    public void render(float delta) {
        this.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.render();
        
        if (renderDebug) { 
            box2dDebugRenderer.render(world, camera.combined.scl(Constants.PPM));
        }

        ArrayList<String> inventory = player.inventory;

        handleItemInteraction();

        this.batch.begin();
        // render the objects
        TextureRegion playerAnimation = player.getCurrentFrame();
        TextureRegion enemyAnimation = enemy.getCurrentFrame();
        float playerX = player.getBody().getPosition().x * Constants.PPM - (playerAnimation.getRegionWidth() / 2);
        float playerY = player.getBody().getPosition().y * Constants.PPM - (playerAnimation.getRegionHeight() - 28 / 2);
        float offSet = 65;
        float enemyScaleX = 0.35f;
        float enemyScaleY = 0.35f;

        renderItems();

        batch.draw(playerAnimation, playerX, playerY);

        int invWidth = inventory.size() * 20; 
        float startX = playerX - invWidth/2 + 10;
        int xOffset = 0;

        this.batch.end();
        
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        this.batch.begin();

        batch.draw(enemyAnimation, 
        enemy.getBody().getPosition().x * Constants.PPM - (enemyAnimation.getRegionWidth() / 2), 
        enemy.getBody().getPosition().y * Constants.PPM - (enemyAnimation.getRegionHeight() / 2),
        enemyAnimation.getRegionWidth() / 2, enemyAnimation.getRegionHeight() / 2,
        enemyAnimation.getRegionWidth(), enemyAnimation.getRegionHeight(),
        enemyScaleX, enemyScaleY, 0);

        // Rendering the Items in Inventory
        for(String item : inventory) {

            if(item.equals("Crow")) {
                batch.draw(crowInv, startX + xOffset, playerY - offSet);
                xOffset += 20; 
            }
            
            else if(item.equals("Button")) {
                batch.draw(buttonInv, startX + xOffset, playerY - offSet); 
                xOffset += 20;
            }
            
            else if(item.equals("Needle")) {
                batch.draw(needleInv, startX + xOffset, playerY - offSet);
                xOffset += 20;
            }

            else if(item.equals("Crayons")) {
                batch.draw(crayonsInv, startX + xOffset, playerY - offSet);
                xOffset += 20;
            }

            else if(item.equals("Threads")) {
                batch.draw(threadsInv, startX + xOffset, playerY - offSet);
                xOffset += 20;
            }

            else if(item.equals("Picture")) {
                batch.draw(pictureFrameInv, startX + xOffset, playerY - offSet);
                xOffset += 20;
            }

        }
            
        this.batch.end();

    } 

    private void handleItemInteraction() {
        ArrayList<String> inventory = player.inventory;
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            if(contactListener.esubaInteract) {
                if(inventory.size() == 0) {
                    System.out.println("You must recall your memories");
                    System.out.println("Find 6 Items that will help you remember...");
                } else if(inventory.size() <= 5) {
                    System.out.println("You have collected: " + inventory.size() + " items");
                } else if(inventory.size() == 6) {
                    System.out.println("You have collected: " + inventory.size() + " items");
                    System.out.println("You remember everything... my child...");
                }
            } else if(contactListener.pickNeedle) {
                handleItemPickup("Needle", needle.getBody());
                grab.play(10f);
            } else if(contactListener.pickButton) {
                handleItemPickup("Button", button.getBody());
                grab.play(10f);
            } else if(contactListener.pickCrow) {
                handleItemPickup("Crow", crow.getBody());
                grab.play(10f);
            } else if(contactListener.pickThreads) {
                handleItemPickup("Threads", threads.getBody());
                grab.play(10f);
            } else if(contactListener.pickCrayons) {
                handleItemPickup("Crayons", crayons.getBody());
                grab.play(10f);
            } else if(contactListener.pickPicture) {
                handleItemPickup("Picture", pictureFrame.getBody());
                grab.play(10f);
            }
        }
        enemy.handleInventorySize(inventory); 
    }

    public void handleItemPickup(String itemName, Body itemBody) {
        
        player.inventory.add(itemName);
        insertionSort(player.inventory); 
        System.out.println("Added " + itemName + " to inventory");
        System.out.println("Inventory: " + player.inventory);
        world.destroyBody(itemBody);
        
        if (itemName.equals("Button")) {
            button = null;
        } else if (itemName.equals("Needle")) {
            needle = null;
        } else if (itemName.equals("Crow")) {
            crow = null;
        } else if (itemName.equals("Threads")) {
            threads = null;
        } else if (itemName.equals("Crayons")) {
            crayons = null;
        } else if (itemName.equals("Picture")) {
            pictureFrame = null;
        }        
    }

    public static void insertionSort(ArrayList<String> inventory) {
        int n = inventory.size();
        for (int j = 1; j < n; j++) {
            String key = inventory.get(j);
            int i = j - 1;
            while ((i > -1) && (inventory.get(i).compareTo(key) > 0)) {
                inventory.set(i + 1, inventory.get(i));
                i--;
            }
            inventory.set(i + 1, key);
        }
    }
    
    private void renderItems() {
        if (button != null) {
            TextureRegion buttonTexture = button.getTexture();
            batch.draw(buttonTexture, 
                button.getBody().getPosition().x * Constants.PPM - (buttonTexture.getRegionWidth() / 2), 
                button.getBody().getPosition().y * Constants.PPM - (buttonTexture.getRegionHeight() / 2));
        }

        if (needle != null) {
            TextureRegion needleTexture = needle.getTexture();
            batch.draw(needleTexture, 
                needle.getBody().getPosition().x * Constants.PPM - (needleTexture.getRegionWidth() / 2), 
                needle.getBody().getPosition().y * Constants.PPM - (needleTexture.getRegionHeight() / 2));
        }

        if (crow != null) {
            TextureRegion crowTexture = crow.getTexture();
            batch.draw(crowTexture, 
                crow.getBody().getPosition().x * Constants.PPM - (crowTexture.getRegionWidth() / 2), 
                crow.getBody().getPosition().y * Constants.PPM - (crowTexture.getRegionHeight() / 2));
        }

        if (threads != null) {
            TextureRegion threadsTexture = threads.getTexture();
            batch.draw(threadsTexture, 
                threads.getBody().getPosition().x * Constants.PPM - (threadsTexture.getRegionWidth() / 2), 
                threads.getBody().getPosition().y * Constants.PPM - (threadsTexture.getRegionHeight() / 2));
        }

        if (crayons != null) {
            TextureRegion crayonsTexture = crayons.getTexture();
            batch.draw(crayonsTexture, 
                crayons.getBody().getPosition().x * Constants.PPM - (crayonsTexture.getRegionWidth() / 2), 
                crayons.getBody().getPosition().y * Constants.PPM - (crayonsTexture.getRegionHeight() / 2));
        }

        if (pictureFrame != null) {
            TextureRegion pictureFrameTexture = pictureFrame.getTexture();
            batch.draw(pictureFrameTexture, 
                pictureFrame.getBody().getPosition().x * Constants.PPM - (pictureFrameTexture.getRegionWidth() / 2), 
                pictureFrame.getBody().getPosition().y * Constants.PPM - (pictureFrameTexture.getRegionHeight() / 2));
        }
    }

    public void triggerJumpscare() {
        // Show jumpscare image/animation
      }

    private void initLight() {
        light = new PointLight(rayHandler, 128, new Color(1, 1, 0.7f, 1f), 10, player.getBody().getPosition().x * Constants.PPM, player.getBody().getPosition().y * Constants.PPM);
        light.attachToBody(player.getBody());
        light.setStaticLight(false);
        light.setSoftnessLength(2);
        light.setDistance(2);
        light.setSoft(true);
    }

    public void toggleLight(PointLight light) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            light.setActive(!light.isActive());
            if (light.isActive()) {
                lightSound.play(0.5f);
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public World getWorld() {
        return world;
    }

    public void setPlayer(Player player) {
        this.player = player;
    } 

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    } 

    public void setEsuba(Esuba esuba) {
        this.esuba = esuba;
    } 

    public void setCrow(Crow crow) {
        this.crow = crow;
    } 

    public void setThreads(Threads threads) {
        this.threads = threads;
    } 

    public void setPicture(PictureFrame picture) {
        this.pictureFrame = picture;
    } 

    public void setCrayons(Crayons crayons) {
        this.crayons = crayons;
    } 

    public void setNeedle(Needle needle) {
        this.needle = needle;
    } 

    public void setButton(Button button) {
        this.button = button;
    } 

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        box2dDebugRenderer.dispose();
        orthogonalTiledMapRenderer.dispose();
        crowInv.dispose(); 
        buttonInv.dispose(); 
        needleInv.dispose(); 
        crayonsInv.dispose(); 
        threadsInv.dispose();
        pictureFrameInv.dispose();
        world.dispose();
        music.dispose();
        atlas.dispose();
        rayHandler.dispose();
        light.dispose();
        player.dispose();
        enemy.dispose();
        esuba.dispose();
        needle.dispose();
        crow.dispose();
        threads.dispose();
        pictureFrame.dispose();
        crayons.dispose();
        button.dispose();
        lightSound.dispose();
        scream.dispose();
        grab.dispose();
        super.dispose();
        if (atlas != null) {
            atlas.dispose();
        }
        if (rayHandler != null) {
            rayHandler.dispose();
        }
        if (light != null) {
            light.dispose();
        }
        if (box2dDebugRenderer != null) {
            box2dDebugRenderer.dispose();
        }                
    }
}