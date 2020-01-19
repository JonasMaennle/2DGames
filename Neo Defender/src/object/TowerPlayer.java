package object;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Image;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

public class TowerPlayer extends Tower{
	
	private CopyOnWriteArrayList<Form> forms; 
	private Image triangleImage;
	private boolean toggelSpawn;

	public TowerPlayer(int x, int y) {
		super(x, y);
		this.forms = new CopyOnWriteArrayList<Form>();
		this.triangleImage = quickLoaderImage("form/player_0");
		this.toggelSpawn = false;
	}

	@Override
	public void update() {
		
		// spawn triangle
		if(Keyboard.isKeyDown(Keyboard.KEY_X) && toggelSpawn) {
			forms.add(new FormTriangle(x, y, TILE_SIZE, TILE_SIZE, triangleImage, 1));
			toggelSpawn = false;
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_X))
			toggelSpawn = true;

		
		for(Form form : forms) {
			form.update();
		}
	}
	
	@Override
	public void draw() {
		for(Form form : forms) {
			form.draw();
		}
	}
	
	public CopyOnWriteArrayList<Form> getForms(){
		return forms;
	}
}
