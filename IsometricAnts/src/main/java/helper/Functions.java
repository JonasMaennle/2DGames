package helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import objects.GameEntity;
import objects.ants.AntAbstract;
import pathfinding.Graph;
import pathfinding.Node;

import java.util.List;

import static helper.Const.TILE_HEIGHT;
import static helper.Const.TILE_WIDTH;

/**
 * contains an collection of useful methods and stuff
 */
public class Functions {

    /**
     * get the current angle between object and target position in degree
     * @param x The x coordinate in pixels
     * @param y The y coordinate in pixels
     * @param destX The target x position
     * @param destY  The target y position */
    public static float getAngle(float x, float y, float destX, float destY){
        float angle = (float) Math.toDegrees(Math.atan2(destY - (y), destX - (x)));
        if(angle < 0)
            angle += 360;
        // System.out.println("Angle: " + angle);
        return angle;
    }



    /**
     * find out if two GameEntity Objects are in range (x -and y axis)
     * @param entity1 first entity to check
     * @param entity2 second entity toi check
     * @param range
     */
    /*
    public static boolean isEntityInRange(AbstractEntity entity1, AbstractEntity entity2, int range) {
        if((entity1.getX() < entity2.getX() + range && entity1.getX() > entity2.getX() - range)
                && (entity1.getY() < entity2.getY() + range
                && entity1.getY() > entity2.getY() - range)){
            return true;
        }
        return false;
    }
     */

    /**
     * get Node from coordionates -> f.e. ant coordinates
     * @param x
     * @param y
     * @param mapWidth
     * @param mapHeight
     * @param graph
     * @return
     */
    public static Node getNodeFromPosition(float x, float y, int mapWidth, int mapHeight, Graph graph) {
        Vector2 normalize = transformCoordinatesToIso(new Vector2(x,y), mapWidth, mapHeight);
        Vector2 posGrid = transformCoordinatesToGrid(normalize.x, normalize.y, mapWidth, mapHeight);
        return graph.getNode((int)posGrid.x, (int)posGrid.y);
    }

    /**
     * transform grid coordinates [2][5] into game coordinates (234,754) -> returns center of quader
     * @param x
     * @param y
     * @param mapWidth
     * @param mapHeight
     * @return
     */
    public static Vector2 transformGridToCoordinates(float x, float y, int mapWidth, int mapHeight){
        float newX = (mapWidth / 2 + (x * TILE_WIDTH / 2)) - ((mapHeight + (y * TILE_HEIGHT)) - mapWidth / 2);
        float newY = (((mapHeight / 2) - y * TILE_HEIGHT) * 2) - ((newX / 2)- (y * TILE_HEIGHT));

        newX -= TILE_WIDTH / 2;
        newY -= TILE_HEIGHT /2;

        return new Vector2(newX, newY);
    }

    /**
     * transform grid coordinates [2][5] into game coordinates (234,754) -> returns left bottom corner
     * @param x
     * @param y
     * @param mapWidth
     * @param mapHeight
     * @return
     */
    public static Vector2 transformGridToCoordinatesWithoutAdjustment(float x, float y, int mapWidth, int mapHeight){
        float newX = (mapWidth / 2 + (x * TILE_WIDTH / 2)) - ((mapHeight + (y * TILE_HEIGHT)) - mapWidth / 2);
        float newY = (((mapHeight / 2) - y * TILE_HEIGHT) * 2) - ((newX / 2)- (y * TILE_HEIGHT));

        return new Vector2(newX, newY);
    }


    /**
     * transform game coordinates (234,754) into grid coordinates [2][5]
     * @param x
     * @param y
     * @param mapWidth
     * @param mapHeight
     * @return
     */
    public static Vector2 transformCoordinatesToGrid(float x, float y, int mapWidth, int mapHeight) {
        // adjust position
        x += (TILE_WIDTH / 2);
        y += (TILE_HEIGHT / 2);

        // check if values are on the map
        if(x < 0 || y < 0 || x > mapWidth || y > mapHeight)
            return null;

        int xN = (int) (x / TILE_WIDTH);
        int yN = (int) (y / TILE_HEIGHT);

        return new Vector2(xN, yN);
    }

    /**
     * turns mouse position into isometric coordinates where 0,0 is top of map
     * @param camera needs Orthographic camera (libgdx)
     * @param mapWidth (TileWidth * TileCountWidth) example: 64 * 50
     * @param mapHeight (TileHeight * TileCountHeight) example: 32 * 50
     * @return
     */
    public static Vector2 transformTiledMapCoordinatesLeftToTop(Camera camera, int mapWidth, int mapHeight) {
        // get map coords form camera/mouse position
        Vector3 vec3 = transformMouseToWorldCoordinates(camera);
        float newX = (vec3.x - (mapWidth / 2)) - ((vec3.y - (mapHeight / 2)) * 2);
        float newY = (((mapHeight / 2) - vec3.y) * 2) - (newX / 2);
        return new Vector2(newX, newY);
    }

    /**
     * turns position into isometric coordinates where 0,0 is top of map
     * @param vector start position
     * @param mapWidth (TileWidth * TileCountWidth) example: 64 * 50
     * @param mapHeight (TileHeight * TileCountHeight) example: 32 * 50
     * @return
     */
    public static Vector2 transformCoordinatesToIso(Vector2 vector, int mapWidth, int mapHeight) {
        float newX = (vector.x - (mapWidth / 2)) - ((vector.y - (mapHeight / 2)) * 2);
        float newY = (((mapHeight / 2) - vector.y) * 2) - (newX / 2) - TILE_HEIGHT;
        return new Vector2(newX, newY);
    }

    /**
     * transform Mouse input Coordinates to actual map coordinates (y is correct!)
     * @param camera gamescreen camera object
     * @return vector3 (use only x and y)
     */
    public static Vector3 transformMouseToWorldCoordinates(Camera camera) {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }


    /**
     * transform tiledMap object to grid position
     * @param x
     * @param y
     * @param height number of tiles for y-direction
     * @return
     */
    public static Vector2 transformTiledMapObjectToGrid(float x, float y, int height) {
        float xT = x / (TILE_WIDTH / 2);
        float yT = height - (y / TILE_HEIGHT) - 1;
        return new Vector2(xT, yT);
    }

    /**
     *  check if target if available -> not blocked by ant or ant pathtarget
     * @param targetNodes
     * @param targetNode
     * @param entries
     * @param mapWidth
     * @param mapHeight
     * @return
     */
    public static boolean testIfTargetNodeAvailable(List<Node> targetNodes, Node targetNode, List<GameEntity> entries, int mapWidth, int mapHeight) {
        // is blocked by ant?
        Vector2 targetPos = transformGridToCoordinates(targetNode.getX(), targetNode.getY(), mapWidth, mapHeight);
        for(GameEntity e : entries) {
            if(e instanceof AntAbstract) {
                if(targetPos.x == ((AntAbstract) e).getX() && targetPos.y == ((AntAbstract) e).getY()) {
                    return false;
                }
            }
        }
        // is blocked as target node path
        return !targetNodes.contains(targetNode);
    }

    /**
     * calculate velocity (as vector) based on two given points
     * @param startXY start point
     * @param targetXY target location
     * @return Vector with .x as velX and .y as velY
     */
    public static Vector2 calculateDirectionVelocity(Vector2 startXY, Vector2 targetXY) {
        float totalAllowedMovement = 1.0f;
        float xDistanceFromTarget = Math.abs(targetXY.x - startXY.x);
        float yDistanceFromTarget = Math.abs(targetXY.y - startXY.y);
        float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
        float xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;

        float velX = xPercentOfMovement;
        float velY = totalAllowedMovement - xPercentOfMovement;
        if(targetXY.y < startXY.y)
            velY *= -1;

        if(targetXY.x < startXY.x)
            velX *= -1;

        return new Vector2(velX, velY);
    }
}
