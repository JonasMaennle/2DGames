package object.player;

import org.newdawn.slick.Image;

import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class CarryableWood implements Carryable {

    private Image wood;
    private Player player;

    public CarryableWood(Player player){
        this.player = player;
        this.wood = quickLoaderImage("trees/log");
    }

    @Override
    public void draw() {
        drawQuadImage(wood, player.getX() - 20, player.getY() + 12, 58, 37);

    }
}
