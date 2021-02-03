package objects.resource;

import core.GameScreen;

public abstract class Resource {

    protected float x, y;
    protected int maxAmount, currentAmount;
    protected GameScreen gameScreen;

    public Resource(float x, float y, GameScreen gameScreen) {
        this.x = x;
        this.y = y;
        this.gameScreen = gameScreen;

        this.maxAmount = 100;
        this.currentAmount = maxAmount;
    }

    public abstract void collected();

    public float getX() { return x; }

    public float getY() { return y; }

    public int getMaxAmount() { return maxAmount; }

    public int getCurrentAmount() { return currentAmount; }

    public void setCurrentAmount(int currentAmount) { this.currentAmount = currentAmount; }
}
