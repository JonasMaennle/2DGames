package framework.helper;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import framework.entity.GameEntity;
import shader.Light;

public class Collection {
	
	// START SETTINGS
	public static int WIDTH = 960;//(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int HEIGHT = 640;//(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static boolean SET_FULL_SCREEN = false;
	public static final int TILE_SIZE = 32;
	
	public static ArrayList<Light> lights = new ArrayList<Light>();
	public static CopyOnWriteArrayList<GameEntity> shadowObstacleList = new CopyOnWriteArrayList<>();
	public static float MOVEMENT_X, MOVEMENT_Y;

	
	public static int TILES_WIDTH;
	public static int TILES_HEIGHT;
	
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
