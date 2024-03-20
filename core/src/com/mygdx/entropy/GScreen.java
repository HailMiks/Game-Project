package com.mygdx.entropy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
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
import com.mygdx.entropy.Utils.TileMapHelper;
import com.mygdx.entropy.Objects.Player.Player;

public class GScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Music music;
    private World world;
    private Box2DDebugRenderer box2dDebugRenderer;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;

    private TextureAtlas atlas;

    // Game Objects
    private Player player;

    public GScreen(OrthographicCamera camera) {

        this.camera = camera;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, 0), false);
        this.box2dDebugRenderer = new Box2DDebugRenderer();

        this.tileMapHelper = new TileMapHelper(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();

        this.music = Gdx.audio.newMusic(Gdx.files.internal("audio/music_box.wav"));
    
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    private void update() {
        world.step(1 / 60f, 6, 2);
        cameraUpdate();


        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
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
        TextureRegion currentFrame = player.getCurrentFrame();
        batch.draw(currentFrame, 
        player.getBody().getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2), 
        player.getBody().getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() - 28 / 2));
        
        this.batch.end();
        box2dDebugRenderer.render(world, camera.combined.scl(Constants.PPM));
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

    @Override
    public void dispose() {
        batch.dispose();
        box2dDebugRenderer.dispose();
        orthogonalTiledMapRenderer.dispose();
        world.dispose();
        music.dispose();
        atlas.dispose();
    }
}
