package object.player.playertask;

import framework.core.Handler;
import object.buildings.Building;
import object.buildings.Stock;
import object.player.CarryableWood;
import object.player.Player;
import object.trees.Tree;
import org.lwjgl.util.vector.Vector2f;

import static framework.helper.Collection.WOOD_NUMBER;

public class MakeWoodRoutine extends PlayerTask {

    private CutTree cutTree;
    private WalkTo walkToTree;
    private Handler handler;
    private PlayerTask currentTask;
    private WalkTo walkToStock;
    private Tree tree;

    public MakeWoodRoutine(Handler handler, Player player, CutTree cutTree, WalkTo walkTo, Tree tree) {
        super(player);
        this.cutTree = cutTree;
        this.walkToTree = walkTo;
        this.handler = handler;
        this.tree = tree;
    }

    @Override
    public void performTask() {
        // walk to tree
        if(!walkToTree.isTaskDone()){
            walkToTree.performTask();
            currentTask = walkToTree;
            return;
        }
        // make wood
        if(walkToTree.isTaskDone() && !cutTree.isTaskDone()){
            cutTree.performTask();
            currentTask = cutTree;
            Vector2f building = findStockBuilding(handler);
            walkToStock = new WalkTo(building.x, building.y, player, false);
            return;
        }
        // walk to stock
        if(cutTree.isTaskDone() && !walkToStock.isTaskDone()){
            walkToStock.performTask();
            currentTask = walkToStock;
            player.setCarryable(new CarryableWood(player));
            return;
        }

        if(walkToStock.isTaskDone() && WOOD_NUMBER < 78){
            WOOD_NUMBER++;
            player.setCarryable(null);
        }

        if(WOOD_NUMBER >= 78){
            player.setCarryable(null);
            player.removeActiveTask();
        }

        // reset walkToTree / cutTree
        if(tree != null){
            walkToTree = new WalkTo(tree.getRoot().x, tree.getRoot().y, player, false);
            cutTree = new CutTree(player, tree);
        }
    }

    private Vector2f findStockBuilding(Handler handler){
        for(Building building : handler.getBuildingList()){
            if(building instanceof Stock){
                return new Vector2f(building.getX() + building.getWidth()/2, building.getY() + building.getHeight()/2);
            }
        }
        return null;
    }

    @Override
    public void renderTask() {
        if(currentTask != null)
            currentTask.renderTask();
    }
}
