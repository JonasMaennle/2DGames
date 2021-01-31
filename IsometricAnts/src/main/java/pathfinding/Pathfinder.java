package pathfinding;

import com.badlogic.gdx.math.Vector2;
import core.GameScreen;
import pathfinding.Graph;

import java.util.LinkedList;

import static helper.Functions.transformCoordinatesToGrid;


public class Pathfinder {

    private Graph graph;
    private GameScreen gameScreen;

    public Pathfinder(Graph graph, GameScreen gameScreen) {
        this.graph = graph;
        this.gameScreen = gameScreen;
    }

    public LinkedList<Node> getPath(float xStart, float yStart, float xTarget, float yTarget) {
        try {
            Vector2 start =  transformCoordinatesToGrid(xStart, yStart, gameScreen.getMapWidth(), gameScreen.getMapHeight());
            Vector2 end = transformCoordinatesToGrid(xTarget, yTarget, gameScreen.getMapWidth(), gameScreen.getMapHeight());
            if(start == null || end == null) {
                return null;
            }


            int enemyX = (int) start.x;
            int enemyY = (int) start.y;
            int playerX = (int) end.x;
            int playerY = (int) end.y;

            // System.out.println(enemyX + "  " + enemyY + "    :     " + playerX + "  " + playerY);

            if(graph.getNodeID(enemyX, enemyY) != -1 && graph.getNodeID(playerX, playerY) != -1) {
                LinkedList<Node> tempPath = graph.astar(graph.getNodeID(enemyX, enemyY), graph.getNodeID(playerX, playerY));
                //tempPath.remove(tempPath.size() - 1);
                return tempPath;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
