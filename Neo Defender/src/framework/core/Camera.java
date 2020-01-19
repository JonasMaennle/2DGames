package framework.core;

import static framework.helper.Collection.*;

import org.lwjgl.input.Keyboard;

import framework.helper.Collection;

public class Camera {

	
	public Camera(){
		reset();
	}
	
	public void update(){
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) 
			if(MOVEMENT_OFFSET > -32) MOVEMENT_OFFSET -= 1;


		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			if(MOVEMENT_OFFSET < 32) MOVEMENT_OFFSET += 1;
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) 
			MOVEMENT_OFFSET *= 0.9f;
		
		
		MOVEMENT_X += Collection.MOVEMENT_OFFSET * 0.75f; // * x.x -> smoothnes factor
		MOVEMENT_Y += 0;
	}

	public void reset(){
		MOVEMENT_X = 0;
		MOVEMENT_Y = 0;
	}
}
