package object;

import java.awt.Rectangle;

import core.Handler;

public class Enemy_Spider extends Enemy_Basic{

	public Enemy_Spider(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
	}
	
	public void update(){
		velX = 0;
		velY = 0;
		//System.out.println(path.size());
		if(path.size() > 0)
		{
			absx = path.get(path.size() - 1).getX() * 32;
			absy = path.get(path.size() - 1).getY() * 32;

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
		mapCollision();
		
		// remove visited nodes
		if(path.size() > 0){
			// create rect for current node
			Rectangle node = new Rectangle(path.get(path.size() - 1).getX() * 32 + 16, path.get(path.size() - 1).getY() * 32 + 16, 16, 16);
			
			if(getBounds().intersects(node) && path.size() > 0){
				visited.add(path.get(path.size()-1));
				path.remove(path.size() - 1);		
			}
		}
	}
}
