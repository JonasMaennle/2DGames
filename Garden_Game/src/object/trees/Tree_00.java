package object.trees;

import framework.core.pathfinding.Graph;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static framework.helper.Collection.SCALE;
import static framework.helper.Graphics.*;

public class Tree_00 extends Tree {

    public Tree_00(int x, int y, Graph graph) {
        super(quickLoaderImage("trees/tree_00"),quickLoaderImage("trees/tree_00_transparent"), x, y, 192, 192, graph);
        this.root = new Vector2f(x + 96, y + 192);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) ((x + 95) * SCALE), (int) ((y + 60) * SCALE), (int) (15 * SCALE), (int) (130 * SCALE));
    }

    @Override
    public Rectangle getTranparencyBounds() {
        return new Rectangle(x, y, width, 140);
    }

    @Override
    public Vector2f getRoot() {
        return this.root;
    }
}
