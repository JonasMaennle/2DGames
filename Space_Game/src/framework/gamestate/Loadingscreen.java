package framework.gamestate;

import static framework.helper.Collection.*;

import org.newdawn.slick.Color;

import framework.core.BackgroundHandler;
import framework.core.GameScreen;
import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.ui.HeadUpDisplay;

public class Loadingscreen extends GameScreen{

	private HeadUpDisplay hud;
	
	public Loadingscreen(StateManager stateManager, Handler handler, BackgroundHandler backgroundHandler) {
		super(stateManager, handler, backgroundHandler);

		this.hud = new HeadUpDisplay(handler, 24, stateManager);
	}


	public void update() {
		super.update();


		StateManager.gameState = GameState.GAME;
	}

	public void render() {
		super.render();
		hud.drawString(WIDTH / 2 - 64, HEIGHT / 2 - 12, "loading...", new Color(255, 255, 255));
	}
}
