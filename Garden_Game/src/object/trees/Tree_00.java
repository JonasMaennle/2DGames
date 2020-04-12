package object.trees;

import framework.core.pathfinding.Graph;
import org.lwjgl.util.Rectangle;

import static framework.helper.Collection.SCALE;
import static framework.helper.Graphics.*;

public class Tree_00 extends Tree {

    public Tree_00(int x, int y, Graph graph) {
        super(quickLoaderImage("trees/tree_00"), x, y, 192, 192, graph);
    }

    @Override
    protected void removeTrunkFromGrid() {

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) ((x + 95) * SCALE), (int) ((y + 60) * SCALE), (int) (15 * SCALE), (int) (130 * SCALE));
    }
}
