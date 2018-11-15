package data;

import static helpers.Setup.*;

import Enity.Entity;

public class Camera {
	
	private Entity entity;
	private float playerOptimatePosX;
	private float playerOptimatePosY;
	
	public Camera(Entity entity)
	{
		this.entity = entity;
	}
	
	public void update()
	{
		playerOptimatePosX = (getRightBoarder() + getLeftBoarder()) / 2.0f;
		playerOptimatePosY = (getTopBoarder() + getBottomBoarder()) / 2.0f;
		if(entity.getClass().getSimpleName().equals("Speeder"))
		{
			playerOptimatePosX = getLeftBoarder() + (WIDTH/4);
		}

		MOVEMENT_X += ((playerOptimatePosX - entity.getX()) * 0.5); // * x.x -> smoothnes factor
		MOVEMENT_Y += ((playerOptimatePosY - entity.getY()) * 0.5);
		
		//System.out.println(getTopBoarder() + " " + getBottomBoarder());
	}

	public void reset()
	{
		MOVEMENT_X = 0;
		MOVEMENT_Y = 0;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
