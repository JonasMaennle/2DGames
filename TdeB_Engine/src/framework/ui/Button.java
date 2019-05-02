package framework.ui;

import org.newdawn.slick.Image;

public class Button {

	private String name;
	private Image image;
	private int x, y, width, height;
	
	public Button(String name, Image image, int x, int y, int width, int height)
	{
		this.name = name;
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Button(String name, Image image, int x, int y)
	{
		this.name = name;
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
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
}
