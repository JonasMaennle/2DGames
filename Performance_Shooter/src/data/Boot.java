package data;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import Gamestate.StateManager;
import helpers.Clock;
import static helpers.Setup.*;

public class Boot {
	
	public Boot()
	{
		Mouse.setGrabbed(true);
		beginSession();
		StateManager statemanager = new StateManager();
		
		// Game loop
		while(!Display.isCloseRequested())
		{
			Clock.update();	
			statemanager.update();
			
			Display.update();
			Display.sync(60);
		}	
		Display.destroy();
	}

	public static void main(String[] args) 
	{
		new Boot();
	}
}