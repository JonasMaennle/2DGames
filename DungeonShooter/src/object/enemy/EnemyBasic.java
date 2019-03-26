package object.enemy;

import java.awt.Rectangle;
import java.util.LinkedList;
import static helpers.Graphics.*;

import path.Node;

public class EnemyBasic extends Enemy{

	private LinkedList<Node> path;
	private LinkedList<Node> visited;
	private int nextX, nextY;
	private float absx, absy;
	
	public EnemyBasic(float x, float y, int width, int height) 
	{
		super(x, y, width, height);
		this.speed = 3;
		path = new LinkedList<>();
		visited = new LinkedList<>();
		
		nextX = (int)x;
		nextY = (int)y;
		
		absx = x;
		absy = y;
	}
	
	public void update()
	{
		velX = 0;
		velY = 0;
		//System.out.println(path.size());
		if(path.size() > 0)
		{
			absx = path.get(path.size() - 1).getX() * 64;
			absy = path.get(path.size() - 1).getY() * 64;
			
//			Rectangle tmpPath = new Rectangle((int)path.get(path.size() - 1).getX() * 64 + 32, (int)path.get(path.size() - 1).getY() * 64 + 32, 64, 64);
//			//System.out.println(absx + " " + x + "\t"+ absy + " " + y); 
//			if(tmpPath.intersects(this.getBounds()))
//			{
//				path.remove(path.size() - 1);
//				return;
//			}
			if(absx > x)
				velX = 1; 
			if(absx < x)
				velX = -1;
			if(absx == x)
				velX = 0;
			
			if(absy > y)
				velY = 1;
			if(absy < y)
				velY = -1;
			if(absy == y)
				velY = 0;				
		}else{
			velX = 0;
			velY = 0;
		}

		x += (velX * speed);
		y += (velY * speed);
		
		if(path.size() > 0)
		{
			Rectangle node = new Rectangle(path.get(path.size() - 1).getX() * 64 + 16, path.get(path.size() - 1).getY() * 64 + 16, 32, 32);
			if(getBounds().intersects(node) && path.size() > 0){
				visited.add(path.get(path.size()-1));
				path.remove(path.size() - 1);		
			}
		}
	}
	
	public void draw()
	{
		drawQuad(x, y, width, height);
	}
	
	public int getNextX() {
		
		if(path.size() > 1)
		{
//			Rectangle tmpPath = new Rectangle((int)path.get(path.size() - 1).getX() * 16, (int)path.get(path.size() - 1).getY() * 16, 16, 16);	
//			if(tmpPath.intersects(this.getBounds()))
//					nextX = path.get(path.size() - 2).getX();
			
			nextX = path.get(path.size() - 1).getX();
		}else{
			nextX = (int) x / 64;
		}
		return (int) absx/64;
	}

	public int getNextY() {
		
		if(path.size() > 1)
		{
//			Rectangle tmpPath = new Rectangle((int)path.get(path.size() - 1).getX() * 16, (int)path.get(path.size() - 1).getY() * 16, 16, 16);
//			if(tmpPath.intersects(this.getBounds()))
//				nextY = path.get(path.size() - 2).getY();
			nextX = path.get(path.size() - 1).getY();
		}else{
			nextY = (int) y / 64;
		}
		return (int) absy/64;
	}
	
	public void setPath(LinkedList<Node> path)
	{
		this.path = path;
	}
	
	public int getEnemyNodesLeft()
	{
		return path.size();
	}

	public LinkedList<Node> getPath() {
		return path;
	}
}
