package objects.ants.task;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import objects.ants.AntAbstract;
import objects.building.Hive;
import objects.resource.Resource;
import pathfinding.Node;

import static helper.Functions.transformCoordinatesToIso;
import static helper.Functions.transformGridToCoordinates;

public class HarvestResource extends Task {

    private Resource resource;
    private WalkToTask walkToTask;
    private long timer;
    private boolean atHome;
    private int harvestDuration;

    public HarvestResource(Resource resource, AntAbstract antAbstract) {
        super(antAbstract);
        this.resource = resource;
        this.walkToTask = new WalkToTask(resource.getX(), resource.getY(), getOwner(), 2f, true);
        this.atHome = false;
        this.harvestDuration = 2000; // time in ms
    }

    @Override
    public void executeTask() {
        // walk to resource
        if(walkToTask != null && !walkToTask.isDone()) {
            walkToTask.executeTask();
            if(walkToTask.isDone()) {
                walkToTask = null;
                timer = System.currentTimeMillis();
            }
            return;
        }

        // harvest stuff
        if(walkToTask == null && !atHome) {
            // work for 1 second
            if(System.currentTimeMillis() - timer > harvestDuration) {
                timer = System.currentTimeMillis();
                // walk back
                walkToHive();
                atHome = true;
                return;
            }
        }

        // walk back to resource
        if(walkToTask == null && atHome) {
            walkToResource();
            atHome = false;
        }
    }

    private void walkToResource() {
        walkToTask = new WalkToTask(resource.getX(), resource.getY(), getOwner(), 2f, true);
    }

    private void walkToHive() {
        Hive hive = getOwner().getGameScreen().getHandler().getHive();
        Node homeNode = hive.getHomeNode();

        Vector2 target = transformGridToCoordinates(homeNode.getX(), homeNode.getY(), getOwner().getGameScreen().getMapWidth(), getOwner().getGameScreen().getMapHeight());
        Vector2 isoCoords = transformCoordinatesToIso(target, getOwner().getGameScreen().getMapWidth(), getOwner().getGameScreen().getMapHeight());

        walkToTask = new WalkToTask(isoCoords.x, isoCoords.y, getOwner(), 2f, true);
    }

    @Override
    public void renderTask(SpriteBatch batch) {
        // if(walkToTask != null) walkToTask.renderTask(batch);
    }
}
