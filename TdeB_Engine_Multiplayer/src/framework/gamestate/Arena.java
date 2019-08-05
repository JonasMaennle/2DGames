package framework.gamestate;
import framework.arena.WaveManager;
import framework.core.Handler;
import framework.core.Scene2D;
import framework.core.StateManager;

public class Arena extends Scene2D{
	
	private WaveManager waveManager;
	
	public Arena(Handler handler, StateManager manager) {
		super(handler, manager);
		
		this.waveManager = new WaveManager(handler, manager);
	}
	
	public void update() {
		super.update();
		waveManager.update();
	}
	
	public void render() {
		super.render();
	}

	public WaveManager getWaveManager() {
		return waveManager;
	}

	public void setWaveManager(WaveManager waveManager) {
		this.waveManager = waveManager;
	}
}
