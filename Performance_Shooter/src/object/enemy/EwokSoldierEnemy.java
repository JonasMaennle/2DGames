package object.enemy;

import static helpers.Graphics.*;
import static helpers.Setup.TILE_SIZE;

import java.awt.Rectangle;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import Enity.TileType;
import data.Handler;
import object.Tile;

public class EwokSoldierEnemy extends Enemy{
	
	private final float MAX_SPEED = 5;
	private Animation walk_left, walk_right;
	private Image idle_left, idle_right, spear;
	private String direction;
	private Handler handler;
	private float spearAngle, spearX, velXSpear, spearY, velYSpear;
	private int frameCount;
	private boolean jumping, dying;
	private Sound sound, attack_sound;
	private Random rand;

	public EwokSoldierEnemy(float x, float y, int width, int height, Handler handler) 
	{
		super(x, y, width, height);
		this.handler = handler;
		this.rand = new Random();
		this.gravity = 4;
		this.speed = 2 + rand.nextFloat();
		this.velX = 0;
		this.spearAngle = 320;
		this.jumping = false;
		this.dying = false;
		this.frameCount = 100;
		this.velXSpear = -1;
		this.velYSpear = -1;	
		this.health = width -8;
		
		try {
			this.sound = new Sound("sound/Ewok/ewok_sound_" + rand.nextInt(4) + ".wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		this.healthBackground = quickLoaderImage("enemy/healthBackground");
		this.healthBorder = quickLoaderImage("enemy/healthBorder");
		this.healthForeground = quickLoaderImage("enemy/healthForeground");
		
		this.walk_left = new Animation(loadSpriteSheet("enemy/ewok_walk_left", 48, 80), 50);
		this.walk_right = new Animation(loadSpriteSheet("enemy/ewok_walk_right", 48, 80), 50);
		
		this.idle_left = quickLoaderImage("enemy/ewok_idle_left");
		this.idle_right = quickLoaderImage("enemy/ewok_idle_right");	
		this.spear = quickLoaderImage("enemy/ewok_spear");
		
		this.direction = "left";
		
		try {
			this.attack_sound = new Sound("sound/spear_sound.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		velY = gravity;

		jump();
		if(!dying)moveToEntity();
		
		x += velX * speed;
		y += velY * speed;

		updateBounds();
		mapCollision();
	}
	public void draw()
	{
		// draw spear
		if(direction.equals("left"))
		{
			drawQuadImageRot(spear, x + spearX + 10, y - 40 + spearY, spear.getWidth(), spear.getHeight(), spearAngle);
		}else{
			drawQuadImageRot(spear, x + spearX + 20, y - 40 - spearY, spear.getWidth(), spear.getHeight(), spearAngle);
		}
		
		// draw ewok
		switch ((int)velX) {
		case -1:
			drawAnimation(walk_left, x, y, width, height);
			break;
		case 1:
			drawAnimation(walk_right, x, y, width, height);
			break;
		case 0:
			if(direction.equals("left"))
				drawQuadImage(idle_left, x, y, 64, 128);
			else
				drawQuadImage(idle_right, x, y, 64, 128);
			break;
		default:
			break;
		}
		
		// draw health bar
		drawQuadImage(healthBackground, x + 4, y - 5, width - 8, 6);
		drawQuadImage(healthForeground, x + 4, y - 5, health, 6);
		drawQuadImage(healthBorder, x + 4, y - 5, width - 8, 6);
	}
	
	private void moveToEntity()
	{
		// y area
		if(y - handler.getCurrentEntity().getY() > - 300 && y - handler.getCurrentEntity().getY() < 300)
		{
			// Move  x to entity if in range (700px)
			if(x > handler.getCurrentEntity().getX() && x - handler.getCurrentEntity().getX() < 700) // entity left
			{
				if(!sound.playing())
					sound.play();
				if(getBounds().intersects(handler.getCurrentEntity().getBounds()))
				{
					velX = 0;
					attack();
				}else{
					velX = -1;
					spearAngle = 320;
					direction = "left";
				}
			}
			
			if(x < handler.getCurrentEntity().getX() && x - handler.getCurrentEntity().getX() > - 700) // entity right
			{
				if(!sound.playing())
					sound.play();
				if(getBounds().intersects(handler.getCurrentEntity().getBounds()))
				{
					velX = 0;
					attack();
				}else{
					velX = 1;
					spearAngle = 40;
					direction = "right";
				}
			}
		}

	}
	
	private void attack()
	{
		if(direction.equals("left"))
		{
			if(spearX < -5)
			{
				velXSpear = 1;
			}
			if(spearX > 5)
			{
				velXSpear = -1;
			}
			if(spearY < -5)
			{
				velYSpear = 1;
			}
			if(spearY > 5)
			{
				velYSpear = -1;
			}
			
			if(spearX == -6)
				attack_sound.play();
				
			spearX += velXSpear;
			spearY += velYSpear;

		}else{
			if(spearX > 5)
			{
				velXSpear = -1;
			}
			if(spearX < -5)
			{
				velXSpear = 1;
			}
			if(spearY > 5)
			{
				velYSpear = -1;
			}
			if(spearY < -5)
			{
				velYSpear = 1;
			}
			
			if(spearX == 6)
				attack_sound.play();
				
			spearX += velXSpear;
			spearY += velYSpear;
		}
		
		handler.getCurrentEntity().damage(0.25f);
	}
	
	private void jump()
	{
		if(jumping)
		{
			if(jumping && frameCount >= 0)
			{
				velY -= frameCount * 0.1;
				frameCount -= 4;
				if(velY > MAX_SPEED)
				{
					velY = MAX_SPEED;
				}
			}
		}else{
			frameCount = 100;
		}
	}
	
	private void mapCollision()
	{
		for(Tile t : handler.obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), TILE_SIZE, TILE_SIZE);

			if(r.intersects(getBounds()))
			{
				if(t.getType() == TileType.Lava_Light || t.getType() == TileType.Lava)
				{
					damage(1);
					y -= velY * speed * 0.9f;
					velX = 0;
					dying = true;
					return;
				}
			}
			
			if(r.intersects(rectLeft))
			{
				jumping = true;
				x = (float) ((float) r.getX() + r.getWidth() + 1);
				return;
			}
			if(r.intersects(rectRight))
			{
				jumping = true;
				x = (float) (r.getX() - width - 1);
				return;
			}
			if(r.intersects(rectBottom))
			{
				jumping = false;
				velY = 0;
				y = (float) (r.getY() - height);
			}
		}
	}
	
	public void damage(float amount)
	{
		if(health > 0)
		{
			health -= amount;
			if(health <= 0)
			{
				sound.stop();
				handler.enemyList.remove(this); 
			}
		}
	}
}
