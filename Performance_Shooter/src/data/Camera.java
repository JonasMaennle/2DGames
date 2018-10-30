package data;

import static helpers.Artist.*;

public class Camera {
	
	private Player player;
	private int x_offset;
	private boolean startmovement;
	private int camera_trigger;
	
	public Camera(Player player)
	{
		this.camera_trigger = 400;
		this.player = player;
		this.x_offset = WIDTH - camera_trigger;
		this.startmovement = true;
	}
	
	public void update()
	{
		if(player.getX() > x_offset)
		{		
			if(startmovement)
			{
				//System.out.println("" + (int)(player.getX()) + " " + x_offset);
				MOVEMENT_X -= 1;
				startmovement = false;
			}
			
			if(MOVEMENT_X % WIDTH == 0)
			{
				x_offset += WIDTH;
				startmovement = true;
				//System.out.println("hit");
			}else{
				MOVEMENT_X -= 1;
			}
		}
	}
	
	public void reset()
	{
		MOVEMENT_X = 0;
	}
}
