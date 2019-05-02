package object.collectable;
import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import framework.shader.Light;
import object.player.Player;

public class Collectable_Helmet extends Collectable_Basic{

	private Player player;
	private Image imgLeft, imgRight;
	private Light light;
	private int offset;
	
	public Collectable_Helmet(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.imgRight = quickLoaderImage("player/player_lamp_right");
		this.imgLeft = quickLoaderImage("player/player_lamp_left");
		this.light = new Light(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), 50, 25, 5, 12);
		lights.add(light);
	}
	
	public void update(){
		
		if(found){
			x = (int) player.getX();
			y = (int) player.getY() - height + 4;	
		}else{
			light.setLocation(new Vector2f(x + MOVEMENT_X + width - 10, y + MOVEMENT_Y));
		}
	}
	
	public void draw(){
		
		if(found){
			if(player.getDirection().equals("right")){
				offset = player.getWalkRight().getFrame() * 4;
				light.setLocation(new Vector2f(x + width + MOVEMENT_X + offset - 10, y + height/2 + MOVEMENT_Y));
				drawQuadImage(imgRight, x + offset, y, width, height);
			}else{
				offset = player.getWalkLeft().getFrame() * 4;
				light.setLocation(new Vector2f(x + MOVEMENT_X + offset + 10, y + height/2 + MOVEMENT_Y));
				drawQuadImage(imgLeft, x + offset, y, width, height);
			}
		}else{
			drawQuadImage(imgRight, x, y - height + 4, width, height);
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void removeLight(){
		lights.remove(light);
	}
}
