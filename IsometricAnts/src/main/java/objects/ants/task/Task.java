package objects.ants.task;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import objects.ants.AntAbstract;

public abstract class Task {

    protected boolean done;
    protected AntAbstract owner;

    public Task(AntAbstract antAbstract) {
        this.owner = antAbstract;
        this.done = false;
    }

    public abstract void executeTask();

    public abstract void renderTask(SpriteBatch batch);

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

    public AntAbstract getOwner() { return owner; }
}
