package object.buildings;

import framework.helper.Graphics;
import org.newdawn.slick.Image;

import java.awt.*;

import static framework.helper.Collection.WOOD_NUMBER;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class Stock extends Building{

    private Image image, wood_image;

    public Stock(int x, int y){
        this.image = quickLoaderImage("buildings/stock");
        this.x = x;
        this.y = y;
        this.width = 256;
        this.height = 128;
        this.wood_image = quickLoaderImage("trees/log");
    }

    public void update() {
        int countLog = WOOD_NUMBER;
    }

    public void draw(){
        drawQuadImage(image, x, y, width, height);

        for(int i = 0; i < WOOD_NUMBER; i++){
            // drawQuadImage(wood_image, x + (i * 10) + 10, y + (i * 5) + 25, 48, 48);
            int xoffset = ((i % 13) * 10) + 55;
            int t = i / 13;
            int yoffset = (i * 5) - (t * 75);

            if(i < 39){
                drawQuadImage(wood_image, x + xoffset, y + yoffset, 48, 48);
            }else{
                drawQuadImage(wood_image, x + xoffset - 45, y + yoffset + 55, 48, 48);
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 64, y + 128, 64, 64 );
    }
}
