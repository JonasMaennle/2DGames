package object.other;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

import java.awt.Rectangle;

import framework.core.Handler;
import framework.entity.GameEntity;

public class Tile implements GameEntity{
	
	private int x, y, ID;
	private int width, height;
	private Image image;
	private int waveOffset;
	private boolean triggerWave;
	private boolean tideMax;

	public Tile(int x, int y, Image image, Handler handler, int ID){
		this.x = x;
		this.y = y;
		this.ID = ID;
		this.width = TILE_SIZE;
		this.height = TILE_SIZE;
		this.image = image;
		this.waveOffset = 0;
	}
	
	public void update(){}

	public void draw(){
		
		drawQuadImage(image, x, y - waveOffset, width, height);	
	}

	public float getX() {
		return x;
	}
	
	public int getXPlace(){
		return (int)(x / TILE_SIZE);
	}

	public float getY() {
		return y;
	}
	
	public int getYPlace(){
		return (int)(y / TILE_SIZE);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public Vector2f[] getVertices() {	
		
		// no shadow tiles
		if(ID == 55 || ID == 65) {
			return new Vector2f[] {};
		}

		return new Vector2f[] {
				new Vector2f((x + MOVEMENT_X + width/2) * SCALE,	(y + MOVEMENT_Y) * SCALE), // top center
				new Vector2f((x + MOVEMENT_X) * SCALE, 				(y + MOVEMENT_Y + height/4) * SCALE), // left top corner
				new Vector2f((x + MOVEMENT_X) * SCALE, 				(y + MOVEMENT_Y + height * 0.75f) * SCALE), // left bottom corner
				new Vector2f((x + MOVEMENT_X + width/2) * SCALE,	(y + MOVEMENT_Y + height) * SCALE), // bottom center
				new Vector2f((x + MOVEMENT_X + width) 	* SCALE, 	(y + MOVEMENT_Y + height * 0.75f) * SCALE), // right bottom corner
				new Vector2f((x + MOVEMENT_X + width) 	* SCALE, 	(y + MOVEMENT_Y + height/4) * SCALE) // right top corner
		};
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
	
	public Rectangle getTopBounds(){
		return new Rectangle((int)x + 4, (int)y, width - 8, 4);
	}
	
	public Rectangle getBottomBounds(){
		return new Rectangle((int)x + 4, (int)y + height-4, width - 8, 4);
	}
	
	public Rectangle getLeftBounds(){
		return new Rectangle((int)x, (int)y + 4, 4, height - 8);
	}
	
	public Rectangle getRightBounds(){
		return new Rectangle((int)x + width-4, (int)y + 4, 4, height - 8);
	}

	public int getID() {
		return ID;
	}

	public int getWaveOffset() {
		return waveOffset;
	}

	public void setWaveOffset(int waveOffset) {
		this.waveOffset = waveOffset;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isTriggerWave() {
		return triggerWave;
	}

	public void setTriggerWave(boolean triggerWave) {
		this.triggerWave = triggerWave;
	}

	public boolean isTideMax() {
		return tideMax;
	}

	public void setTideMax(boolean tideMax) {
		this.tideMax = tideMax;
	}
}

