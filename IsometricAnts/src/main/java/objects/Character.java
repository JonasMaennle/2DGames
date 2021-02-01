package objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import core.GameScreen;
import helper.BodyHelper;
import pathfinding.Node;

import java.util.LinkedList;

import static helper.Const.*;
import static helper.Functions.transformGridToCoordinates;


public class Character {

    private float x, y, velX, velY, speed;
    private Body body;
    private GameScreen gameScreen;
    private LinkedList<Node> path;

    public Character(float x, float y, GameScreen gameScreen) {
        this.x = x;
        this.y = y;
        this.gameScreen = gameScreen;
        // this.body = BodyHelper.createIsometricBox(x, y, 64, 32, false, 0, gameScreen.getWorld());
        this.body = BodyHelper.createCircularBody(x, y, 8, false, 0, gameScreen.getWorld());
        this.speed = 2;
    }

    public void setPath(LinkedList<Node> path) {
        this.path = path;
    }

    public void update() {
        x = body.getPosition().x * PPM - (TILE_WIDTH / 2);
        y = body.getPosition().y * PPM - (TILE_HEIGHT / 2);
        x = (int)x;
        y = (int)y;
        velX = 0;
        velY = 0;

        if(path != null && path.size() > 0) {
            Node target = path.get(path.size() - 1);
            Vector2 targetPosition = transformGridToCoordinates(target.getX(), target.getY(), gameScreen.getMapWidth(), gameScreen.getMapHeight());

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
                    body.setTransform((targetPosition.x + (TILE_WIDTH / 2)) / PPM, (targetPosition.y + (TILE_HEIGHT / 2)) / PPM, 0);
                    path.removeLast();
                    return;
                }
                path.removeLast();
            }
        }
        body.setLinearVelocity(velX * speed, velY * speed);
    }
}
