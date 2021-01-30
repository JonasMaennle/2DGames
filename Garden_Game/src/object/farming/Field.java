package object.farming;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import java.awt.*;

import static framework.helper.Graphics.drawAnimation;
import static framework.helper.Graphics.drawQuadImage;

public abstract class Field {

    protected int x, y, width, height;
    protected Animation animation;
    protected Image seededImage;
    protected boolean growing;

    public Field(int x, int y){
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;
    }

    public void update(){

    }

    public void draw(){
        if(growing){
            if(animation != null){
                animation.stopAt(animation.getFrameCount() - 1);
                drawAnimation(animation, x, y, width, height);
            }
        }else{
            drawQuadImage(seededImage,x , y, width, height);
        }
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height/2);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isGrowing() {
        return growing;
    }

    public void setGrowing(boolean growing) {
        this.growing = growing;
    }
}
