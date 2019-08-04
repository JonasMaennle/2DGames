package framework.helper;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import framework.core.StateManager;
import framework.entity.GameEntity;
import framework.shader.Light;

public class Collection {
	
	// START SETTINGS
	public static int WIDTH =  960; //(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int HEIGHT =  640;//(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static boolean SET_FULL_SCREEN = false;
	public static final int TILE_SIZE = 32;
	
	public static ArrayList<Light> lights = new ArrayList<Light>();
	public static CopyOnWriteArrayList<GameEntity> shadowObstacleList = new CopyOnWriteArrayList<>();
	public static float MOVEMENT_X, MOVEMENT_Y;

	public static int TILES_WIDTH;
	public static int TILES_HEIGHT;
	
	// Font stuff
	public static int fontSize = 30;
	public static Font awtFont;
	public static TrueTypeFont font;
	
	// PLAYER STATS
	public static int PLAYER_HP = 96;
	public static float BATTERY_CHARGE = 96;
	public static int AMMO_LEFT = 999;
	public static float SPEEDBAR = 0;
	private static long time1, time2;
	
	public static int PLAYER_ID;
	
	public static int PISTOL_STREAK = 0;
	
	public static int ARENA_CURRENT_WAVE = 1;
	public static int ENEMIES_LEFT;
	
	// Static Methods
	public static void resetPlayerStats(){
		PLAYER_HP = 96;
		BATTERY_CHARGE = 96;
		AMMO_LEFT = 999;
		SPEEDBAR = 0;
	}
	
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
	
	public static void drawString(int x, int y, String text, Color color){
		
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		font.drawString(x, y, text, color);
		color = new Color(255, 255, 255, 255);
		font.drawString(x, y, "", color);

		GL11.glDisable(GL_BLEND);	
	}
	
	public static Vector2f[] getImageVertices(int x, int y, Image image, int scale){
		
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
						alpha[yy][xx] = 1;
						alphaCounter++;
					}
				}
			}
		}
		
		//System.out.println(alphaCounter);
		Vector2f[] vertices = new Vector2f[alphaCounter];
		int count = 0;
		// fill vertices array
		for(int xx = 0; xx < w; xx++){
			for(int yy = 0; yy < h; yy++){
				if(alpha[xx][yy] == 1 && count < alphaCounter){
					vertices[count] = new Vector2f(x + MOVEMENT_X + xx*scale, y + MOVEMENT_Y + yy*scale);
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
			return true;
		}
		return false;
	}
	
	// scale 4 -> 32x32 -> 8x8
	public static Image scaleImage(Image input, int scale){ 

		int w = input.getWidth() / scale;
		int h = input.getHeight() / scale;
		
		// create empty image 
		BufferedImage bufImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				Color c = input.getColor(x * scale, y * scale);
				java.awt.Color color = new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
				bufImage.setRGB(x, y, color.getRGB());
			}
		}
		
		// create Slick Image from BufferedImage
		Texture tex = null;
		try {
			tex = BufferedImageUtil.getTexture("scaledImage", bufImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image img = new Image(tex);
		return img;
	}
}
