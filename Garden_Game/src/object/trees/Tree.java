package object.trees;

import framework.core.pathfinding.Graph;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import java.util.Vector;

import static framework.helper.Collection.convertObjectCoordinatesToIsometricGrid;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public abstract class Tree {

    protected Image image;
    protected int x, y, width, height;
    protected Graph graph;

    public Tree(Image image, int x, int y, int width, int height, Graph graph){
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.graph = graph;
        removeTrunkFromGrid();
    }

    protected abstract void removeTrunkFromGrid();

    public abstract Rectangle getBounds();

    public void draw(){
        drawQuadImage(image, x, y, width, height);
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
}
