package object;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import static helpers.Artist.*;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

import data.Handler;
import data.Tile;

public class GunganEnemy extends Enemy{
	
	private Image image_left, image_right, healthBackground, healthBorder, healthForeground;
	private Handler handler;
	private Player player;
	private Sound lasterShot;
	private Rectangle testShot;
	private CopyOnWriteArrayList<Laser> laserList;
	private long timer1, timer2;

	public GunganEnemy(float x, float y, int width, int height, Handler handler) 
	{
		super(x, y, width, height);
		this.image_left = quickLoaderImage("enemy/enemy_left");
		this.image_right = quickLoaderImage("enemy/enemy_right");
		this.healthBackground = quickLoaderImage("enemy/healthBackground");
		this.healthBorder = quickLoaderImage("enemy/healthBorder");
		this.healthForeground = quickLoaderImage("enemy/healthForeground");
		this.velX = 2;
		this.handler = handler;
		this.player = handler.player;
		this.testShot = new Rectangle((int)x, (int)y+52, 5, 5);
		this.laserList = new CopyOnWriteArrayList<>();
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.health = width - 8;
		
		try {
			this.lasterShot = new Sound("sound/rebel_laser.wav");
		} catch (SlickException e) {e.printStackTrace();}
	}
	
	public void update()
	{
		x += velX;
		
		// check if shoot() is possible
		if(player.getY() + player.getHeight() <= y + height && player.getY() + player.getHeight() >= y)
		{
			// check if player is left
			if(player.getX() < x - width && velX < 0)
			{
				testShoot();
			}
			// check if player is right
			if(player.getX() > x+width && velX > 0)
			{
				testShoot();
			}
		}
		
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
		for(Laser l : laserList)
		{
			l.draw();
		}
		
		if(velX > 0)
		{
			drawQuadImage(image_right, x, y, width, height);
		}else{
			drawQuadImage(image_left, x, y, width, height);
		}
		
		// draw health bar
		drawQuadImage(healthBackground, x + 4, y - 20, width - 8, 6);
		drawQuadImage(healthForeground, x + 4, y - 20, health, 6);
		drawQuadImage(healthBorder, x + 4, y - 20, width - 8, 6);
		// draw testShot
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
				velX *= -1;
				x = (float) ((float) r.getX() + r.getWidth());
			}
			if(r.intersects(rectRight))
			{
				velX *= -1;
				x = (float) (r.getX() - width);
			}
			if(r.intersects(rectBottom))
			{
				velY = 0;
				y = (float) (r.getY() - height);
			}
		}
	}
	
	private void testShoot()
	{
		// check if player is left
		if(player.getX() < x - width && velX < 0)
		{
			testShot.setLocation((int)testShot.getX()-50, (int) (y + 52));
			
			for(Tile t : handler.obstacleList)
			{
				if(checkCollision(t.getX(), t.getY(), t.getWidth(), t.getHeight(), (float)testShot.getX(), (float)testShot.getY(), (float)testShot.getWidth(), (float)testShot.getHeight()))
				{
					testShot.setBounds((int)x, (int)y + 52, 5, 5);
				}
				if(checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), (float)testShot.getX(), (float)testShot.getY(), (float)testShot.getWidth(), (float)testShot.getHeight()))
				{
					shoot();
					testShot.setBounds((int)x, (int)y + 52, 5, 5);
				}
			}
				if(testShot.getX() > x + (WIDTH/2) || testShot.getX() < x - (WIDTH / 2))
					testShot.setBounds((int)x, (int)y + 52, 5, 5);
		}
		
		// check if player is right
		if(player.getX() > x+width && velX > 0)
		{
			testShot.setLocation((int)testShot.getX()+50, (int) (y + 52));
			
			for(Tile t : handler.obstacleList)
			{
				if(checkCollision(t.getX(), t.getY(), t.getWidth(), t.getHeight(), (float)testShot.getX(), (float)testShot.getY(), (float)testShot.getWidth(), (float)testShot.getHeight()))
				{
					testShot.setBounds((int)x, (int)y + 52, 5, 5);
				}
				if(checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), (float)testShot.getX(), (float)testShot.getY(), (float)testShot.getWidth(), (float)testShot.getHeight()))
				{
					shoot();
					testShot.setBounds((int)x, (int)y + 52, 5, 5);
				}
			}
		}
			if(testShot.getX() > x + (WIDTH/2) || testShot.getX() < x - (WIDTH / 2))
				testShot.setBounds((int)x, (int)y + 52, 5, 5);
		
	}
	
	private void shoot()
	{
		// shoot every 500 ms
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 500)
		{
			timer2 = timer1;
			if(velX > 0)laserList.add(new Laser(x, y + 52, 20, 6, "right", 20, "green"));
			if(velX < 0)laserList.add(new Laser(x, y + 52, 20, 6, "left", 20, "green"));
			lasterShot.play();
		}
	}
}
