package objects.resource;

public class StoneResource extends Resource {

    public StoneResource(float x, float y) {
        super(x, y);
        this.maxAmount = 100;
        this.currentAmount = maxAmount;
    }
}
