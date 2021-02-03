package objects.building;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import core.GameScreen;
import pathfinding.Node;

import static helper.BodyHelper.createIsometricBody;
import static helper.Const.*;
import static helper.Const.PPM;
import static helper.Functions.*;

public abstract class Building {

    protected float x, y;
    protected int width, height;
    protected GameScreen gameScreen;
    protected Node homeNode;
    protected Body body;

    public Building(float x, float y, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.body = createIsometricBody(x, y, TILE_WIDTH, TILE_HEIGHT, true, 1000, false, gameScreen.getWorld());

        // set home node
        Vector2 vec = transformCoordinatesToIso(new Vector2(x,y), gameScreen.getMapWidth(), gameScreen.getMapHeight());
        Vector2 gridPosition = transformCoordinatesToGrid(vec.x, vec.y + TILE_HEIGHT, gameScreen.getMapWidth(), gameScreen.getMapHeight());
        homeNode = gameScreen.getGraph().getNode((int)gridPosition.x, (int)gridPosition.y);

        this.x = body.getPosition().x * PPM - (TILE_WIDTH / 2);
        this.y = body.getPosition().y * PPM - (TILE_HEIGHT / 2);
    }

    public abstract void selected();

    public abstract void update();

    public abstract void render(SpriteBatch batch);

    public Node getHomeNode() {
        return homeNode;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
