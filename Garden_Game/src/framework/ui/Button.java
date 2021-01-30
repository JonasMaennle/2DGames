package framework.ui;

import java.awt.Rectangle;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import static framework.helper.Graphics.*;

public class Button{

	private String name;
	private Image image;
	private int x, y, width, height;
	
	public Button(String name, Image image, int x, int y, int width, int height) {
		this.name = name;
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void draw(){
		drawQuadImageStatic(image, x, y, width, height);
	}
	
	public void changeImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
}
