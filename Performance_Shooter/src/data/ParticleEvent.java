package data;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import object.Particle;

public class ParticleEvent {
	
	private CopyOnWriteArrayList<Particle> list;
	private Random rand;
	
	public ParticleEvent(int startX, int startY, int number)
	{
		this.list = new CopyOnWriteArrayList<>();
		this.rand = new Random();
		for(int i = 0; i < number; i++)
		{
			list.add(new Particle(startX, startY, rand.nextInt(10), rand.nextInt(10), rand.nextInt(10)+2, rand.nextInt(10)+2, rand.nextInt(20)+5));
		}
	}
	
	public void update()
	{
		for(Particle p : list)
		{
			p.update();
		}
	}
	
	public void draw()
	{
		for(Particle p : list)
		{
			p.draw();
		}
	}
}
