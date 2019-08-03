package com.jonas.neon.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.KeyInput;
import com.jonas.neon.framework.MouseInput;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.framework.Sound;
import com.jonas.neon.framework.Texture;
import com.jonas.neon.objects.AT_ST_Walker;
import com.jonas.neon.objects.Player;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = -6863828205386232919L;
	
	public static int WIDTH;
	public static int HEIGHT;
	public float cloudX = 0;
	private int fps;

	private boolean running = false;
	private Thread thread;
	private float backgroundX1, backgroundX2;
	
	public BufferedImage level, clouds, menuFrame, background_01, background_01_1, filter;
	public static int LEVEL = 1;
	private boolean ready = true;
	
	// Object
	Handler handler;
	Camera cam;
	static Texture tex;
	private HUD gameHud;
	private Menu mainMenu;
	private BufferedImageLoader loader;
	private Sound sound;
	private GameObject go, temp;
	private Player p;
	private AT_ST_Walker walker;
	private MouseInput in;
	
	// MenuEnum System 
	public static enum STATE{
		MENU,
		GAME,
		END,
		HELP,
	};
	public static STATE state = STATE.MENU;
	
	private void init()
	{
		WIDTH = getWidth();
		HEIGHT = getHeight();			
		
		tex = new Texture();
		gameHud = new HUD();
		mainMenu = new Menu();
				
		loader = new BufferedImageLoader();
		level = loader.loadImage("/level.png"); // loading the level
		clouds = loader.loadImage("/cloud.png");
		menuFrame = loader.loadImage("/MenuFrame.png");
		background_01 = loader.loadImage("/background_01.png");
		background_01_1 = loader.loadImage("/background_1_1.png");
		filter = loader.loadImage("/layer_filter.png");
		
		cam = new Camera(0,0);
		handler = new Handler(cam);	
		sound = new Sound();
		
		handler.loadImageLevel(level);
		in = new MouseInput();
		this.addKeyListener(new KeyInput(cam, handler, sound));
		this.addMouseListener(in);
		this.addMouseMotionListener(in);
	}
	
	public synchronized void start()
	{
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() 
	{
		init();
		this.requestFocus();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1)
			{
				tick();
				updates++;
				delta--;
			}					
			render();
			frames++;
							
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				System.out.println("FPS: " + frames + " Ticks: " + updates);
				fps = frames;
				updates = 0;
				frames = 0;
			}
		}
	}
	
	private void tick()
	{
		// mouse update
		in.tick();
		
		if(HUD.health <= 0)
		{			
			state = STATE.MENU;
			LEVEL = 1;
		}
		
		if(state == STATE.GAME)
		{
			ready = true;
			handler.tick();
			gameHud.tick();
			
			// check mouse
			if(MouseInput.mouseVisible)
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}else if(!MouseInput.mouseVisible)
			{
				setCursor(java.awt.Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR),new java.awt.Point(0,0),"NOCURSOR"));
			}
		}	
		
		if(state == STATE.MENU)
		{
			AT_ST_Walker.enabled = false;
			LEVEL = 1;
			HUD.health = 3;
			
			for(int i = 0; i < handler.object.size(); i++)
			{
				go = handler.object.get(i);
				if(go.getID() == ObjectId.Player)
				{
					p = (Player)go;
					p.respawn();
				}
			}
			if(p == null)
			{
				handler.addObject(new Player(100, 100, ObjectId.Player, handler, cam));
			}

			if(ready)
			{
				handler.switchLevel();
				ready = false;
			}	
		}
		
		for(int i = 0; i < handler.object.size(); i++)
		{
			if(!AT_ST_Walker.enabled)
			{
				if(handler.object.get(i).getID() == ObjectId.Player)
				{
					cam.tick(handler.object.get(i));			
				}
			}
					
			if(AT_ST_Walker.enabled)
			{
				if(handler.object.get(i).getID() == ObjectId.AT_ST)
				{
					cam.tickAT_ST(handler.object.get(i));			
				}
			}		
		}	
		
		// background movement	
		for(int i = 0; i < handler.object.size(); i++) // player
		{
			temp = handler.object.get(i);
			if(temp.getID() == ObjectId.Player)
			{
				p = (Player)temp;
				
				if(p.getVelX() < 0 && p.getMovement())
				{
					backgroundX1 += 0.1;
					backgroundX2 += 0.5;
				}else if(p.getVelX() > 0 && p.getMovement())
				{
					backgroundX1 -= 0.1;
					backgroundX2 -= 0.5;
				}
			}
		}	
		
		for(int i = 0; i < handler.object.size(); i++) // AT_ST
		{
			temp = handler.object.get(i);
			if(temp.getID() == ObjectId.AT_ST)
			{
				walker = (AT_ST_Walker)temp;
				
				if(walker.getVelX() < 0 && walker.getMovement())
				{
					backgroundX1 += 0.1;
					backgroundX2 += 0.5;
				}else if(walker.getVelX() > 0 && walker.getMovement())
				{
					backgroundX1 -= 0.1;
					backgroundX2 -= 0.5;
				}
			}
		}
	}
	
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}	
		
		Graphics g = bs.getDrawGraphics();	
		Graphics2D g2d = (Graphics2D) g;
		
		/////////////////////////////////
		/////////////////////////////////
		//g.setColor(Color.blue);
		g.setColor(new Color(179, 230, 255));
		g.fillRect(0, 0, getWidth(), getHeight()); // draw sky
		
		// draw custom background
		
		// moving mountain
		if(backgroundX1 < Window.width)
		{
			g.drawImage(background_01_1, (int)backgroundX1, 100, null); // forest
			g.drawImage(background_01_1, (int)backgroundX1 + Window.width, 100, null); // forest
			g.drawImage(background_01_1, (int)backgroundX1 - Window.width, 100, null); // forest
		}else{
			backgroundX1 = 0;
		}
		
		if(backgroundX1 > -Window.width)
		{
			g.drawImage(background_01_1, (int)backgroundX1, 100, null); // forest
			g.drawImage(background_01_1, (int)backgroundX1 + Window.width, 100, null); // forest
			g.drawImage(background_01_1, (int)backgroundX1 - Window.width, 100, null); // forest
		}else{
			backgroundX1 = 0;
		}
		
		// Cloud spawn system
		if(cloudX >  -Window.width)
		{
			g.drawImage(clouds, (int)cloudX, 0, null);
			g.drawImage(clouds, (int)cloudX + Window.width, 0, null);
			cloudX -= 0.02f;
		}else{
			cloudX = 0;
		}
			
		// moving forest
		if(backgroundX2 < Window.width)
		{
			g.drawImage(background_01, (int)backgroundX2, 0, null); // forest
			g.drawImage(background_01, (int)backgroundX2 + Window.width, 0, null); // forest
			g.drawImage(background_01, (int)backgroundX2 - Window.width, 0, null); // forest
		}else{
			backgroundX2 = 0;
		}
		
		if(backgroundX2 > -Window.width)
		{
			g.drawImage(background_01, (int)backgroundX2, 0, null); // forest
			g.drawImage(background_01, (int)backgroundX2 + Window.width, 0, null); // forest
			g.drawImage(background_01, (int)backgroundX2 - Window.width, 0, null); // forest
		}else{
			backgroundX2 = 0;
		}
		
		// draw blue background filter
		g.drawImage(filter, 0, 0, Window.width, Window.height, null);
		
		// Check Game State
		if(state == STATE.GAME)
		{
			g2d.translate(cam.getX(), cam.getY()); // begin of cam
			
			handler.render(g);
		
			g2d.translate(-cam.getX(), -cam.getY()); // end of cam
			// Hud
			gameHud.render(g);
		
		}else if(state == STATE.MENU)
		{
			mainMenu.render(g);
		}else if(state == STATE.END)
		{
			
		}else if(state == STATE.HELP)
		{
			g.setColor(Color.gray);
			Font hudFont = new Font("Verdana", Font.BOLD, 17);
			g.setFont(hudFont);
					
			// g.fillRect(0, 0, WIDTH, HEIGHT);
			g.drawImage(menuFrame, 0, 0, null);
			
			g.setColor(Color.black);
			g.drawString("Menu", 20, 25);
			
			g.drawString("# Press 'A' to move to the left", 20, 80);
			g.drawString("# Press 'D' to move to the right", 20, 120);
			
			// draw player/weapon images
			g.drawImage(tex.player[0], 350, 40, null);
			g.drawImage(tex.player[15], 420, 40, null);
			
			g.drawString("# Press 'SPACE' to jump", 20, 200);
			g.drawImage(tex.player[17], 350, 150, null);
			
			g.drawString("# Press 'Enter' to shoot", 20, 280);
			g.drawImage(tex.player[15], 350, 230, null);
			g.drawImage(tex.weapon[2], 395, 272, null);
			g.drawImage(tex.weapon[0], 357, 257, null);
			g.drawImage(tex.weapon[2], 415, 272, null);
			g.drawImage(tex.weapon[2], 435, 272, null);
			
			//g.drawString("-----------------------------------------------", 20, 360);
			g.drawString("# Collect energy crystals to refill your ammo", 20, 400);
			g.drawImage(tex.collectable[0], 450, 380, null);
			g.drawString("# Shoot the enemy Gungans to survive", 20, 480);
			g.drawImage(tex.enemy[10], 400, 440, null);		
		}
		
		// FPS
		Font f = new Font("arial", Font.BOLD, 20);
		g.setFont(f);
		g.setColor(Color.black);
		g.drawString("FPS: " + fps, 10, Window.height - 10);
		
		/////////////////////////////////
		/////////////////////////////////
		g.dispose();
		bs.show();		
	}
	
	public static Texture getInstance()
	{
		return tex;
	}
	
	public static void main(String[] args) 
	{
		new Window("Neon Platformer", new Game());
	}
}
