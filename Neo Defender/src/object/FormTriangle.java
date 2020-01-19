package object;

import org.newdawn.slick.Image;

public class FormTriangle extends Form{

	public FormTriangle(int x, int y, int width, int height, Image image, float velX) {
		super(x, y, width, height, image, velX);

	}
	
	@Override
	public void update() {
		x += velX * speed;
	}
	
	@Override
	public void draw() {
		super.draw();
	}
}
