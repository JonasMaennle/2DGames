package framework.core;

import static framework.helper.Collection.*;

import framework.entity.GameEntity;

public class Camera {
	
	private GameEntity entity;
	private float playerOptimatePosX;
	private float playerOptimatePosY;
	
	public Camera(GameEntity entity){
		this.entity = entity;
		reset();
	}
	
	public void update(){
		playerOptimatePosX = (getRightBorder() + getLeftBorder()) / 2.0f;
		playerOptimatePosY = (getTopBorder() + getBottomBorder()) / 2.0f;

		MOVEMENT_X += ((playerOptimatePosX - entity.getX()) * 0.5); // * x.x -> smoothnes factor
		MOVEMENT_Y += ((playerOptimatePosY - entity.getY()) * 0.5);
	}

	public void reset(){
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
