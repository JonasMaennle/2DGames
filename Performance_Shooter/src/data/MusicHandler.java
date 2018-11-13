package data;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import Gamestate.StateManager;
import Gamestate.StateManager.GameState;

public class MusicHandler {
	
	// endor music
	private Music endor_Theme;
	private String currentMusic;
	
	// horth music
	
	// space music
	
	public MusicHandler()
	{
		this.currentMusic = "endor";
		try {
			this.endor_Theme = new Music("sound/endor_Theme.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		if(StateManager.gameState != GameState.MAINMENU)
		{
			switch (currentMusic) {
			case "endor":
				if(!endor_Theme.playing())
					//endor_Theme.play();
				break;

			default:
				break;
			}
		}
	}

	public String getCurrentMusic() {
		return currentMusic;
	}

	public void setCurrentMusic(String currentMusic) {
		this.currentMusic = currentMusic;
	}
}
