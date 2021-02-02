package objects.ants.task;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import objects.ants.AntAbstract;
import pathfinding.Node;
import pathfinding.Pathfinder;

import java.util.LinkedList;

import static helper.Const.*;
import static helper.Const.PPM;
import static helper.Functions.testNextNodeAvailable;
import static helper.Functions.transformGridToCoordinates;

public class WalkToTask extends Task {

    private Texture texture;
    private LinkedList<Node> path;
    private float x, y, velX, velY, speed;
    private float xTarget, yTarget;

    public WalkToTask(float xTarget, float yTarget, AntAbstract antAbstract, float speed) {
        super(antAbstract);
        this.xTarget = xTarget;
        this.yTarget = yTarget;
        this.texture = new Texture("testing/debug.png");
        this.speed = speed;
        this.x = owner.getX();
        this.y = owner.getY();
        new Pathfinder(owner.getGameScreen().getGraph(), owner.getGameScreen(), this).start();
    }

    @Override
    public void executeTask() {
        x = (int)owner.getX();
        y = (int)owner.getY();
        owner.setX(x);
        owner.setY(y);
        velX = 0;
        velY = 0;

        pathFinding(path);
        if(done) return;

        owner.getBody().setLinearVelocity(velX * speed, velY * speed);
    }

    @Override
    public void renderTask(SpriteBatch batch) {
        // draw path
        if(path != null) {
            path.forEach(element -> {
                Vector2 position = transformGridToCoordinates(element.getX(), element.getY(), owner.getGameScreen().getMapWidth(), owner.getGameScreen().getMapHeight());
                batch.draw(texture, position.x, position.y, TILE_WIDTH, TILE_HEIGHT);
            });
        }
    }

    private void pathFinding(LinkedList<Node> path) {
        if(path == null || path.size() == 0) return;

        Node target = path.get(path.size() - 1);

        // check if node is blocked
        boolean result = testNextNodeAvailable(owner, target, owner.getGameScreen().getHandler().entities, owner.getGameScreen().getMapWidth(), owner.getGameScreen().getMapHeight());
        if(!result) {
            // get alternative path
            new Pathfinder(owner.getGameScreen().getGraph(), owner.getGameScreen(), this, target).start();
            return;
        }


        Vector2 targetPosition = transformGridToCoordinates(target.getX(), target.getY(), owner.getGameScreen().getMapWidth(), owner.getGameScreen().getMapHeight());

        if(x < targetPosition.x) velX = 1;
        if(x > targetPosition.x) velX = -1;
        if(y < targetPosition.y) velY = 0.5f;
        if(y > targetPosition.y) velY = -0.5f;

        // smooth out
        if(x <= targetPosition.x + (speed / 2) && x >= targetPosition.x - (speed / 2))
            velX = 0;

        // speed up diagonal movement
        if(velX == 0 && velY != 0)
            velY *=2;

        if(x >= targetPosition.x - (speed / 2) && x <= targetPosition.x + (speed / 2)  && y <= targetPosition.y + (speed / 2) && y >= targetPosition.y - (speed / 2)) {
            // set pixel perfect
            if(path.size() == 1) {
                owner.getBody().setTransform((targetPosition.x + (TILE_WIDTH / 2)) / PPM, (targetPosition.y + (TILE_HEIGHT / 2)) / PPM, 0);
                done = true;
                owner.getBody().setLinearVelocity(0, 0);

                // remove target node from list
                owner.getGameScreen().getHandler().targetNodeList.remove(target);
            }
            path.removeLast();
        }
    }

    public LinkedList<Node> getPath() { return path; }

    public void setPath(LinkedList<Node> path) { this.path = path; }

    public float getxTarget() {
        return xTarget;
    }

    public float getyTarget() {
        return yTarget;
    }
}
