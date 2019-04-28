package core;

import static core.Constants.*;
import static helper.Collection.*;

import Entity.GameEntity;

public class Camera {
	
	private GameEntity entity;
	private float playerOptimatePosX;
	private float playerOptimatePosY;
	
	public Camera(GameEntity entity)
	{
		this.entity = entity;
	}
	
	public void update()
	{
		playerOptimatePosX = (getRightBorder() + getLeftBorder()) / 2.0f;
		playerOptimatePosY = (getTopBorder() + getBottomBorder()) / 2.0f;
//		if(entity.getClass().getSimpleName().equals("Speeder"))
//		{
//			playerOptimatePosX = getLeftBorder() + (WIDTH/4);
//		}

		MOVEMENT_X += ((playerOptimatePosX - entity.getX()) * 0.5); // * x.x -> smoothnes factor
		MOVEMENT_Y += ((playerOptimatePosY - entity.getY()) * 0.5);
		
		//System.out.println(getTopBoarder() + " " + getBottomBoarder());
	}

	public void reset()
	{
		MOVEMENT_X = 0;
		MOVEMENT_Y = 0;
	}

	public GameEntity getEntity() {
		return entity;
	}

	public void setEntity(GameEntity entity) {
		this.entity = entity;
	}
}
