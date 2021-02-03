package objects.resource;

public abstract class Resource {

    protected float x, y;
    protected int maxAmount, currentAmount;

    public Resource(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public int getMaxAmount() { return maxAmount; }

    public int getCurrentAmount() { return currentAmount; }

    public void setCurrentAmount(int currentAmount) { this.currentAmount = currentAmount; }
}
