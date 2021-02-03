package objects.resource;

import core.GameScreen;

public class WoodResource extends Resource {

    public WoodResource(float x, float y, GameScreen gameScreen) {
        super(x, y, gameScreen);
    }

    @Override
    public void collected() {
        gameScreen.setWoodCount(gameScreen.getWoodCount() + 1);
    }
}
