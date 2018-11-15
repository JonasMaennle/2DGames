package data;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleEvent {
	
	private CopyOnWriteArrayList<Particle> list;
	private Random rand;
	
	public ParticleEvent(int startX, int startY, int number, String color, String modus)
	{
		this.list = new CopyOnWriteArrayList<>();
		this.rand = new Random();
		for(int i = 0; i < number; i++)
		{
			// 										Particle(int x, int y, int width, int height, float velX, float velY, float speed)
			if(modus.equals("normal"))list.add(new Particle(startX, startY, rand.nextInt(10)+1, rand.nextInt(10)+1, rand.nextInt(10)-5, rand.nextInt(10)-5, rand.nextInt(5)+5, color));
			if(modus.equals("small"))list.add(new Particle(startX, startY, rand.nextInt(10)+1, rand.nextInt(10)+1, rand.nextInt(10)-5, rand.nextInt(10)-10, rand.nextInt(5)+3, color));
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
}
