package core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import objects.GameEntity;
import pathfinding.Node;

import java.util.concurrent.CopyOnWriteArrayList;

public class Handler {

    public CopyOnWriteArrayList<GameEntity> entities;
    public CopyOnWriteArrayList<Node> targetNodeList;

    public Handler() {
        this.entities = new CopyOnWriteArrayList<>();
        this.targetNodeList = new CopyOnWriteArrayList<>();
    }

    public void update() {
        entities.forEach(e -> e.update());
    }

    public void render(SpriteBatch batch) {
        entities.forEach(e -> e.render(batch));
    }

    public void addEntity(GameEntity entity) {
        this.entities.add(entity);
    }
}
