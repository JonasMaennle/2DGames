package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameEntity {
    public void update();
    public void render(SpriteBatch batch);
}
