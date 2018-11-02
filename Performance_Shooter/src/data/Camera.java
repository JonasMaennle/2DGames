package data;

import static helpers.Artist.*;

import object.Player;

public class Camera {
	
	private Player player;
	private float playerOptimatePosX;
	private float playerOptimatePosY;
	
	public Camera(Player player)
	{
		this.player = player;
	}
	
	public void update()
	{
		playerOptimatePosX = (getRightBoarder() + getLeftBoarder()) / 2.0f;
		playerOptimatePosY = (getTopBoarder() + getBottomBoarder()) / 2.0f;

		MOVEMENT_X += ((playerOptimatePosX - player.getX()) * 0.5); // * x.x -> smoothnes factor
		MOVEMENT_Y += ((playerOptimatePosY - player.getY()) * 0.5);
		
		//System.out.println(getTopBoarder() + " " + getBottomBoarder());
	}
	
	public void reset()
	{
		MOVEMENT_X = 0;
		MOVEMENT_Y = 0;
	}
}
