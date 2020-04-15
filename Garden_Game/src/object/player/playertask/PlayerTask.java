package object.player.playertask;

import object.player.Player;

public abstract class PlayerTask {

    protected boolean taskDone;

    protected Player player;

    public PlayerTask(Player player){
        this.player = player;
    }

    public abstract void performTask();

    public abstract void renderTask();

    public Player getPlayer() {
        return player;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }
}
