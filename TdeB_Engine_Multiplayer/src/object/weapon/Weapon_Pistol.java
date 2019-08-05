package object.weapon;

import org.newdawn.slick.Image;
import static framework.helper.Graphics.*;
import framework.core.Handler;
import object.player.Player;

public class Weapon_Pistol extends Weapon_Basic{

	private transient Image weaponRight, weaponLeft;
	
	public Weapon_Pistol(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);

		this.weaponRight = quickLoaderImage("player/weapon_pistol_right");
		this.weaponLeft = quickLoaderImage("player/weapon_pistol_left");
		this.bulletDamage = 15;
		this.bulletSpeed = 12;
		this.bulletSpeedMAX = 12;
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
		super.shoot();
	}
}
