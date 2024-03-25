package com.mygdx.entropy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.entropy.Utils.Constants;
import com.mygdx.entropy.Utils.ContactListen;
import com.mygdx.entropy.Utils.TileMapHelper;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.mygdx.entropy.Objects.Enemies.Enemy;
import com.mygdx.entropy.Objects.Player.Player;

public class GScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Music music;
    private Sound lightSound;

    private World world;
    private Box2DDebugRenderer box2dDebugRenderer;
    private RayHandler rayHandler;
    private boolean renderDebug = true;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;

    private TextureAtlas atlas;

    // Game Objects
    private Player player;
    private Enemy enemy;
    private PointLight light; 

    public GScreen(OrthographicCamera camera) {

        this.camera = camera;
        this.batch = new SpriteBatch();
        
        // Box2D
        this.world = new World(new Vector2(0, 0), false);
        this.world.setContactListener(new ContactListen());
        this.box2dDebugRenderer = new Box2DDebugRenderer(
            true,
            false,
            false,
            true,
            false,
            false);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 1f);
        rayHandler.setBlurNum(3);
        RayHandler.useDiffuseLight(true);
        rayHandler.setCulling(false);
        rayHandler.setShadows(true);

        this.tileMapHelper = new TileMapHelper(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();

        initLight();

        // Audio
        this.music = Gdx.audio.newMusic(Gdx.files.internal("audio/music_box.wav"));
        lightSound = Gdx.audio.newSound(Gdx.files.internal("audio/matchStick.mp3"));

        music.setLooping(true);
        music.setVolume(0.5f);
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
        enemy.update();

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

        this.batch.begin();
        // render the objects
        TextureRegion playerAnimation = player.getCurrentFrame();
        TextureRegion enemyAnimation = enemy.getCurrentFrame();
        float enemyScaleX = 0.15f;
        float enemyScaleY = 0.15f;

        batch.draw(enemyAnimation, 
            enemy.getBody().getPosition().x * Constants.PPM - (enemyAnimation.getRegionWidth() / 2), 
            enemy.getBody().getPosition().y * Constants.PPM - (enemyAnimation.getRegionHeight() / 2),
            enemyAnimation.getRegionWidth() / 2, enemyAnimation.getRegionHeight() / 2,
            enemyAnimation.getRegionWidth(), enemyAnimation.getRegionHeight(),
            enemyScaleX, enemyScaleY, 0);

        batch.draw(playerAnimation, 
            player.getBody().getPosition().x * Constants.PPM - (playerAnimation.getRegionWidth() / 2), 
            player.getBody().getPosition().y * Constants.PPM - (playerAnimation.getRegionHeight() - 28 / 2));

        this.batch.end();

        if (renderDebug) { 
            box2dDebugRenderer.render(world, camera.combined.scl(Constants.PPM));
        }
        
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
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

    private void initLight() {
        light = new PointLight(rayHandler, 128, new Color(1, 1, 0.7f, 1f), 10, player.getBody().getPosition().x * Constants.PPM, player.getBody().getPosition().y * Constants.PPM);
        light.attachToBody(player.getBody());
        light.setStaticLight(false);
        light.setSoftnessLength(2);
        light.setDistance(3);
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
    
    @Override
    public void dispose() {
        batch.dispose();
        box2dDebugRenderer.dispose();
        orthogonalTiledMapRenderer.dispose();
        world.dispose();
        music.dispose();
        atlas.dispose();
        rayHandler.dispose();
        light.dispose();
        player.dispose();
        enemy.dispose();
    }
}
