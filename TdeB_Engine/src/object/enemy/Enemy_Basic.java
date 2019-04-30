package object.enemy;

import java.awt.Rectangle;
import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import core.Handler;
import entity.GameEntity;
import object.Player;
import path.Node;

import static core.Constants.*;
import static helper.Graphics.*;

public abstract class Enemy_Basic implements GameEntity{

	protected float x, y, speed, velX, velY;
	protected int width, height;
	protected Image image;
	
	protected LinkedList<Node> path;
	protected LinkedList<Node> visited;
	protected int nextX, nextY;
	protected float absx, absy;
	protected Handler handler;
	
	public Enemy_Basic(float x, float y, int width, int height, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = quickLoaderImage("Enemy/Enemy_tmp");
		this.speed = 2;
		this.velX = 0;
		this.velY = 0;
		
		this.path = new LinkedList<>();
		this.visited = new LinkedList<>();
		
		this.nextX = (int)x;
		this.nextY = (int)y;
		
		this.absx = x;
		this.absy = y;
		
		this.handler = handler;
	}
	
	public void update() {
		
	}

	public void draw() {
		drawQuadImage(image, x, y, width, height);
	}
	
	protected void mapCollision() {
		for(GameEntity ge : handler.obstacleList){
			// top
			if(ge.getBottomBounds().intersects(getTopBounds()))
			{
				velY = 0;
				y = (float) (ge.getY() + ge.getHeight());
			}
			// bottom
			if(ge.getTopBounds().intersects(getBottomBounds()))
			{	
				velY = 0;
				y = (float) (ge.getY() - TILE_SIZE);
			}		
			// left
			if(ge.getRightBounds().intersects(getLeftBounds()))
			{
				velX = 0;
				x = (float) (ge.getX() + ge.getWidth());
			}
			// right
			if(ge.getLeftBounds().intersects(getRightBounds()))
			{
				velX = 0;
				x = (float) (ge.getX() - TILE_SIZE);
			}	
		}
		
		Player player = handler.getPlayer();
		// top
		if(player.getBottomBounds().intersects(getTopBounds()))
		{
			velY = 0;
			y = (float) (player.getY() + player.getHeight());
		}
		// bottom
		if(player.getTopBounds().intersects(getBottomBounds()))
		{	
			velY = 0;
			y = (float) (player.getY() - TILE_SIZE);
		}		
		// left
		if(player.getRightBounds().intersects(getLeftBounds()))
		{
			velX = 0;
			x = (float) (player.getX() + player.getWidth());
		}
		// right
		if(player.getLeftBounds().intersects(getRightBounds()))
		{
			velX = 0;
			x = (float) (player.getX() - TILE_SIZE);
		}	
	}
	
	public int getNextX() {
		
		if(path.size() > 1){	
			nextX = path.get(path.size() - 1).getX();
		}else{
			nextX = (int) x / TILE_SIZE;
		}
		return (int) absx/TILE_SIZE;
	}

	public int getNextY() {
		
		if(path.size() > 1){
			nextX = path.get(path.size() - 1).getY();
		}else{
			nextY = (int) y / TILE_SIZE;
		}
		return (int) absy/TILE_SIZE;
	}
	
	public void setPath(LinkedList<Node> path){
		this.path = path;
	}
	
	public int getEnemyNodesLeft(){
		return path.size();
	}

	public LinkedList<Node> getPath() {
		return path;
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
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	@Override
	public Vector2f[] getVertices() {
		return new Vector2f[] {
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), // left top
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + height), // left bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y + height), // right bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y) // right top
		};
	}
	
	public Rectangle getTopBounds(){
		return new Rectangle((int)x + 4, (int)y, width - 8, 4);
	}
	
	public Rectangle getBottomBounds(){
		return new Rectangle((int)x + 4, (int)y + height-4, width - 8, 4);
	}
	
	public Rectangle getLeftBounds(){
		return new Rectangle((int)x, (int)y + 4, 4, height - 8);
	}
	
	public Rectangle getRightBounds(){
		return new Rectangle((int)x + width-4, (int)y + 4, 4, height - 8);
	}
}
