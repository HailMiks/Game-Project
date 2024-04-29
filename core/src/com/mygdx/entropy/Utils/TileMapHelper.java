package com.mygdx.entropy.Utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.mygdx.entropy.Objects.Enemies.Enemy;
import com.mygdx.entropy.Objects.Items.Button;
import com.mygdx.entropy.Objects.Items.Crayons;
import com.mygdx.entropy.Objects.Items.Crow;
import com.mygdx.entropy.Objects.Items.Esuba;
import com.mygdx.entropy.Objects.Items.Needle;
import com.mygdx.entropy.Objects.Items.PictureFrame;
import com.mygdx.entropy.Objects.Items.Threads;
import com.mygdx.entropy.Objects.Player.Player;
import com.mygdx.entropy.Screens.GScreen;

public class TileMapHelper {

    private TiledMap tiledMap;
    private GScreen gameScreen;

    public TileMapHelper(GScreen gameScreen) {
        this.gameScreen = gameScreen; 
    }

    public OrthogonalTiledMapRenderer setupMap() {
        tiledMap = new TmxMapLoader().load("maps/map1.tmx");
        tiledMap.getLayers().get("background");
        tiledMap.getLayers().get("items");
        parseMapObjects(tiledMap.getLayers().get("objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    private void parseMapObjects(MapObjects mapObjects) {
        for(MapObject mapObject : mapObjects) {

            if(mapObject instanceof PolygonMapObject) {
                createStaticBody((PolygonMapObject) mapObject);
            }

            if(mapObject instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                String rectangleName = mapObject.getName();

                if(rectangleName.equals("Player")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2,
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(),
                        rectangle.getHeight(),
                        false,
                        gameScreen.getWorld()
                    );
                    gameScreen.setPlayer(new Player(rectangle.getWidth(), rectangle.getHeight(), body));
                }

                if(rectangleName.equals("Enemy")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2,
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(),
                        rectangle.getHeight(),
                        false,
                        gameScreen.getWorld()
                    );
                    gameScreen.setEnemy(new Enemy(rectangle.getWidth(), rectangle.getHeight(), body, 1));
                }

                if(rectangleName.equals("Esuba")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2,
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(),
                        rectangle.getHeight(),
                        true,
                        gameScreen.getWorld()
                    );
                    gameScreen.setEsuba(new Esuba(rectangle.getWidth(), rectangle.getHeight(), body));
                }

                if(rectangleName.equals("Needle")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2,
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(),
                        rectangle.getHeight(),
                        true,
                        gameScreen.getWorld()
                    );
                    gameScreen.setNeedle(new Needle(rectangle.getWidth(), rectangle.getHeight(), body));
                }

                if(rectangleName.equals("Button")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2,
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(),
                        rectangle.getHeight(),
                        true,
                        gameScreen.getWorld()
                    );
                    gameScreen.setButton(new Button(rectangle.getWidth(), rectangle.getHeight(), body));
                }

                if(rectangleName.equals("Crow")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2,
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(),
                        rectangle.getHeight(),
                        true,
                        gameScreen.getWorld()
                    );
                    gameScreen.setCrow(new Crow(rectangle.getWidth(), rectangle.getHeight(), body));
                }

                if(rectangleName.equals("Picture")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2,
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(),
                        rectangle.getHeight(),
                        true,
                        gameScreen.getWorld()
                    );
                    gameScreen.setPicture(new PictureFrame(rectangle.getWidth(), rectangle.getHeight(), body));
                }

                if(rectangleName.equals("Crayons")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2,
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(),
                        rectangle.getHeight(),
                        true,
                        gameScreen.getWorld()
                    );
                    gameScreen.setCrayons(new Crayons(rectangle.getWidth(), rectangle.getHeight(), body));
                }

                if(rectangleName.equals("Thread")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2,
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(),
                        rectangle.getHeight(),
                        true,
                        gameScreen.getWorld()
                    );
                    gameScreen.setThreads(new Threads(rectangle.getWidth(), rectangle.getHeight(), body));
                }
                
            } 
        }
    }

    private void createStaticBody(PolygonMapObject polygonMapObject) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        Shape shape = createPolygonShape(polygonMapObject);
        body.createFixture(shape, 1f);
        shape.dispose();
    }

    private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for(int i = 0; i < vertices.length / 2; i++) {
            Vector2 current = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
            worldVertices[i] = current;
        }

        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;
    }
}
