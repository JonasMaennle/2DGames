package object.enemy;

import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.entity.EnemyType;
import framework.shader.Light;
import object.Particle;

import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

public class Enemy_Digger extends Enemy_Basic{
	
	private Image ground;
	private CopyOnWriteArrayList<Particle> particleList;
	private Random rand;
	private long timer1, timer2;
	private float tmpHeight;
	private boolean digged, initHP;
	
	private int eyeX, eyeY;
	private Light eyeLightLeft, eyeLightRight;

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
		this.hpFactor = 14;
		
		if(StateManager.gameMode == GameState.GAME)
			playerRange = 64;
		else
			playerRange = 1000;
		
		eyeLightLeft = new Light(new Vector2f(x,y), 156, 198, 217, 200);
		eyeLightRight = new Light(new Vector2f(x,y), 156, 198, 217, 200);

		
		this.type = EnemyType.DIGGER;
	}
	
	public void update() {
		
		if(!digged) {
			
			super.update();
		}
		
		if(tmpHeight > 10) {
			
			// calc eyelight position
			if(direction.equals("left")){
				if(moveLeft.getFrame() == 0){
					eyeX = (int) (x + MOVEMENT_X + 2);
					eyeY = (int) (y + MOVEMENT_Y + 10);
				}else{
					eyeX = (int) (x + MOVEMENT_X + 2);
					eyeY = (int) (y + MOVEMENT_Y + 14);
				}
				eyeLightLeft.setLocation(new Vector2f(eyeX, eyeY));
				eyeLightRight.setLocation(new Vector2f(eyeX + 10, eyeY));
			}else{
				if(moveRight.getFrame() == 0){
					eyeX = (int) (x + MOVEMENT_X + 30);
					eyeY = (int) (y + MOVEMENT_Y + 10);
				}else{
					eyeX = (int) (x + MOVEMENT_X + 30);
					eyeY = (int) (y + MOVEMENT_Y + 14);
				}	
				eyeLightLeft.setLocation(new Vector2f(eyeX, eyeY));
				eyeLightRight.setLocation(new Vector2f(eyeX - 10, eyeY));
			}
		}

		isPlayerInRange(playerRange);
		
		if(speed != 0) {
			timer1 = System.currentTimeMillis();
			
			if(digged){
				if(timer1 - timer2 > 25) {
					particleList.add(new Particle((int)(x + width/2), (int)(y + tmpHeight) - 8, 8, 8, (rand.nextFloat() - 0.5f) * 2,  -rand.nextInt(2) - 1, 3, "ground"));
					timer2 = timer1;
				}
				
				if(handler.getPlayer().getX() < x + width/2) {
					this.direction = "left";
				}else {
					this.direction = "right";
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
					this.hp *= hpFactor;
					
					lights.add(eyeLightLeft);
					lights.add(eyeLightRight);
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
	public void isPlayerInRange(int borderOffset){
		if(x > handler.getPlayer().getX() - borderOffset && x < handler.getPlayer().getX() + borderOffset){
			if(y > handler.getPlayer().getY() - borderOffset && y < handler.getPlayer().getY() + borderOffset){
				speed = 2;
			}
		}else
			return;
	}
	
	@Override
	public void die(){
		lights.remove(eyeLightLeft);
		lights.remove(eyeLightRight);
	}
	
	@Override
	public Vector2f[] getVertices() {
		
		if(direction.equals("left")){
			
			if(moveLeft.getFrame() == 0) {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 0),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 7, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 7, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 20),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 20),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 0)
				};
			}else {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 7, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 20),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 4)
				};
			}
		}else{
			if(moveRight.getFrame() == 0) {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 0),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 20),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 20),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 24, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 24, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 0)
				};
			}else {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 20),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 24, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 24, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 4)
				};
			}
		}
	}
}
