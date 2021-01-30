package object.player.playertask;

import object.farming.Field;
import object.player.Player;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import static framework.helper.Graphics.*;

public class HoeField extends PlayerTask {

    private Field field;
    private Animation hoe_swing;
    private Image player_shadow, player_selected;
    private long timer;
    private boolean trigger;

    public HoeField(Player player, Field field) {
        super(player);
        this.field = field;
        this.hoe_swing = new Animation(loadSpriteSheet("player/hoe_swing", 128, 128), 100);
        this.player_shadow = quickLoaderImage("player/player_shadow");
        this.player_selected = quickLoaderImage("player/player_selected");
    }

    @Override
    public void performTask() {
        player.setShowIdle(false);
        player.setMovingDirection("none");
        if(!trigger){
            trigger = true;
            timer = System.currentTimeMillis();
        }

        if(System.currentTimeMillis() - timer > 5000){
            taskDone = true;
        }
    }

    @Override
    public void renderTask() {
        if(player.getHandler().getSelectedPlayer() != null && player.getHandler().getSelectedPlayer().equals(player)){
            drawQuadImage(player_selected, player.getX(), player.getY() + player.getHeight() - 20, player.getWidth() + 8, 18);
        }else{
            drawQuadImage(player_shadow, player.getX(), player.getY() + player.getHeight() - 20, player.getWidth() + 8, 18);
        }
        drawAnimation(hoe_swing, player.getX() - 16, player.getY() - 8, player.getWidth() * 2, player.getHeight());
    }
}
