package objects.resource;

import core.GameScreen;

public class StoneResource extends Resource {

    public StoneResource(float x, float y, GameScreen gameScreen) {
        super(x, y, gameScreen);
    }

    @Override
    public void collected() {
        gameScreen.setStoneCount(gameScreen.getStoneCount() + 1);
    }
}
