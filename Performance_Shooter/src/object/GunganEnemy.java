package object;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import static helpers.Artist.*;

import java.awt.Rectangle;

import data.Handler;
import data.Tile;

public class GunganEnemy extends Enemy{
	
	private Image healthBackground, healthBorder, healthForeground;
	private Handler handler;
	private Player player;
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
		this.tX = x + (width/2);
		this.tY = y + 55;
		this.angle = 0;
		this.handler = handler;
		this.player = handler.player;
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.speed = 3;
		this.health = width - 8;
		this.anim_walkRight = new Animation(loadSpriteSheet("enemy/enemy_right_sheet", TILE_SIZE, TILE_SIZE * 2), 50);
		this.anim_walkLeft = new Animation(loadSpriteSheet("enemy/enemy_left_sheet", TILE_SIZE, TILE_SIZE * 2), 50);
		try {
			this.lasterShot = new Sound("sound/rebel_laser.wav");
		} catch (SlickException e) {e.printStackTrace();}
	}
	
	public GunganEnemy(float x, float y, int width, int height, Handler handler, int range) 
	{
		super(x, y, width, height);
		this.healthBackground = quickLoaderImage("enemy/healthBackground");
		this.healthBorder = quickLoaderImage("enemy/healthBorder");
		this.healthForeground = quickLoaderImage("enemy/healthForeground");
		this.velX = 1;
		this.angle = 0;
		this.tX = x + (width/2);
		this.tY = y + 55;
		this.handler = handler;
		this.player = handler.player;
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.speed = 3;
		this.health = width - 8;
		this.rangeLeft = (int)x - (range * 64);
		this.rangeRight = (int)x + (range * 64);
		this.anim_walkRight = new Animation(loadSpriteSheet("enemy/enemy_right_sheet", TILE_SIZE, TILE_SIZE * 2), 500);
		this.anim_walkLeft = new Animation(loadSpriteSheet("enemy/enemy_left_sheet", TILE_SIZE, TILE_SIZE * 2), 500);
		
		try {
			this.lasterShot = new Sound("sound/rebel_laser.wav");
		} catch (SlickException e) {e.printStackTrace();}
	}
	
	public void update()
	{
		if(rangeLeft != 0)
		{
			if(rangeRight > x && velX > 0)
			{
				velX = 1;
			}else if(rangeRight < x)
			{
				velX = -1;
			}
			
			if(rangeLeft < x && velX < 0)
			{
				velX = -1;
			}else if(rangeLeft > x)
			{
				velX = 1;
			}
		}
		x += velX * speed;
		
		// calc current angle
		calcAngle(player.getX() + (player.getWidth()/2), player.getY() + (player.getHeight()/4));
		// check if testShoot() is possible
		if(x - player.getX() < 1000 || x - player.getX() > 0)
			testShoot();

		
		updateBounds();
		mapCollision();

		
		// update laserList
		for(Laser l : laserList)
		{
			l.update();
			
			if(checkCollision(player.getX() + 20, player.getY(), player.getWidth()-40, player.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
			{
				laserList.remove(l);
				// deal damage to player
				player.damage(20);
			}
			for(Tile tile : handler.obstacleList)
			{
				if(checkCollision(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					laserList.remove(l);
				}
				if(l.isOutOfMap())
				{
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
			
		}else{
			//drawQuadImage(image_left, x, y, width, height);
			drawAnimation(anim_walkLeft, x, y, TILE_SIZE, TILE_SIZE * 2);
		}
		
		// draw health bar
		drawQuadImage(healthBackground, x + 4, y - 20, width - 8, 6);
		drawQuadImage(healthForeground, x + 4, y - 20, health, 6);
		drawQuadImage(healthBorder, x + 4, y - 20, width - 8, 6);

		//drawQuad((float)testShot.getX(), (float)testShot.getY(), (float)testShot.getWidth(), (float)testShot.getHeight());
		
		//drawBounds();
	}
	
	@Override
	public void damage(int amount)
	{
		if(health > 0)
		{
			health -= amount;
			if(health <= 0)
			{
				handler.gunganList.remove(this); 
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
				velX = 1;
				x = (float) ((float) r.getX() + r.getWidth() + 1);
			}
			if(r.intersects(rectRight))
			{
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
	    
		//System.out.println("Angle: " + angle);
	}
	
	private void testShoot()
	{
		float totalAllowedMovement = 1.0f;
		float xDistanceFromTarget = Math.abs(destX - x);
		float yDistanceFromTarget = Math.abs(destY-30 - y);
		float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
		float xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;
		
		tVelX = xPercentOfMovement;
		tVelY = totalAllowedMovement - xPercentOfMovement;
		
		// set direction based on position of target relative to tower
		if(destY-30 < tY)
			tVelY *= -1;
		if(destX < tX)
			tVelX *= -1;	
		// move test bullet
		tX += tVelX * 50;
		tY += tVelY * 50;
		testShot.setLocation((int)tX, (int)tY);
		
		// check map collisions
		for(Tile t : handler.obstacleList)
		{
			if(checkCollision((float)testShot.getX(), (float)testShot.getY(), (float)testShot.getWidth(), (float)testShot.getHeight(), t.getX(), t.getY(), t.getWidth(), t.getHeight()))
			{
				testShot.setLocation((int)x, (int)y);
				tX = x + (width / 2);
				tY = y + 55;
			}
		}
		// check player collision
		if(checkCollision((float)testShot.getX(), (float)testShot.getY(), (float)testShot.getWidth(), (float)testShot.getHeight(), player.getX(), player.getY(), player.getWidth(), player.getHeight()))
		{
			testShot.setLocation((int)x + (width / 2), (int)y + 55);
			tX = x + (width / 2);
			tY = y + 55;
			
			// check if gungan is facing player
			if(velX > 0 && player.getX() > x)
				shoot();
			if(velX < 0 && player.getX() < x)
				shoot();
		}
	}
	
	private void shoot()
	{
		// shoot every 500 ms
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 500)
		{
			timer2 = timer1;
			//System.out.println("x:" + x + " y: " + y + "  destX: " + destX + " destY: " +destY);
			laserList.add(new Laser(x + (width/2), y + 55, destX, destY, 25, 4, 15, "green", -angle));
			lasterShot.play();
		}
	}
}
