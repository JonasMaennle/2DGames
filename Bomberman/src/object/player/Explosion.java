package object.player;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static framework.helper.Graphics.*;

import framework.core.Handler;
import framework.entity.GameEntity;
import framework.helper.Collection;
import network.GameClient;
import object.Obstacle;

public class Explosion implements GameEntity{

	private static final long serialVersionUID = 2944602353488218127L;

	private int x, y, range;
	
	private transient Image bottom, right, left, top, vertikal, horizontal;
	private Handler handler;
	private CopyOnWriteArrayList<Rectangle> explosionHitArea;
	
	public Explosion(int x, int y, int range, Handler handler) {
		this.x = x;
		this.y = y;
		this.range = range;
		this.handler = handler;
		this.explosionHitArea = new CopyOnWriteArrayList<>();
		
		this.bottom = quickLoaderImage("player/exp_bottom");
		this.top = quickLoaderImage("player/exp_top");
		this.left = quickLoaderImage("player/exp_left");
		this.right = quickLoaderImage("player/exp_right");
		
		this.vertikal = quickLoaderImage("player/exp_vertikal");
		this.horizontal = quickLoaderImage("player/exp_horizontal");
		
		calcHitBox();
	}
	
	private void calcHitBox(){
		for(int i = -range; i < range + 1; i++) {
			
			// horizontal
			if(i == -range)
				explosionHitArea.add(new Rectangle(x - range * 32, y, 32, 32));
			else if(i < 0)
				explosionHitArea.add(new Rectangle(x + (i * 32), y, 32, 32));
			
			if(i == (range) -1)
				explosionHitArea.add(new Rectangle(x + range * 32, y, 32, 32));
			else if(i > 0)
				explosionHitArea.add(new Rectangle(x + (i * 32) - 32, y, 32, 32));
			
			if(i == 0)
				explosionHitArea.add(new Rectangle(x, y, 32, 32));
			
			// vertikal
			if(i == -range)
				explosionHitArea.add(new Rectangle(x, y - range * 32, 32, 32));
			else if(i < 0)
				explosionHitArea.add(new Rectangle(x, y + (i * 32), 32, 32));
		
			if(i == (range) -1)
				explosionHitArea.add(new Rectangle(x, y + range * 32, 32, 32));
			else if(i > 0)
				explosionHitArea.add(new Rectangle(x, y + (i * 32) - 32, 32, 32));
		
			if(i == 0)
				explosionHitArea.add(new Rectangle(x, y, 32, 32));
		}
	}
	
	@Override
	public void update() {
		
		for(Obstacle o : handler.obstacleServerList) {
			for(Rectangle rect : explosionHitArea) {
				if(o.getBounds().intersects(rect)) {
					GameClient.removedObstacles.add(o);
					
					Collection.shadowObstacleList.remove(o);
					handler.obstacleServerList.remove(o);
				}
			}
		}
	}

	@Override
	public void draw() {
		for(int i = -range; i < range + 1; i++) {
			
			// horizontal
			if(i == -range)
				drawQuadImage(left, x - range * 32, y, 32, 32);
			else if(i < 0)
				drawQuadImage(horizontal, x + (i * 32) , y, 32, 32);
			
			if(i == (range) -1)
				drawQuadImage(right, x + range * 32, y, 32, 32);
			else if(i > 0)
				drawQuadImage(horizontal, x + (i * 32) - 32, y, 32, 32);
			
			if(i == 0)
				drawQuadImage(horizontal, x, y, 32, 32);
			
			// vertikal
			if(i == -range)
				drawQuadImage(top, x, y - range * 32, 32, 32);
			else if(i < 0)
				drawQuadImage(vertikal, x , y + (i * 32), 32, 32);
		
			if(i == (range) -1)
				drawQuadImage(bottom, x, y + range * 32, 32, 32);
			else if(i > 0)
				drawQuadImage(vertikal, x, y + (i * 32) - 32, 32, 32);
		
			if(i == 0)
				drawQuadImage(vertikal, x, y, 32, 32);
			
		}
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void setWidth(int width) {}

	@Override
	public void setHeight(int height) {}

	@Override
	public void setX(float x) {
		this.x = (int)x;
	}

	@Override
	public void setY(float y) {
		this.y = (int)y;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getTopBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBottomBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getLeftBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getRightBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public CopyOnWriteArrayList<Rectangle> getExplosionHitArea() {
		return explosionHitArea;
	}

	public void setExplosionHitArea(CopyOnWriteArrayList<Rectangle> explosionHitArea) {
		this.explosionHitArea = explosionHitArea;
	}
}
