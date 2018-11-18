package UI;

import java.awt.Font;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import static helpers.Graphics.*;
import static helpers.Setup.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

public class UI {
	
	private ArrayList<Button> buttonList;
	private ArrayList<Menu> menuList;
	private TrueTypeFont font;
	private Font awtFont;
	
	public UI()
	{
		this.buttonList = new ArrayList<>();
		this.menuList = new ArrayList<>();
		awtFont = new Font("Arial", Font.BOLD, 24);

		font = new TrueTypeFont(awtFont, false);
	}
	
	public void drawString(int x, int y, String text)
	{
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	
		font.drawString(x, y, text);	
		GL11.glDisable(GL_BLEND);
	}
	
	public void addButton(String name, String textureName, int x, int y)
	{
		buttonList.add(new Button(name, quickLoaderImage(textureName), x, y));
	}
	
	public void addButton(String name, String textureName, int x, int y, int width, int height)
	{
		buttonList.add(new Button(name, quickLoaderImage(textureName), x, y, width, height));
	}
	
	public boolean isButtonClicked(String buttonName)
	{
		Button b = getButton(buttonName);
		float mouseY = HEIGHT - Mouse.getY() - 1;
		if(Mouse.getX() > b.getX() && Mouse.getX() < b.getX() + b.getWidth() &&
				mouseY > b.getY() && mouseY < b.getY() + b.getHeight()){
			return true;
		}else
			return false;
	}
	
	private Button getButton(String buttonName)
	{
		for(Button b : buttonList)
		{
			if(b.getName().equals(buttonName))
			{
				return b;
			}
		}
		return null;
	}
	
	public void createMenu(String name, int x, int y, int width, int height, int optionsWidth, int optionsHeight)
	{
		menuList.add(new Menu(name, x, y, width, height, optionsWidth, optionsHeight));
	}
	
	public void drawImage(int x, int y,int width,int height, Image image)
	{
		drawQuadImage(image, x, y, width, height);
	}
	
	public Menu getMenu(String name)
	{
		for(Menu m : menuList)
		{
			if(name.equals(m.getName()))
				return m;
		}
		return null;
	}
	
	public void draw()
	{
		for(Button b : buttonList)
			drawQuadImage(b.getImage(), b.getX(), b.getY(), b.getWidth(), b.getHeight());
		
		for(Menu m: menuList)
			m.draw();
		
	}
	
	public ArrayList<Button> getButtonList() {
		return buttonList;
	}

	public void setButtonList(ArrayList<Button> buttonList) {
		this.buttonList = buttonList;
	}



	public class Menu{
		
		String name;
		private ArrayList<Button> menuButtons;
		private int x, y, width, height, buttonAmount, optionsWidth, optionsHeight, padding;	
		
		public Menu(String name, int x, int y, int width, int height, int optionsWidth, int optionsHeight)
		{
			this.name = name;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.optionsWidth = optionsWidth;
			this.optionsHeight = optionsHeight;
			this.padding = (width - (optionsWidth * TILE_SIZE)) / (optionsWidth + 1);
			this.buttonAmount = 0;
			this.menuButtons = new ArrayList<>();
		}
		
		public void addButton(Button b)
		{
			setButton(b);
		}
		
		public void quickAdd(String name, String textureName, int width, int height)
		{
			Button b = new Button(name, quickLoaderImage(textureName), 0, 0, width, height);
			setButton(b);
		}
		
		private void setButton(Button b)
		{
			if(optionsWidth != 0)
				b.setY(y + (buttonAmount / optionsHeight) * TILE_SIZE);
			b.setX(x + (buttonAmount % optionsWidth) * (padding + TILE_SIZE) + padding);
			buttonAmount++;
			menuButtons.add(b);
		}
		
		public void draw()
		{
			for(Button b : menuButtons)
			{
				drawQuadImageStatic(b.getImage(), b.getX(), b.getY(), b.getWidth(), b.getHeight());
			}
		}
		
		public String getName()
		{
			return name;
		}
		
		public boolean isButtonClicked(String buttonName)
		{
			Button b = getButton(buttonName);
			float mouseY = HEIGHT - Mouse.getY() - 1;
			if(Mouse.getX() > b.getX() && Mouse.getX() < b.getX() + b.getWidth() &&
					mouseY > b.getY() && mouseY < b.getY() + b.getHeight()){
				return true;
			}else
				return false;
		}
		
		private Button getButton(String buttonName)
		{
			for(Button b : menuButtons)
			{
				if(b.getName().equals(buttonName))
				{
					return b;
				}
			}
			return null;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public int getOptionsHeight() {
			return optionsHeight;
		}
	}
}
