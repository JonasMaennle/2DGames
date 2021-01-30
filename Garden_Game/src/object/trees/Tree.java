package object.trees;

import framework.core.pathfinding.Graph;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import java.awt.*;
import java.util.Vector;

import static framework.helper.Collection.convertObjectCoordinatesToIsometricGrid;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public abstract class Tree {

    protected Image image, image_transparent;
    protected int x, y, width, height;
    protected Graph graph;
    protected Vector2f root;
    protected int woodLeft;
    protected boolean blocked, sapling;
    protected long plantingTimeStamp;
    protected float totalGrowTime;

    public Tree(Image image, Image image_transparent, int x, int y, int width, int height, Graph graph, boolean sapling){
        this.image = image;
        this.image_transparent = image_transparent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.graph = graph;
        this.blocked = false;
        this.sapling = sapling;
        this.totalGrowTime = 100000;
        this.plantingTimeStamp = System.currentTimeMillis() - (long)(totalGrowTime * 0.2f);
    }

    // trunk bounds
    public abstract Rectangle getBounds();

    public abstract Rectangle getTranparencyBounds();

    public abstract Vector2f getRoot();

    public void draw(){
        basicDraw(image);
    }

    public void drawTransparent(){
        basicDraw(image_transparent);
    }

    private void basicDraw(Image image){
        if(sapling){
            float delta = System.currentTimeMillis() - plantingTimeStamp;
            float percent = delta / totalGrowTime;
            if(percent >= 1)
                sapling = false;
            drawQuadImage(image, x + ((1 - percent) * (width/2)), y - (height * percent) + height, width * percent, height * percent);
        }else{
            drawQuadImage(image, x, y, width, height);
        }
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

    public int getWoodLeft() { return woodLeft; }

    public void setWoodLeft(int woodLeft) { this.woodLeft = woodLeft; }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isSapling() {
        return sapling;
    }

    public void setSapling(boolean sapling) {
        this.sapling = sapling;
    }
}
