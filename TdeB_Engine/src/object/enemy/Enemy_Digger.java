package object.enemy;

import framework.core.Handler;
import object.Particle;

import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

public class Enemy_Digger extends Enemy_Basic{
	
	private Image ground;
	private CopyOnWriteArrayList<Particle> particleList;
	private Random rand;
	private long timer1, timer2;
	private float tmpHeight;
	private boolean digged, initHP;

	public Enemy_Digger(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		this.particleList = new CopyOnWriteArrayList<Particle>();
		this.rand = new Random();
		this.ground = quickLoaderImage("enemy/ground");
		this.digged = true;
		this.initHP = true;
		
		this.moveLeft = new Animation(loadSpriteSheet("enemy/Enemy_Digger_left", 32, 32), 200);
		this.moveRight = new Animation(loadSpriteSheet("enemy/Enemy_Digger_right", 32, 32), 200);
		this.hp = 1000;
		this.tmpHeight = 0;
	}
	
	public void update() {
		
		if(!digged)
			super.update();
		
		isPlayerInRange();
		if(speed != 0) {
			timer1 = System.currentTimeMillis();
			
			if(digged){
				if(timer1 - timer2 > 100) {
					particleList.add(new Particle((int)(x + width/2), (int)(y + height/2), 8, 8, rand.nextFloat() - 0.5f,  -rand.nextInt(2) - 1, 3, "ground"));
					timer2 = timer1;
				}
			}

			
			for(Particle p : particleList) {
				p.update();
				p.setVelY(p.getVelY()+0.15f);
				if(System.currentTimeMillis() - p.getTimeCreated() > 350) {
					particleList.remove(p);
				}
			}
			
			if(tmpHeight < 32){
				tmpHeight += 0.2f;
			}else{
				digged = false;
				// Set hp
				if(initHP){
					initHP = false;
					this.hp = 32;
					this.hpFactor = 12;
					this.hp *= hpFactor;
					shadowObstacleList.add(this);
				}
			}				
		}
	}
	
	public void draw() {
		
		if(speed != 0) {
			if(direction.equals("right")){
				drawAnimation(moveRight, x, y, width, height);
			}else{
				drawAnimation(moveLeft, x, y, width, height);
			}		
		}
		
		if(digged)
			drawQuadImage(ground, x, y + tmpHeight, width, height - tmpHeight);	
		
		for(Particle p : particleList) {
			p.draw();
		}
		
		// hp bar
		if(!digged)
			drawQuadImage(hpBar, x, y - 6, hp / hpFactor, 4);
	}
	
	@Override
	public void isPlayerInRange(){
		if(x > handler.getPlayer().getX() - 64 && x < handler.getPlayer().getX() + 64){
			if(y > handler.getPlayer().getY() - 64 && y < handler.getPlayer().getY() + 64){
				speed = 2;
			}
		}else
			return;
	}
}
