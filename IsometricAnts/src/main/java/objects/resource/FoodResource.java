package objects.resource;

import core.GameScreen;

public class FoodResource extends Resource {

    public FoodResource(float x, float y, GameScreen gameScreen) {
        super(x, y, gameScreen);
    }

    @Override
    public void collected() {
        gameScreen.setFoodCount(gameScreen.getFoodCount() + 1);
    }
}
