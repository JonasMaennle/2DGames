package objects.ants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import core.GameScreen;

public class AntWorker extends AntAbstract {

    private Texture placeholder;

    public AntWorker(float x, float y, int width, int height, GameScreen gameScreen) {
        super(x, y, width, height, gameScreen);
        this.placeholder = new Texture("ant/ant_.png");
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(placeholder,x + (width / 4),y + (height / 4),width ,height);
    }
}
