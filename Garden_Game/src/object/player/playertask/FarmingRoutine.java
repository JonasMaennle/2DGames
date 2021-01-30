package object.player.playertask;

import framework.core.Handler;
import object.farming.Field;
import object.player.Player;

public class FarmingRoutine extends PlayerTask {

    private WalkTo walkToField;
    private Handler handler;
    private PlayerTask currentTask;
    private Field field;
    private HoeField hoeField;

    public FarmingRoutine(Player player, Handler handler, WalkTo walkToField, Field field) {
        super(player);
        this.handler = handler;
        this.walkToField = walkToField;
        this.field = field;
        this.hoeField = new HoeField(player, field);
    }

    @Override
    public void performTask() {
        currentTask = null;

        // walk to field
        if(!walkToField.isTaskDone()){
            walkToField.performTask();
            currentTask = walkToField;
            return;
        }
        // hoe field
        if(!hoeField.isTaskDone()){
            hoeField.performTask();
            currentTask = hoeField;
            return;
        }
    }

    @Override
    public void renderTask() {
        if(currentTask != null)
            currentTask.renderTask();
    }
}
