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
            walkToStock = new WalkTo(building.x, building.y, player);
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

        // kill task
        if(WOOD_NUMBER >= 78){
            player.setCarryable(null);
            player.removeActiveTask();
        }

        if(tree.getWoodLeft() == 0) tree = findNextTreeToCut(handler);

        // reset walkToTree / cutTree
        if(tree != null){
            walkToTree = new WalkTo(tree.getRoot().x, tree.getRoot().y, player);
            cutTree = new CutTree(player, tree);
        }else{
            // kill task
            player.setCarryable(null);
            player.removeActiveTask();
        }
    }

    private Tree findNextTreeToCut(Handler handler){
        Tree closestTree = null;
        float totalDistanceFromTarget = 10000;

        for(Tree tree : handler.getTreeList()){
            float xDistanceFromTarget = Math.abs(tree.getRoot().x - (player.getX() + player.getWidth() / 2));
            float yDistanceFromTarget = Math.abs(tree.getRoot().y - (player.getY() + player.getHeight()));
            if(totalDistanceFromTarget > (xDistanceFromTarget + yDistanceFromTarget) && !tree.isBlocked() && !tree.isSapling()){
                totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
                closestTree = tree;
            }
        }
        if(closestTree != null)
            closestTree.setBlocked(true);
        return closestTree;
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

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }
}
