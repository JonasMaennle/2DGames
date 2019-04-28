package core;

import java.awt.Toolkit;

public class Constants {
	
	public static int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public static final int TILE_SIZE = 32;
	public static float MOVEMENT_X, MOVEMENT_Y;
	public static boolean SET_FULL_SCREEN = true;
	
	public static int TILES_WIDTH;
	public static int TILES_HEIGHT;
}
