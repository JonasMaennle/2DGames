package data;

import static helpers.Graphics.quickLoaderImage;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleEvent {
	
	private CopyOnWriteArrayList<Particle> list;
	private Random rand;
	private float x, y;
	
	public ParticleEvent(int startX, int startY, int number, float velX, String color, String modus)
	{
		this.list = new CopyOnWriteArrayList<>();
		this.rand = new Random();
		this.x = startX;
		this.y = startY;
		
		for(int i = 0; i < number; i++)
		{
			// 										Particle(int x,  int y,  int width,         int height,          float velX,         float velY,         float speed)
			if(modus.equals("normal"))list.add(new Particle(startX, startY, rand.nextInt(10)+1, rand.nextInt(10)+1, rand.nextInt(10)-5, rand.nextInt(10)-5, rand.nextInt(5)+5, color, 0));
			if(modus.equals("small"))list.add(new Particle(startX, startY, rand.nextInt(10)+1, rand.nextInt(10)+1, rand.nextInt(10)-5, rand.nextInt(10)-10, rand.nextInt(5)+3, color, 0));
			if(modus.equals("tiny"))list.add(new Particle(startX, startY, rand.nextInt(5)+1, rand.nextInt(5)+1, rand.nextInt(3)-2, rand.nextInt(6)-6, rand.nextInt(2)+1, color, 0));
			if(modus.equals("ewok_explosion"))list.add(new Particle(startX, startY, rand.nextInt(10)+1, rand.nextInt(10)+1, velX * rand.nextInt(10), rand.nextInt(10)-10, rand.nextInt(5)+2, color, rand.nextInt(360)));
		}
		
		if(modus.equals("ewok_explosion"))
		{
			// body parts
			list.add(new Particle(startX, startY, rand.nextInt(3)+30, rand.nextInt(3)+30, velX * (rand.nextInt(3) + 3), rand.nextInt(3)-3, rand.nextInt(4)+2, "ewok", rand.nextInt(360), quickLoaderImage("particles/Ewok/death_0")));
			list.add(new Particle(startX, startY, rand.nextInt(3)+30, rand.nextInt(3)+30, velX * (rand.nextInt(3) + 3), rand.nextInt(3)-3, rand.nextInt(4)+2, "ewok", rand.nextInt(360), quickLoaderImage("particles/Ewok/death_1")));
			list.add(new Particle(startX, startY, rand.nextInt(3)+30, rand.nextInt(3)+30, velX * (rand.nextInt(3) + 3), rand.nextInt(3)-3, rand.nextInt(4)+2, "ewok", rand.nextInt(360), quickLoaderImage("particles/Ewok/death_1")));
			list.add(new Particle(startX, startY, rand.nextInt(3)+30, rand.nextInt(3)+30, velX * (rand.nextInt(3) + 3), rand.nextInt(3)-3, rand.nextInt(4)+2, "ewok", rand.nextInt(360), quickLoaderImage("particles/Ewok/death_2")));
		}
	}
	
	public void update()
	{
		for(Particle p : list)
		{
			if(p.isOutOfMap())
			{
				list.remove(p);
			}else{
				p.update();
			}
		}
	}
	
	public void draw()
	{
		for(Particle p : list)
		{
			p.draw();
		}
	}
	
	public boolean isListEmpty()
	{
		if(list.size() == 0)
			return true;
		
		return false;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
