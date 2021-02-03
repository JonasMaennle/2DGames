package objects.ants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import core.GameScreen;
import helper.BodyHelper;
import objects.GameEntity;
import objects.ants.task.Task;
import objects.ants.task.WalkToTask;
import pathfinding.Node;

import java.util.LinkedList;

import static helper.Const.*;
import static helper.Functions.*;

public abstract class AntAbstract implements GameEntity {

    protected float x, y;
    protected int width, height;
    protected Body body;
    protected GameScreen gameScreen;
    protected LinkedList<Task> tasks;

    public AntAbstract(float x, float y, int width, int height, GameScreen gameScreen) {
        this.width = width;
        this.height = height;
        this.gameScreen = gameScreen;
        this.tasks = new LinkedList<>();
        this.body = BodyHelper.createCircularBody(x, y, 8, false, 0, true, gameScreen.getWorld());
        this.x = body.getPosition().x * PPM - (TILE_WIDTH / 2);
        this.y = body.getPosition().y * PPM - (TILE_HEIGHT / 2);
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM - (TILE_WIDTH / 2);
        y = body.getPosition().y * PPM - (TILE_HEIGHT / 2);
        body.setLinearVelocity(0,0);

        if(tasks.size() > 0) {
            tasks.getFirst().executeTask();
            if(tasks.getFirst().isDone()) tasks.removeFirst();
        }

        // move to center of tile if ant got lost
        if(tasks.size() == 0) {
            Node currentTile = getNodeFromPosition(x, y, gameScreen.getMapWidth(), gameScreen.getMapHeight(), gameScreen.getGraph());
            if(currentTile != null) {
                Vector2 correctPos = transformGridToCoordinates(currentTile.getX(), currentTile.getY(), gameScreen.getMapWidth(), gameScreen.getMapHeight());
                if(correctPos.x != x && correctPos.y != y) {
                    body.setTransform((correctPos.x + (TILE_WIDTH / 2)) / PPM, (correctPos.y + (TILE_HEIGHT / 2)) / PPM, 0);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if(tasks.size() > 0) tasks.getFirst().renderTask(batch);
    }

    public void addTask(Task task) {
        if(tasks.size() > 0 && tasks.getFirst() instanceof WalkToTask) {
            // remove target from handler
            WalkToTask currentActive = (WalkToTask) tasks.getFirst();
            Node targetNode = currentActive.getFinalTargetNode();
            gameScreen.getHandler().targetNodeList.remove(targetNode);

            tasks.clear();
        }
        this.tasks.add(task);
    }

    public void clearTasks() {
        this.tasks.clear();
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public GameScreen getGameScreen() { return gameScreen; }

    public Body getBody() { return body; }

    public Task getCurrentTask() { return (tasks.size() == 0) ? null : tasks.getFirst(); }

    public void setX(float x) { this.x = x; }

    public void setY(float y) { this.y = y; }
}
