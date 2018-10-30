package data;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;

import static helpers.Leveler.*;
import static helpers.Artist.*;

import java.awt.Rectangle;

public class Player implements Entity{
	
	private TileGrid grid;
	private float x, y, velX, velY;
	private float speed, gravity;
	private Rectangle rectLeft, rectRight, rectTop, rectBottom;
	private boolean falling, jumping;
	private final float MAX_SPEED = 30;
	private int frameCount;
	private String currentAnimation;
	
	// Animations
	private Animation anim_idleRightw;
	private Animation anim_walkRight;
	
	
	public Player(float x, float y, TileGrid grid)
	{
		this.grid = grid;
		this.speed = 2;
		this.velX = 0;
		this.velY = 0;
		this.x = x; 
		this.y = y;
		this.jumping = false;
		this.frameCount = 110;
		this.gravity = 4;
		this.rectLeft = new Rectangle((int)x, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectRight = new Rectangle((int)x + TILE_SIZE - 4, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectTop = new Rectangle((int)x + 4, (int)y, TILE_SIZE - 8, 4);
		this.rectBottom = new Rectangle((int)x + 4, (int)y + (TILE_SIZE * 2) - 4, TILE_SIZE - 8, 4);
		
		this.anim_walkRight = new Animation(loadSpriteSheet("player_walk", TILE_SIZE, TILE_SIZE * 2), 80);
		this.anim_idleRightw = new Animation();
		this.currentAnimation = "walkRight";
	}
	
	public void update()
	{	
		velX = 0;
		velY = gravity;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			velX += 1;
			
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			velX -= 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) && !jumping)
		{
			jumping = true;
		}

		jump();
		
		x += velX * speed;
		y += velY * speed;
		
		mapCollision();
		//System.out.println("x :  " + x + " y: " + y);
		draw();
	}
	
	public void draw()
	{	
		switch (currentAnimation) {
		case "idleRight":
			drawQuadImage(anim_walkRight.getImage(0), x, y, TILE_SIZE, TILE_SIZE * 2);
			break;
		case "walkRight":
			drawAnimation(anim_walkRight, x, y, TILE_SIZE, TILE_SIZE * 2);
			break;
		default:
			break;
		}
		//drawQuadImage(img, x, y, TILE_SIZE, TILE_SIZE * 2);
		//drawBounds();
	}
	
	private void mapCollision()
	{
		updateBounds();

		for(Tile t : obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), TILE_SIZE, TILE_SIZE);
			
			if(r.intersects(rectTop))
			{
				velY = gravity;
				y = (float) (r.getY() + TILE_SIZE);
				jumping = false;
				return;
			}
			if(r.intersects(rectLeft))
			{
				velX = 0;
				x = (float) (r.getX() + r.getWidth());
			}
			if(r.intersects(rectRight))
			{
				velX = 0;
				x = (float) (r.getX() - TILE_SIZE);
			}
			if(r.intersects(rectBottom))
			{
				velY = 0;
				y = (float) (r.getY() - TILE_SIZE * 2);
				jumping = false;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void drawBounds()
	{
		drawQuad(x, y + 4, 4, (TILE_SIZE * 2) - 16); // left
		drawQuad(x + TILE_SIZE - 4, y + 4, 4, (TILE_SIZE * 2) - 16); // right
		drawQuad(x + 4, y, TILE_SIZE - 8, 4); // top
		drawQuad(x + 4, y + (TILE_SIZE * 2) - 4, TILE_SIZE - 8, 4); // bottom
	}
	
	private void jump()
	{
		if(jumping)
		{
			if(falling || jumping && frameCount >= 0)
			{
				velY -= frameCount * 0.1;
				frameCount -= 4;
				if(velY > MAX_SPEED)
				{
					velY = MAX_SPEED;
				}
			}
		}else{
			frameCount = 135;
		}
	}
	
	private void updateBounds()
	{
		this.rectLeft.setBounds((int)x, (int)y + 4 , 4, (TILE_SIZE * 2) - 16);
		this.rectRight.setBounds((int)x + TILE_SIZE - 4, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectTop.setBounds((int)x + 4, (int)y, TILE_SIZE - 8, 4);
		this.rectBottom.setBounds((int)x + 4, (int)y + (TILE_SIZE * 2)- 4, TILE_SIZE - 8, 4);
	}
	
	public Tile getMouseTile()
	{
		if(Mouse.getX() >= WIDTH && Mouse.getY() < HEIGHT && Mouse.getY() > 0)
		{
			return grid.getTile(((WIDTH-TILE_SIZE) / TILE_SIZE), ((HEIGHT - Mouse.getY() - 1)/TILE_SIZE));
		}
		
		return grid.getTile((Mouse.getX() / TILE_SIZE), ((HEIGHT - Mouse.getY() - 1)/TILE_SIZE));
	}
	
	public Tile getPlayerTile()
	{
		if(getX() >= WIDTH && getY() < HEIGHT && getY() > 0)
		{
			return grid.getTile(((WIDTH-TILE_SIZE) / TILE_SIZE), ((int)(HEIGHT - getY() - 1)/TILE_SIZE));
		}
		
		return grid.getTile(((int)getX() / TILE_SIZE), ((int)(HEIGHT - getY() - 1)/TILE_SIZE));
	}

	@Override
	public float getX() 
	{
		return x;
	}

	@Override
	public float getY() 
	{
		return y;
	}

	@Override
	public int getWidth()
	{
		return TILE_SIZE;
	}

	@Override
	public int getHeight() 
	{
		return TILE_SIZE;
	}

	@Override
	public void setWidth(int width) 
	{
		
	}

	@Override
	public void setHeight(int height) 
	{
		
	}

	@Override
	public void setX(float x) 
	{
		this.x = x;
	}

	@Override
	public void setY(float y) 
	{
		this.y = y;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}
}
