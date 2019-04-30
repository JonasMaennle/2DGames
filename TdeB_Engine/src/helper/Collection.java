package helper;

import static core.Constants.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import entity.GameEntity;
import shader.Light;

public class Collection {
	
	public static ArrayList<Light> lights = new ArrayList<Light>();
	public static CopyOnWriteArrayList<GameEntity> shadowObstacleList = new CopyOnWriteArrayList<>();
	
	public static float getLeftBorder(){
		return MOVEMENT_X * -1;
	}
	
	public static float getRightBorder(){
		return (MOVEMENT_X * -1) + WIDTH;
	}
	
	public static float getTopBorder(){
		return (MOVEMENT_Y * -1);
	}
	
	public static float getBottomBorder(){
		return (MOVEMENT_Y * -1) + HEIGHT;
	}
}
