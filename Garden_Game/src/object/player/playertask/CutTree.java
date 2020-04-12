package object.player.playertask;

import object.player.Player;
import object.trees.Tree;

public class CutTree extends PlayerTask {

    private Tree tree;

    public CutTree(Player player, Tree tree) {
        super(player);
        this.tree = tree;
    }

    @Override
    public void performTask() {

    }

    @Override
    public void renderTask() {

    }
}
