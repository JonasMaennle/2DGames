package object.enemy;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

import java.awt.Rectangle;
import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.entity.EnemyType;
import framework.entity.GameEntity;
import framework.path.Node;
import object.Tile;
import object.player.Player;

public abstract class Enemy_Basic implements GameEntity{

	protected float x, y, speed, velX, velY, max_speed;
	protected float spawnX, spawnY;
	protected int width, height;
	protected float hp;
	protected boolean pathLock;
	protected String direction;
	protected EnemyType type;
	
	protected LinkedList<Node> path;
	protected LinkedList<Node> visited;
	protected int nextX, nextY;
	protected float absx, absy;
	protected int hpFactor;
	protected int playerRange;
	
	protected Handler handler;
	protected Image image;
	protected Image hpBar;
	
	protected Animation moveLeft;
	protected Animation moveRight;

	public Enemy_Basic(float x, float y, int width, int height, Handler handler){
		this.x = x;
		this.y = y;
		this.spawnX = x;
		this.spawnY = y;
		
		this.width = width;
		this.height = height;
		this.image = quickLoaderImage("enemy/Enemy_tmp");
		this.hpBar = quickLoaderImage("enemy/healthForeground");
		
		this.max_speed = 2;
		this.speed = 0;
		this.velX = 0;
		this.velY = 0;
		this.hp = 32;
		
		this.path = new LinkedList<>();
		this.visited = new LinkedList<>();
		
		this.nextX = (int)x;
		this.nextY = (int)y;
		
		this.absx = x;
		this.absy = y;
		
		this.handler = handler;
		this.pathLock = false;
		this.direction = "right";
		
		if(StateManager.gameMode == GameState.GAME)
			this.playerRange = 250;
		else
			this.playerRange = 1000;
	}

	public boolean isPathLock() {
		return pathLock;
	}

	public void setPathLock(boolean pathLock) {
		this.pathLock = pathLock;
	}

	public void update() {
		velX = 0;
		velY = 0;
		//System.out.println(path.size());
		if(path.size() > 0)
		{
			absx = (path.get(path.size() - 1).getX() * 32);
			absy = (path.get(path.size() - 1).getY() * 32);
			// System.out.println(path.size());
			//System.out.println(absx + "   " + x);
			if(absx > x)
				velX = 1; 
			if(absx < x)
				velX = -1;
			if(absx == x || x >= absx - 4 && x <= absx + 4) // removes epileptic effect
				velX = 0;
			
			if(absy > y)
				velY = 1;
			if(absy < y)
				velY = -1;
			if(absy == y || y >= absy - 4 && y <= absy + 4) // removes epileptic effect
				velY = 0;				
		}else{
			velX = 0;
			velY = 0;
		}

		// set direction
		if(velX == -1)
			direction = "left";
		if(velX == 1)
			direction = "right";

		x += (velX * speed);
		y += (velY * speed);
		
		damagePlayer();
		mapCollision();
		isPlayerInRange(playerRange);
		
		// remove visited nodes
		if(path.size() > 0){
			// create rect for current node
			Rectangle node = new Rectangle(path.get(path.size() - 1).getX() * 32, path.get(path.size() - 1).getY() * 32, 32, 32);
			
			if(getBounds().intersects(node) && path.size() > 0 && !pathLock){
				visited.add(path.get(path.size()-1));
				path.remove(path.size() - 1);		
			}
		}
	}

	public void draw() {
		drawQuadImage(image, x, y, width, height);
	}
	
	public void isPlayerInRange(int borderOffset){
		if(x > handler.getPlayer().getX() - borderOffset && x < handler.getPlayer().getX() + borderOffset){
			if(y > handler.getPlayer().getY() - borderOffset && y < handler.getPlayer().getY() + borderOffset){
				speed = max_speed;
			}
		}else
			return;
	}
	
	protected void damagePlayer(){
		Rectangle rangePlayer = new Rectangle((int)handler.getCurrentEntity().getX() - 8, (int)handler.getCurrentEntity().getY() - 8, (int)handler.getCurrentEntity().getWidth() + 16, (int)handler.getCurrentEntity().getHeight() + 16);
		if(rangePlayer.intersects(getBounds())){
			if(handler.getPlayer().getShield() != null)
				handler.getPlayer().getShield().setEngeryLeft(handler.getPlayer().getShield().getEngeryLeft() - 1);
			else	
				PLAYER_HP -= 1;
		}
	}
	
	protected void mapCollision() {
		for(GameEntity ge : handler.obstacleList){
			int boxingOffet = 2;
			if(ge instanceof Tile){
				boxingOffet = 0;
			}
			// top
			if(ge.getBottomBounds().intersects(getTopBounds()))
			{
				velY = 0;
				y = (float) (ge.getY() + ge.getHeight() + boxingOffet);
			}
			// bottom
			if(ge.getTopBounds().intersects(getBottomBounds()))
			{	
				velY = 0;
				y = (float) (ge.getY() - TILE_SIZE - boxingOffet);
			}		
			// left
			if(ge.getRightBounds().intersects(getLeftBounds()))
			{
				velX = 0;
				x = (float) (ge.getX() + ge.getWidth() + boxingOffet);
			}
			// right
			if(ge.getLeftBounds().intersects(getRightBounds()))
			{
				velX = 0;
				x = (float) (ge.getX() - TILE_SIZE - boxingOffet);
			}	
		}
		
		for(Enemy_Basic ge : handler.enemyList){
			if(ge.getSpeed() != 0){
				int boxingOffet = 2;
				// top
				if(ge.getBottomBounds().intersects(getTopBounds()))
				{
					velY = 0;
					y = (float) (ge.getY() + ge.getHeight() + boxingOffet);
				}
				// bottom
				if(ge.getTopBounds().intersects(getBottomBounds()))
				{	
					velY = 0;
					y = (float) (ge.getY() - TILE_SIZE - boxingOffet);
				}		
				// left
				if(ge.getRightBounds().intersects(getLeftBounds()))
				{
					velX = 0;
					x = (float) (ge.getX() + ge.getWidth() + boxingOffet);
				}
				// right
				if(ge.getLeftBounds().intersects(getRightBounds()))
				{
					velX = 0;
					x = (float) (ge.getX() - TILE_SIZE - boxingOffet);
				}	
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

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}
	
	public void die(){
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public EnemyType getType() {
		return type;
	}

	public float getMax_speed() {
		return max_speed;
	}

	public void setMax_speed(float max_speed) {
		this.max_speed = max_speed;
	}
}
