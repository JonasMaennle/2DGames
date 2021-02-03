package helper;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import core.GameScreen;
import objects.building.Hive;
import objects.resource.FoodResource;
import objects.resource.StoneResource;
import objects.resource.WoodResource;
import org.lwjgl.Sys;
import pathfinding.Graph;
import pathfinding.Node;

import static helper.BodyHelper.createIsometricBody;
import static helper.Const.TILE_HEIGHT;
import static helper.Const.TILE_WIDTH;
import static helper.Functions.*;

public class TiledMapUtils {

    private GameScreen gameScreen;
    private Graph graph;
    private TiledMap tiledMap;

    public TiledMapUtils(GameScreen gameScreen, Graph graph) {
        this.gameScreen = gameScreen;
        this.graph = graph;
    }

    public IsometricTiledMapRenderer setUpTiledMap(String map) {
        tiledMap = new TmxMapLoader().load(map);
        IsometricTiledMapRenderer isometricTiledMapRenderer = new IsometricTiledMapRenderer(tiledMap);

        // add nodes
        MapLayers mapLayers = tiledMap.getLayers();
        int width = ((TiledMapTileLayer) mapLayers.get("layer1")).getWidth();
        int height = ((TiledMapTileLayer) mapLayers.get("layer1")).getHeight();

        gameScreen.setMapWidth(width * TILE_WIDTH);
        gameScreen.setMapHeight(height * TILE_HEIGHT);

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) mapLayers.get("layer1")).getCell(x, y);

                // no node for water tiles
                if(cell != null && cell.getTile().getId() != 11 && cell.getTile().getId() != 12) {
                    int ty = height - y - 1;
                    graph.addNode(new Node(x, ty));
                }

                // resource tiles //
                int ty = height - y - 1;
                Vector2 vec = transformGridToCoordinates(x,ty,gameScreen.getMapWidth(), gameScreen.getMapHeight());
                Vector2 isoCoords = transformCoordinatesToIso(vec, gameScreen.getMapWidth(), gameScreen.getMapHeight());

                // stone
                if(cell != null && cell.getTile().getId() == 13) gameScreen.getHandler().resources.add(new StoneResource(isoCoords.x, isoCoords.y, gameScreen));
                // wood
                if(cell != null && cell.getTile().getId() == 14) gameScreen.getHandler().resources.add(new WoodResource(isoCoords.x, isoCoords.y, gameScreen));
                // food
                if(cell != null && cell.getTile().getId() == 15) gameScreen.getHandler().resources.add(new FoodResource(isoCoords.x, isoCoords.y, gameScreen));
            }
        }

        loadMapObjects(tiledMap.getLayers().get("objects").getObjects(), height);

        graph.createMatrix();
        return isometricTiledMapRenderer;
    }

    private void loadMapObjects(MapObjects objects, int height) {
        for(MapObject object : objects) {
            if(object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                if(object.getName().equals("hive")) {
                    Vector2 gridPosition = transformTiledMapObjectToGrid(rectangle.getX(), rectangle.getY(), height);
                    Vector2 normalPos = transformGridToCoordinatesWithoutAdjustment(gridPosition.x, gridPosition.y, gameScreen.getMapWidth(), gameScreen.getMapHeight());
                    gameScreen.getHandler().buildings.add(new Hive(normalPos.x, normalPos.y, gameScreen));
                }
            }
        }
    }
}
