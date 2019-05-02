package object.player;

import framework.core.Handler;

public class Weapon_Pistol extends Weapon_Basic{

	public Weapon_Pistol(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);

	}
	
	public void update(){
		super.update();
	}
	
	public void draw(){
		super.draw();
	}
	
	public void shoot(){
		bulletDamage = 8;
		bulletSpeed = 12;
		super.shoot();
	}
}
