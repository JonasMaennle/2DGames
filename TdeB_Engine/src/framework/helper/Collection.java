package framework.helper;

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import framework.core.StateManager;
import framework.entity.GameEntity;
import framework.shader.Light;

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
	
	
	// PLAYER STATS
	public static int PLAYER_HP = 96;
	public static float BATTERY_CHARGE = 96;
	public static int AMMO_LEFT = 999;
	
	private static long time1, time2;
	
	// Static Methods
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
	
	public static void timerStart(){
		time1 = System.currentTimeMillis();
	}
	
	public static void timerEnd(){
		time2 = System.currentTimeMillis();
		System.out.println("Time difference: " + ((time2 - time1)*StateManager.framesInLastSecond) + "\tms/s");
	}
	
	public static Font loadCustomFont(String path, float size){
		// read(TileImageStorage.class.getClassLoader().getResourceAsStream(path));
		Font awtFont = null;
		
		try {
	        InputStream inputStream = Collection.class.getClassLoader().getResourceAsStream(path);

	        awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);		
	        awtFont = awtFont.deriveFont(size); // set font size

		    } catch (Exception e) {
		        e.printStackTrace();
		    } 
		return awtFont;
	}
	
	public static Vector2f[] getImageVertices(int x, int y, Image image){

		int w = image.getWidth();
		int h = image.getHeight();
		//
		int[][] alpha = new int[w][h];
		int alphaCounter = 0;
		
		for(int xx = 0; xx < w; xx++){
			for(int yy = 0; yy < h; yy++){
				Color c = image.getColor(xx, yy);
				if(c.getAlpha() > 1){
					// add only if 1 neighbour got alpha = 0
					if(checkNeighbourPixel(xx, yy, image)){
						alpha[xx][yy] = 1;
						alphaCounter++;
					}
				}
			}
		}
		Vector2f[] vertices = new Vector2f[alphaCounter];
		int count = 0;
		// fill vertices array
		for(int xx = 0; xx < w; xx++){
			for(int yy = 0; yy < h; yy++){
				if(alpha[xx][yy] == 1 && count < alphaCounter){
					vertices[count] = new Vector2f(x + MOVEMENT_X + xx, y + MOVEMENT_Y + yy);
					count++;
				}
			}
		}
		//System.out.println(vertices.length);
		return vertices;
	}
	
	private static boolean checkNeighbourPixel(int targetX, int targetY, Image image){

		try {
			// top
			if(image.getColor(targetX, targetY + 1).getAlpha() < 2){
				return true;
			}
			// bottom
			if(image.getColor(targetX, targetY - 1).getAlpha() < 2){
				return true;
			}
			// left
			if(image.getColor(targetX - 1, targetY).getAlpha() < 2){
				return true;
			}
			// right
			if(image.getColor(targetX + 1, targetY).getAlpha() < 2){
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}
}
