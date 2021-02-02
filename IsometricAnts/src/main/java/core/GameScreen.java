package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import helper.Const;
import objects.ants.AntAbstract;
import objects.ants.AntWorker;
import objects.ants.task.WalkToTask;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL20;
import pathfinding.Graph;
import pathfinding.Node;

import static helper.Const.TILE_HEIGHT;
import static helper.Const.TILE_WIDTH;
import static helper.Functions.*;


public class GameScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private TiledMap tiledMap;
    private int mapWidth;
    private int mapHeight;
    private IsometricTiledMapRenderer isometricTiledMapRenderer;

    private Graph graph;
    private Handler handler;

    private AntAbstract antAbstract;

    public GameScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0,0), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        Gdx.input.setInputProcessor(new helper.Input(camera));
        this.graph = new Graph();

        this.setUpTiledMap();
        this.camera.position.set(new Vector3(mapWidth / 2, 0, 0));
        this.handler = new Handler();
    }

    public void update() {
        world.step(1 / 60f, 6, 2);
        userInput();
        this.camera.update();
        batch.setProjectionMatrix(camera.combined);

        handler.update();

        isometricTiledMapRenderer.setView(camera);
    }

    @Override
    public void render(float delta){
        update();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        isometricTiledMapRenderer.render();

        batch.begin();
        handler.render(batch);
        batch.end();

        this.box2DDebugRenderer.render(world, camera.combined.scl(Const.PPM));
    }

    public World getWorld() {
        return world;
    }

    private void setUpTiledMap() {
        tiledMap = new TmxMapLoader().load("map/mapBig.tmx");
        isometricTiledMapRenderer = new IsometricTiledMapRenderer(tiledMap);

        // add nodes
        MapLayers mapLayers = tiledMap.getLayers();
        int width = ((TiledMapTileLayer) mapLayers.get("layer1")).getWidth();
        int height = ((TiledMapTileLayer) mapLayers.get("layer1")).getHeight();

        mapWidth = width * TILE_WIDTH;
        mapHeight = height * TILE_HEIGHT;

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) mapLayers.get("layer1")).getCell(x, y);
                if(cell != null) {
                    int ty = height - y - 1;
                    graph.addNode(new Node(x, ty));
                }
            }
        }
        graph.createMatrix();
    }

    private void userInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            camera.position.x += 10;
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            camera.position.x -= 10;
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            camera.position.y += 10;
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            camera.position.y -= 10;

        // spawn ant
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Vector2 vec2 = transformTiledMapCoordinatesLeftToTop(camera, mapWidth, mapHeight);
            // transform to center of tile
            Vector2 gridPos = transformCoordinatesToGrid(vec2.x, vec2.y, mapWidth, mapHeight);
            Vector2 normalPos = transformGridToCoordinatesWithoutAdjustment(gridPos.x, gridPos.y, mapWidth, mapHeight);

            AntWorker antWorker = new AntWorker(normalPos.x, normalPos.y, 64, 32, this);
            handler.addEntity(antWorker);
        }

        // select ant
        if (Gdx.input.isButtonJustPressed(0)) {
            Vector2 vec2 = transformTiledMapCoordinatesLeftToTop(camera, mapWidth, mapHeight);
            Vector2 gridPos = transformCoordinatesToGrid(vec2.x, vec2.y, mapWidth, mapHeight);
            if(gridPos == null)
                return;
            Vector2 normalPos = transformGridToCoordinates(gridPos.x, gridPos.y, mapWidth, mapHeight);

            handler.entities.forEach(e -> {
                if(e instanceof AntAbstract) {
                    if(normalPos.x == ((AntAbstract) e).getX() && normalPos.y == ((AntAbstract) e).getY()) {
                        System.out.println("found ant");
                        antAbstract = (AntAbstract) e;
                    }
                }
            });
        }

        // target select
        if (Gdx.input.isButtonJustPressed(1)) {
            Vector2 targetPosition = transformTiledMapCoordinatesLeftToTop(camera, mapWidth, mapHeight);
            antAbstract.addTask(new WalkToTask(targetPosition.x, targetPosition.y, antAbstract, 2f));
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public Graph getGraph() {
        return graph;
    }

    public Handler getHandler() {
        return handler;
    }
}
