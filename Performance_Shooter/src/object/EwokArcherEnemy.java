package object;

import static helpers.Graphics.*;

public class EwokArcherEnemy extends Enemy{

	public EwokArcherEnemy(float x, float y, int width, int height) 
	{
		super(x, y, width, height);
		
	}
	
	public void update()
	{
		
	}

	public void draw()
	{
		drawQuad(x, y, width, height);
	}
}
