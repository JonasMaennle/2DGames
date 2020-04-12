package object.trees;

import framework.core.pathfinding.Graph;
import org.lwjgl.util.Rectangle;

import static framework.helper.Collection.SCALE;
import static framework.helper.Graphics.*;

public class Tree_01 extends Tree {
    public Tree_01(int x, int y, Graph graph) {
        super(quickLoaderImage("trees/tree_01"), x, y, 128, 128, graph);
    }

    @Override
    protected void removeTrunkFromGrid() {
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) ((x + 67) * SCALE), (int) ((y + 63) * SCALE), (int) (20 * SCALE), (int) (63 * SCALE));
    }
}
