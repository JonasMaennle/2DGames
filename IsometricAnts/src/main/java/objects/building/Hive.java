package objects.building;

import core.GameScreen;

public class Hive extends Building {

    public Hive(float x, float y, GameScreen gameScreen) {
        super(x, y, gameScreen);
    }

    @Override
    public void selected() {
        System.out.println("hey wazzup");
    }
}
