package object.farming;

import org.newdawn.slick.Animation;

import static framework.helper.Graphics.loadSpriteSheet;
import static framework.helper.Graphics.quickLoaderImage;

public class CarottField extends Field {

    public CarottField(int x, int y) {
        super(x, y);
        this.animation = new Animation(loadSpriteSheet("vegetables/Carotts", 64, 64), 1000);
        this.seededImage = quickLoaderImage("vegetables/Carotts_0");
    }
}
