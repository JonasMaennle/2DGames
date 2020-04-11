package object.player;

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
}
