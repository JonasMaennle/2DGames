package com.jonas.neon.framework;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
	public Sound()
	{
		
	}
	
	public void play(int current)
	{
		// one case for each sound in the game
		
		switch (current) {
		case 0:
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream in = AudioSystem.getAudioInputStream(getClass().getResource("/sound/blaster_sound.wav"));
				clip = AudioSystem.getClip();
				clip.open(in);
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		case 1:
			
			try {
				AudioInputStream in = AudioSystem.getAudioInputStream(getClass().getResource("/sound/AT_ST_sound.wav"));
				Clip clip = AudioSystem.getClip();
				clip.open(in);
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}	
			break;

		default:
			break;
		}
	}
}
