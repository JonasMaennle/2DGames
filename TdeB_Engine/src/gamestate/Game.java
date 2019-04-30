package gamestate;

import org.lwjgl.input.Keyboard;
import static helper.Graphics.*;
import static core.Constants.*;

import static helper.Collection.*;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

import core.BackgroundHandler;
import core.Camera;
import core.Handler;

public class Game {
	
	private Handler handler;
	private Camera camera;
	private BackgroundHandler backgroundHandler;
	
	private Image filter;
	private float filterScale = 1.55f; // between 1.2f and 1.7f
	private float filterValue = 0.01f;
	private float[][] alphaFilter;
	
	public Game(Handler handler)
	{
		this.handler = handler;
		this.camera = new Camera(handler.getCurrentEntity());
		this.backgroundHandler = new BackgroundHandler();
		this.filter = quickLoaderImage("background/Filter");
		
		initFilter();
	}
	
	public void update(){
		
		camera.update();
		handler.update();
		
		while(Keyboard.next())
		{
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
			{
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}
	}
	
	public void render(){
		backgroundHandler.draw();
		handler.draw();
		
		renderLightEntity(shadowObstacleList);

		
		TiledMap t_map = null;
		try {
			t_map = new TiledMap("res/level/map_01_new.tmx", true);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		TileSet set = t_map.getTileSet(0);

		int sheetX = set.getTileX(73);
		int sheetY = set.getTileY(73);

		// public Image getTileImage(int tileID) { return this.tileSet.get(tileID); }
		Image tmpImg;// = set.tiles.getSprite(sheetX, sheetY);
		//tmpImg = set.tiles.getSubImage(sheetX * 32, sheetY * 32, 32, 32);
		
		SpriteSheet ss = set.tiles; // 23 * 24 images
		
		
		tmpImg = ss.getSprite(4, 4);
		tmpImg = tmpImg.getScaledCopy(set.tiles.getWidth(), set.tiles.getHeight());
		//System.out.println(tmpImg.getWidth() + "   " + tmpImg.getHeight());
		SpriteSheet ss2 = new SpriteSheet(tmpImg, 32, 32);

		Image img2 = ss2.getSprite(15, 15);
		System.out.println(img2.getWidth());
		drawQuadImage(img2, 100, 100, 32, 32);
		
		
		
		
		
		// draw filter
//		for(int y = 0; y < HEIGHT/TILE_SIZE + 1; y++){
//			for(int x = 0; x < WIDTH/TILE_SIZE + 1; x++){
//				GL11.glColor4f(0, 0, 0, alphaFilter[y][x]);
//				drawQuadImageStatic(filter, (x*32), (y*32), 32, 32);
//				GL11.glColor4f(1, 1, 1, 1);
//			}
//		}
	}
	
	private void initFilter(){
		alphaFilter = new float[HEIGHT/TILE_SIZE + 1][(WIDTH/TILE_SIZE) + 1]; 		
		// fill array with value = 1
		for(int y = 0; y < HEIGHT/TILE_SIZE + 1; y++){
			for(int x = 0; x < WIDTH/TILE_SIZE + 1; x++){
				alphaFilter[y][x] = 1;
			}
		}
		
		// render filter for topFilterObstacle (objects in the fog of war)
		for(int i = 0; i < HEIGHT/TILE_SIZE; i++){
			alphaFilter = drawCircle(WIDTH/2/TILE_SIZE, HEIGHT/2/TILE_SIZE, i, alphaFilter);
			filterValue *= filterScale;
		}
	}
	
	private float[][] drawCircle(int x, int y, int r, float[][] array) {
	    double angle, x1, y1;

	    int arrayWidth = array[0].length;
	    int arrayHeight = array.length;
	    
	    for (int i = 0; i < 360; i++) {
	        angle = i;
	        x1 = r * Math.cos(angle * Math.PI / 180);
	        y1 = r * Math.sin(angle * Math.PI / 180);

	        int ElX = (int) Math.round(x + x1);
	        int ElY = (int) Math.round(y + y1);
	        if(ElX < arrayWidth && ElX >= 0 && ElY < arrayHeight && ElY >= 0)
	        	array[ElY][ElX] = filterValue;
	    }  
	    return array;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
