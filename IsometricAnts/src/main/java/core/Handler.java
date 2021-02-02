package core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import objects.GameEntity;

import java.util.concurrent.CopyOnWriteArrayList;

public class Handler {

    public CopyOnWriteArrayList<GameEntity> entities;

    public Handler() {
        this.entities = new CopyOnWriteArrayList<>();
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
