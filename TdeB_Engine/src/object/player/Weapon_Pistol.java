package object.player;

import org.newdawn.slick.Image;
import static framework.helper.Graphics.*;
import framework.core.Handler;

public class Weapon_Pistol extends Weapon_Basic{

	private Image weaponRight, weaponLeft;
	
	public Weapon_Pistol(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);

		this.weaponRight = quickLoaderImage("player/weapon_pistol_right");
		this.weaponLeft = quickLoaderImage("player/weapon_pistol_left");
	}
	
	public void update(){
		super.update();
	}
	
	public void draw(){

		if(player.getDirection().equals("right")){
			drawQuadImageRotLeft(weaponRight, x, y, width, height, angle);
		}else{
			drawQuadImageRotRight(weaponLeft, x, y, width, height, angle - 180);
		}
		
		for(Bullet_Basic bullet : bulletList){
			bullet.draw();
		}
	}
	
	public void shoot(){
		bulletDamage = 15;
		bulletSpeed = 12;
		super.shoot();
	}
}
