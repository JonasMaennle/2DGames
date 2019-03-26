package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import Enity.Entity;
import Enity.TileType;
import data.Handler;
import data.ParticleEvent;
import shader.Light;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import java.awt.Rectangle;

import static Gamestate.StateManager.*;

public class Tile implements Entity{
	
	private float x, y;
	private int width, height, hp;
	private TileType type;
	private Image image;
	private Image[] aImage;
	private int index;
	private Animation anim;
	private Light lavaLight;
	private ParticleEvent event;
	private Handler handler;
	private long timer;
	
	public Tile(float x, float y, TileType type, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = type.width;
		this.height = type.height;
		this.type = type;
		this.hp = type.hp;
		this.handler = handler;
		this.timer = System.currentTimeMillis();
		this.image = quickLoaderImage("tiles/" + type.textureName + "" + ENVIRONMENT_SETTING);
		tileCounter++;
		this.aImage = new Image[2];
		this.index = 0;

		if(type == TileType.Lava)
		{
			this.anim = new Animation(loadSpriteSheet("tiles/Lava_Sheet", 64, 64), 1000);
		}
		if(type == TileType.Lava_Light)
		{
			this.anim = new Animation(loadSpriteSheet("tiles/Lava_Sheet", 64, 64), 1000);
			this.lavaLight = new Light(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), 15, 5, 1, 2f);
			this.event = new ParticleEvent((int)x + 32, (int)y + 32, 2, 0, "orange", "tiny");
		}
		
		for(int i = 0; i < aImage.length; i++)
		{
			aImage[i] = quickLoaderImage("tiles/Rock_Basic_" + i);
		}
	}
	
	public void update()
	{
		// update Lava Light
		if(type == TileType.Lava_Light)
		{
			// update light location
			if(lavaLight != null)lavaLight.setLocation(new Vector2f(x + MOVEMENT_X + 30, y + MOVEMENT_Y + 30));
			
			// create new particle event
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer = System.currentTimeMillis();
				handler.addParticleEvent(new ParticleEvent((int)x + 32, (int)y + 32, 5, 0, "orange", "tiny"));
			}
		}
	}

	public void draw()
	{
		if(type == TileType.Lava)
		{
			drawAnimation(anim, x, y, width, height);
		}else if(type == TileType.Lava_Light)
		{
			renderSingleLightStatic(shadowObstacleList, lavaLight);
			event.draw();
			drawAnimation(anim, x, y, width, height);
		}else{
			drawQuadImage(image, x, y, width, height);
		}
	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) 
	{
		this.hp = hp;
	}
	
	public int getIndex() {
		return index;
	}

	public void addIndex() 
	{
		if(this.index < 3)
		{
			this.index++;
		}
	}
	
	public void testDraw()
	{
		drawQuad(x, y, width, height);
	}

	public float getX() {
		return x;
	}
	
	public int getXPlace()
	{
		return (int)(x / TILE_SIZE);
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}
	
	public int getYPlace()
	{
		return (int)(y / TILE_SIZE);
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public float getVelY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void damage(float amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector2f[] getVertices() 
	{	
		
		// non
		if(type == TileType.Lava_Light || type == TileType.Lava)
		{
			return new Vector2f[] {
					new Vector2f(x + MOVEMENT_X + 32, y + MOVEMENT_Y + 32), // left top
					new Vector2f(x + MOVEMENT_X + 32, y + MOVEMENT_Y + height - 32), // left bottom
					new Vector2f(x + MOVEMENT_X + width - 32, y + MOVEMENT_Y + height- 32), // right bottom
					new Vector2f(x + MOVEMENT_X + width - 32, y + MOVEMENT_Y + 32) // right top
			};
		}
		
		// lower top
		return new Vector2f[] {
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), // left top
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + height), // left bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y + height), // right bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y) // right top
		};
	}

	@Override
	public boolean isOutOfMap() {
		return false;
	}

	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, width, height);
	}
	
	public Rectangle getTopBounds()
	{
		return new Rectangle((int)x + 4, (int)y, width - 8, 4);
	}
	
	public Rectangle getBottomBounds()
	{
		return new Rectangle((int)x + 4, (int)y + height-4, width - 8, 4);
	}
	
	public Rectangle getLeftBounds()
	{
		return new Rectangle((int)x, (int)y + 4, 4, height - 8);
	}
	
	public Rectangle getRightBounds()
	{
		return new Rectangle((int)x + width-4, (int)y + 4, 4, height - 8);
	}

	@Override
	public float getVelX() {
		// TODO Auto-generated method stub
		return 0;
	}
}
