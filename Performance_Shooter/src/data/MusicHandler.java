package data;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import Gamestate.StateManager;
import Gamestate.StateManager.GameState;

public class MusicHandler {
	
	// endor music
	private Music endor_Theme;
	
	// horth music
	
	// space music
	
	public MusicHandler()
	{
		try {
			this.endor_Theme = new Music("sound/endor_Theme.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		//endor_Theme.play();
	}
	
	public void update()
	{
		if(StateManager.gameState == GameState.GAME)
		{
			switch (StateManager.CURRENT_LEVEL) {
			// endor levels
			case 1:
			case 2:
			case 3:
				if(!endor_Theme.playing())
					endor_Theme.resume();
				break;
			// hoth levels
			case 4:
				endor_Theme.stop();
				break;

			default:
				break;
			}
			
		}else if(StateManager.gameState == GameState.LOADING || StateManager.gameState == GameState.MAINMENU)
		{
			switch (StateManager.CURRENT_LEVEL) {
			// endor levels
			case 1:
			case 2:
			case 3:
				if(endor_Theme.playing())
					endor_Theme.pause();
				break;
			// hoth levels
			case 4:
				endor_Theme.stop();
				break;

			default:
				break;
			}
		}
	}
}
