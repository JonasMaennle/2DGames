package object.enemy;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import java.awt.Rectangle;

import data.Handler;
import object.Tile;
import object.weapon.Laser;
import object.weapon.TestShot;

public class GunganEnemy extends Enemy{
	
	private Handler handler;
	private Sound lasterShot;
	private long timer1, timer2;
	private float speed;
	private int rangeLeft, rangeRight;

	public GunganEnemy(float x, float y, int width, int height, Handler handler) 
	{
		super(x, y, width, height);
		this.healthBackground = quickLoaderImage("enemy/healthBackground");
		this.healthBorder = quickLoaderImage("enemy/healthBorder");
		this.healthForeground = quickLoaderImage("enemy/healthForeground");
		this.velX = 1;
		if(rand.nextBoolean())this.velX = -1;
		
		this.angle = 0;
		this.handler = handler;
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.gravity = 4;
		this.speed = rand.nextInt(3)+1;
		this.health = width - 8;
		
		this.idleLeft = quickLoaderImage("enemy/enemy_left");
		this.idleRight = quickLoaderImage("enemy/enemy_right");
		
		this.anim_walkRight = new Animation(loadSpriteSheet("enemy/enemy_right_sheet", TILE_SIZE, TILE_SIZE * 2), 50);
		this.anim_walkLeft = new Animation(loadSpriteSheet("enemy/enemy_left_sheet", TILE_SIZE, TILE_SIZE * 2), 50);
		try {
			this.lasterShot = new Sound("sound/rebel_laser.wav");
		} catch (SlickException e) {e.printStackTrace();}
		
		this.direction = "right";
		if(velX < 0)
			direction = "left";
	}
	
	public GunganEnemy(float x, float y, int width, int height, Handler handler, int range) 
	{
		super(x, y, width, height);
		this.healthBackground = quickLoaderImage("enemy/healthBackground");
		this.healthBorder = quickLoaderImage("enemy/healthBorder");
		this.healthForeground = quickLoaderImage("enemy/healthForeground");
		this.velX = 1;
		if(rand.nextBoolean())this.velX = -1;
		this.angle = 0;

		this.handler = handler;
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.gravity = 4;
		this.speed = rand.nextInt(3)+1;
		this.health = width - 8;
		this.rangeLeft = (int)x - (range * 64);
		this.rangeRight = (int)x + (range * 64);
		
		this.idleLeft = quickLoaderImage("enemy/enemy_left");
		this.idleRight = quickLoaderImage("enemy/enemy_right");
		
		this.anim_walkRight = new Animation(loadSpriteSheet("enemy/enemy_right_sheet", TILE_SIZE, TILE_SIZE * 2), 500);
		this.anim_walkLeft = new Animation(loadSpriteSheet("enemy/enemy_left_sheet", TILE_SIZE, TILE_SIZE * 2), 500);
		
		try {
			this.lasterShot = new Sound("sound/rebel_laser.wav");
		} catch (SlickException e) {e.printStackTrace();}
		
		this.direction = "right";
		if(velX < 0)
			direction = "left";
	}
	
	public void update()
	{
		if(rangeLeft != 0)
		{
			if(rangeRight > x && velX > 0)
			{
				direction = "right";
				velX = 1;
			}else if(rangeRight < x)
			{
				direction = "left";
				velX = -1;
			}
			
			if(rangeLeft < x && velX < 0)
			{
				direction = "left";
				velX = -1;
			}else if(rangeLeft > x)
			{
				direction = "right";
				velX = 1;
			}
		}
		
		// calc current angle
		entity = handler.getCurrentEntity();
		calcAngle(entity.getX() + (entity.getWidth()/2), entity.getY() + 5 + rand.nextInt(entity.getHeight()-10) ); //  rand.nextInt(entity.getHeight()-10)
		
		if(System.currentTimeMillis() - lastShot > 1500)
		{
			if(direction.equals("left"))
				velX = -1;
			if(direction.equals("right"))
				velX = 1;
		}
		
		x += velX * speed;
		y += gravity;
		
		updateBounds();
		mapCollision();

		
		// update laserList
		for(Laser l : laserList)
		{
			l.update();
			
			if(checkCollision(entity.getX() + 20, entity.getY(), entity.getWidth()-40, entity.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
			{
				l.removeLight();
				laserList.remove(l);
				// deal damage to current entity
				entity.damage(20);
			}
			for(Tile tile : handler.obstacleList)
			{
				if(checkCollision(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					l.removeLight();
					laserList.remove(l);
				}
				if(l.isOutOfMap())
				{
					l.removeLight();
					laserList.remove(l);
				}
			}
		}
	}
	
	public void draw()
	{
		// draw laser
		for(Laser laser : laserList)
		{
			laser.draw();
		}
		
		// draw image
		if(velX > 0)
		{
			//drawQuadImage(image_right, x, y, width, height);
			drawAnimation(anim_walkRight, x, y, TILE_SIZE, TILE_SIZE * 2);
			
		}else if (velX < 0){
			//drawQuadImage(image_left, x, y, width, height);
			drawAnimation(anim_walkLeft, x, y, TILE_SIZE, TILE_SIZE * 2);
		}
		
		if(velX == 0 && direction.equals("right"))
		{
			drawQuadImage(idleRight, x, y, width, height);
		}
		if(velX == 0 && direction.equals("left"))
		{
			drawQuadImage(idleLeft, x, y, width, height);
		}
		
		// draw health bar
		drawQuadImage(healthBackground, x + 4, y - 20, width - 8, 6);
		drawQuadImage(healthForeground, x + 4, y - 20, health, 6);
		drawQuadImage(healthBorder, x + 4, y - 20, width - 8, 6);

		//if(testShot != null)testShot.draw();
		
		//drawBounds();
	}
	
	@Override
	public void damage(float amount)
	{
		if(health > 0)
		{
			health -= amount;
			if(health <= 0)
			{
				for(Laser l : laserList)
				{
					projectileList.add(l);
				}
				handler.enemyList.remove(this); 
			}
		}
	}
	
	private void mapCollision()
	{
		for(Tile t : handler.obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), TILE_SIZE, TILE_SIZE);

			if(r.intersects(rectLeft))
			{
				direction = "right";
				velX = 1;
				x = (float) ((float) r.getX() + r.getWidth() + 1);
			}
			if(r.intersects(rectRight))
			{
				direction = "left";
				velX = -1;
				x = (float) (r.getX() - width - 1);
			}
			if(r.intersects(rectBottom))
			{
				velY = 0;
				y = (float) (r.getY() - height);
			}
		}
	}
	
	// Calc Angle in degree between x,y and destX,destY <- nice
	private void calcAngle(float destX, float destY)
	{
		this.destX = destX;
		this.destY = destY;
		angle = -(float) Math.toDegrees(Math.atan2(destY - (y), destX - (x)));

	    if(angle < 0){
	        angle += 360;
	    }
		calcTestShot((int)destX, (int)destY);
		//System.out.println("Angle: " + angle);
	}
	
	private void calcTestShot(int destX, int destY)
	{
		if(testShot == null)
			testShot = new TestShot(x + (width/2), y + 55, destX, destY, 4, 4, 32);
		
		testShot.update();
		
		if(testShot.isOutOfMap())
			testShot = null;
		
		// check map collisions
		for(Tile t : handler.obstacleList)
		{
			if(testShot != null && checkCollision((float)testShot.getX(), (float)testShot.getY(), (float)testShot.getWidth(), (float)testShot.getHeight(), t.getX(), t.getY(), t.getWidth(), t.getHeight()))
			{
				testShot = null;
			}
		}
		// check player collision
		if(testShot != null && checkCollision((float)testShot.getX(), (float)testShot.getY(), (float)testShot.getWidth(), (float)testShot.getHeight(), entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()))
		{
			testShot = null;
			
			// check if gungan is facing player
			if(velX >= 0 && entity.getX() > x)
				shoot(destX, destY);
			if(velX <= 0 && entity.getX() < x)
				shoot(destX, destY);
		}
	}
	
	private void shoot(int destX, int destY)
	{
		velX = 0;
		lastShot = System.currentTimeMillis();
		
		// shoot every 500 ms
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 500)
		{
			timer2 = timer1;
			//System.out.println("x:" + x + " y: " + y + "  destX: " + destX + " destY: " +destY);
			laserList.add(new Laser(x + (width/2), y + 50, destX, destY, 4, 4, 10, "blue", -angle));
			lasterShot.play();
		}
	}

	@Override
	public float getVelX() {
		return velX;
	}

	@Override
	public float getVelY() {
		return velY;
	}
}
