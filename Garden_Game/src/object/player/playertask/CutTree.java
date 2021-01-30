package object.player.playertask;

import object.player.Player;
import object.trees.Tree;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import static framework.helper.Graphics.*;

public class CutTree extends PlayerTask {

    private Tree tree;
    private Animation axe_swing;
    private Image player_shadow, player_selected;
    private long timer;
    private boolean trigger;

    public CutTree(Player player, Tree tree) {
        super(player);
        this.tree = tree;
        this.axe_swing = new Animation(loadSpriteSheet("player/axe_swing", 128, 128), 100);
        this.player_shadow = quickLoaderImage("player/player_shadow");
        this.player_selected = quickLoaderImage("player/player_selected");
    }

    @Override
    public void performTask() {
        if(!trigger){
            trigger = true;
            timer = System.currentTimeMillis();
        }

        player.setX(tree.getRoot().x);
        player.setY(tree.getRoot().y - player.getHeight());

        player.setShowIdle(false);
        player.setMovingDirection("none");

        if(System.currentTimeMillis() - timer > 5000){
            // bring wood to stock
            tree.setWoodLeft(tree.getWoodLeft() - 1);
            taskDone = true;
        }
    }

    @Override
    public void renderTask() {
        if(player.getHandler().getSelectedPlayer() != null && player.getHandler().getSelectedPlayer().equals(player)){
            drawQuadImage(player_selected, player.getX() + 12 - player.getWidth(), player.getY() + player.getHeight() - 12, player.getWidth() + 8, 18);
        }else{
            drawQuadImage(player_shadow, player.getX() + 12 - player.getWidth(), player.getY() + player.getHeight() - 12, player.getWidth() + 8, 18);
        }
        drawAnimation(axe_swing, player.getX() - player.getWidth(), player.getY(), player.getWidth() * 2, player.getHeight());
    }
}
