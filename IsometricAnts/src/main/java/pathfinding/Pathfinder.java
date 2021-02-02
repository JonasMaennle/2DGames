package pathfinding;

import com.badlogic.gdx.math.Vector2;
import core.GameScreen;
import objects.ants.task.WalkToTask;
import java.util.LinkedList;
import static helper.Functions.*;

public class Pathfinder extends Thread {

    private Graph graph;
    private GameScreen gameScreen;
    private WalkToTask walkToTask;

    public Pathfinder(Graph graph, GameScreen gameScreen, WalkToTask walkToTask) {
        this.graph = graph;
        this.gameScreen = gameScreen;
        this.walkToTask = walkToTask;
    }

    @Override
    public void run() {
        Vector2 start = transformCoordinatesToIso(new Vector2(walkToTask.getOwner().getX(), walkToTask.getOwner().getY()), gameScreen.getMapWidth(), gameScreen.getMapHeight());
        walkToTask.setPath(getPath(start.x, start.y, walkToTask.getxTarget(), walkToTask.getyTarget()));
    }

    public LinkedList<Node> getPath(float xStart, float yStart, float xTarget, float yTarget) {
        try {
            Vector2 start =  transformCoordinatesToGrid(xStart, yStart, gameScreen.getMapWidth(), gameScreen.getMapHeight());
            Vector2 end = transformCoordinatesToGrid(xTarget, yTarget, gameScreen.getMapWidth(), gameScreen.getMapHeight());
            if(start == null || end == null) return null;

            int enemyX = (int) start.x;
            int enemyY = (int) start.y;
            int playerX = (int) end.x;
            int playerY = (int) end.y;

            if(graph.getNodeID(enemyX, enemyY) != -1 && graph.getNodeID(playerX, playerY) != -1) {
                return graph.astar(graph.getNodeID(enemyX, enemyY), graph.getNodeID(playerX, playerY));
                // tempPath.remove(tempPath.size() - 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
