package object.trees;

import framework.core.pathfinding.Graph;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static framework.helper.Collection.SCALE;
import static framework.helper.Graphics.*;

public class Tree_01 extends Tree {
    public Tree_01(int x, int y, Graph graph) {
        super(quickLoaderImage("trees/tree_01"),quickLoaderImage("trees/tree_01_transparent"), x, y, 128, 128, graph);
        this.root = new Vector2f(x + 48, y + 128);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) ((x + 50) * SCALE), (int) ((y + 63) * SCALE), (int) (20 * SCALE), (int) (63 * SCALE));
    }

    @Override
    public Rectangle getTranparencyBounds() {
        return new Rectangle(x, y, width, 64);
    }

    @Override
    public Vector2f getRoot() {
        return root;
    }
}
