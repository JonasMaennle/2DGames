package objects.ants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import core.GameScreen;
import helper.BodyHelper;
import objects.GameEntity;
import objects.ants.task.Task;
import static helper.Const.*;

public abstract class AntAbstract implements GameEntity {

    protected float x, y;
    protected int width, height;
    protected Body body;
    protected GameScreen gameScreen;
    protected Task task;

    public AntAbstract(float x, float y, int width, int height, GameScreen gameScreen) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gameScreen = gameScreen;
        this.body = BodyHelper.createCircularBody(x, y, 8, false, 0, gameScreen.getWorld());
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM - (TILE_WIDTH / 2);
        y = body.getPosition().y * PPM - (TILE_HEIGHT / 2);
        body.setLinearVelocity(0,0);

        if(task != null) {
            task.executeTask();
            if(task.isDone()) task = null;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if(task != null) task.renderTask(batch);
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public GameScreen getGameScreen() { return gameScreen; }

    public Body getBody() { return body; }

    public Task getTask() { return task; }

    public void setTask(Task task) { this.task = task; }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
