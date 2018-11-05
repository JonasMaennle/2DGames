package data;

import org.lwjgl.opengl.Display;

import helpers.Clock;
import helpers.StateManager;

import static helpers.Artist.*;

public class Boot {
	
	public Boot()
	{
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