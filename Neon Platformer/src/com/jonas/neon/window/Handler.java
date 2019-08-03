package com.jonas.neon.window;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.objects.AT_ST_Walker;
import com.jonas.neon.objects.Block;
import com.jonas.neon.objects.Collectable;
import com.jonas.neon.objects.Enemy;
import com.jonas.neon.objects.Flag;
import com.jonas.neon.objects.Player;
import com.jonas.neon.objects.Weapon;

public class Handler 
{
	public LinkedList<GameObject> object = new LinkedList<>();
	
	private GameObject tempObject;
	private Camera cam;
	private boolean right = false;
	private boolean left = false;
	// lvl
	public BufferedImage level1, level2, level3, level4;
	
	
	public Handler(Camera cam)
	{
		this.cam = cam;
		BufferedImageLoader loader = new BufferedImageLoader();
		level1 = loader.loadImage("/level.png");
		level2 = loader.loadImage("/level2.png");
		level3 = loader.loadImage("/level3.png");
		level4 = loader.loadImage("/level4.png");
	}
	
	public void tick()
	{
		for(int i = 0; i < object.size(); i++)
		{
			tempObject = object.get(i);

			if(tempObject.getX() < (cam.getX() * -1) + Window.width + 200 && tempObject.getX() > (cam.getX() * -1) - 200)
			{
				tempObject.tick(object);
			}	
		}
		// all game objects
		//System.out.println(object.size());
	}
	
	public void render(Graphics g)
	{
		for(int i = 0; i < object.size(); i++)
		{
			tempObject = object.get(i);
			
			if(tempObject.getX() < (cam.getX() * -1) + Window.width + 200 && tempObject.getX() > (cam.getX() * -1) - 200)
			{
				tempObject.render(g);
			}	
		}	
	}
	
	public void loadImageLevel(BufferedImage image)
	{
		int w = image.getWidth();
		int h = image.getHeight();
		
		//System.out.println("width, height" + w + ", "+ h);
		
		for(int xx = 0; xx < h; xx++)
		{
			for(int yy = 0; yy < w; yy++)
			{
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				// check if there is a white pixel / dirt
				if(red == 255 && green == 255 && blue == 255)
				{
					addObject(new Block(xx * 32, yy * 32, 0, this, ObjectId.Block));
				}
				// check if there is a blue pixel
				if(red == 0 && green == 0 && blue == 255)
				{
					addObject(new Player(xx * 32, yy * 32, ObjectId.Player, this, cam));
				}
				// check if there is a turquoise pixel
				if(red == 0 && green == 255 && blue == 255)
				{
					addObject(new Block(xx * 32, yy * 32, 8, this, ObjectId.Block));
				}
				// check if there is a gray pixel / grass
				if(red == 128 && green == 128 && blue == 128)
				{
					addObject(new Block(xx * 32, yy * 32, 1, this, ObjectId.Block));
				}
				// check if there is a yellow pixel / flag
				if(red == 255 && green == 255 && blue == 0)
				{
					addObject(new Flag(xx * 32, yy * 32, ObjectId.Flag));
				}
				// check if there is a red pixel / enemy(gungan)
				if(red == 255 && green == 0 && blue == 0)
				{
					// range (x * 32)
					addObject(new Enemy(xx * 32, yy * 32 - 20,0 , 3, this, cam, ObjectId.Enemy));
				}
				// check if there is a orange pixel / collectable(engery crystal)
				if(red == 255 && green == 128 && blue == 0)
				{
					addObject(new Collectable(xx * 32, yy * 32 - 20, 0, ObjectId.Collectable));
				}
				// check if there is a pink pixel / lava top block
				if(red == 255 && green == 0 && blue == 255)
				{
					addObject(new Block(xx * 32, yy * 32, 2, this, ObjectId.Block));
				}
				// check if there is a purple pixel / lava block
				if(red == 87 && green == 0 && blue == 127)
				{
					addObject(new Block(xx * 32, yy * 32, 3, this, ObjectId.Block));
				}
				
				// check if there is a gray pixel / grass left
				if(red == 196 && green == 196 && blue == 196)
				{
					addObject(new Block(xx * 32, yy * 32, 4, this, ObjectId.Block));
				}
				// check if there is a gray pixel / grass right
				if(red == 64 && green == 64 && blue == 64)
				{
					addObject(new Block(xx * 32, yy * 32, 5, this, ObjectId.Block));
				}
				// check if there is a gray pixel / grass single
				if(red == 96 && green == 96 && blue == 96)
				{
					addObject(new Block(xx * 32, yy * 32, 6, this, ObjectId.Block));
				}
				// check if there is a green pixel / health++
				if(red == 0 && green == 255 && blue == 33)
				{
					addObject(new Collectable(xx * 32, yy * 32, 1, ObjectId.Collectable));
				}
				// check if there is a dark gray pixel / grass moveable
				if(red == 48 && green == 48 && blue == 48)
				{
					addObject(new Block(xx * 32, yy * 32, 7, this, ObjectId.Block));
				}
				// check if there is a dark gray pixel / grass moveable
				if(red == 255 && green == 127 && blue == 127)
				{
					addObject(new AT_ST_Walker(xx * 32, yy * 32, this, cam, ObjectId.AT_ST));
				}
			}
		}
		// add weapon
		addObject(new Weapon(0, 0, this, ObjectId.Weapon));
	}
	
	public void switchLevel()
	{
		clearLevel();
		cam.setX(0);	
			
		switch (Game.LEVEL) {
		case 1:
			loadImageLevel(level1);
			break;
		case 2:
			loadImageLevel(level4);
			break;
		case 3:
			loadImageLevel(level3);
			break;
		case 4:
			loadImageLevel(level2);
			break;

		default:
			Game.state = Game.STATE.MENU;
			break;
		}	
	}
	
	public void clearLevel()
	{
		object.clear();
	}
	
	public void addObject(GameObject object)
	{
		this.object.add(object);
	}
	
	public void removeObject(GameObject object)
	{
		this.object.remove(object);
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}
}
