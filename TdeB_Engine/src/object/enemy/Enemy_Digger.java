package object.enemy;

import framework.core.Handler;
import object.Particle;

import static framework.helper.Graphics.*;

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

	public Enemy_Digger(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		this.particleList = new CopyOnWriteArrayList<Particle>();
		this.rand = new Random();
		this.ground = quickLoaderImage("enemy/ground");
		
		this.moveLeft = new Animation(loadSpriteSheet("enemy/Enemy_Digger_left", 32, 32), 200);
		this.moveRight = new Animation(loadSpriteSheet("enemy/Enemy_Digger_right", 32, 32), 200);
		this.hp = 10000;
		this.tmpHeight = 0;
	}
	
	public void update() {
		isPlayerInRange();
		if(speed != 0) {
			timer1 = System.currentTimeMillis();
			if(timer1 - timer2 > 100) {
				particleList.add(new Particle((int)(x + width/2), (int)(y + height/2), 8, 8, rand.nextFloat() - 0.5f,  -rand.nextInt(2) - 1, 3, "ground"));
				timer2 = timer1;
			}
			
			for(Particle p : particleList) {
				p.update();
				p.setVelY(p.getVelY()+0.15f);
				if(System.currentTimeMillis() - p.getTimeCreated() > 350) {
					particleList.remove(p);
				}
			}
			
		}
	}
	
	public void draw() {


		
		if(speed != 0) {
			drawAnimation(moveLeft, x, y, width, height);
			tmpHeight += 0.25f;
		}
		
		drawQuadImage(ground, x, y + tmpHeight, width, height - tmpHeight);
		
		
		for(Particle p : particleList) {
			p.draw();
		}
	}
	
	@Override
	public void isPlayerInRange(){
		if(x > handler.getPlayer().getX() - 250 && x < handler.getPlayer().getX() + 250){
			if(y > handler.getPlayer().getY() - 250 && y < handler.getPlayer().getY() + 250){
				speed = 2;
			}
		}else
			return;
	}
}
