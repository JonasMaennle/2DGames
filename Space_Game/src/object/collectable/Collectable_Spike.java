package object.collectable;

import static framework.helper.Graphics.*;

import org.newdawn.slick.Image;

public class Collectable_Spike extends Collectable_Basic{
	
	private Image decay0, decay1, decay2;
	private int healthPoints;

	public Collectable_Spike(float x, float y, int width, int height) {
		super(x, y, width, height);
		this.image = quickLoaderImage("player/metal_spike");
		this.decay0 = quickLoaderImage("player/metal_spike_decay_0");
		this.decay1 = quickLoaderImage("player/metal_spike_decay_1");
		this.decay2 = quickLoaderImage("player/metal_spike_decay_2");
		
		this.healthPoints = 100;
	}
	
	public void update() {
		
	}
	
	public void draw() {
		if(healthPoints > 75) {
			if(player == null)
				drawQuadImage(image, x, y, width, height);
			else
				drawQuadImageRotCenter(image, player.getX(), player.getY(), width, height, player.getPlayerRotation());
		}else if(healthPoints > 50) {
			drawQuadImageRotCenter(decay0, player.getX(), player.getY(), width, height, player.getPlayerRotation());
		}else if(healthPoints > 25) {
			drawQuadImageRotCenter(decay1, player.getX(), player.getY(), width, height, player.getPlayerRotation());
		}else if(healthPoints > 0) {
			drawQuadImageRotCenter(decay2, player.getX(), player.getY(), width, height, player.getPlayerRotation());
		}
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}
}
