package framework.arena;

import java.io.Serializable;

public class ScoreEntry implements Serializable{
	
	private static final long serialVersionUID = 5430435011806131606L;
	private int wave;
	private String name;
	
	public ScoreEntry(int wave, String name) {
		this.wave = wave;
		this.name = name;
	}

	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return ("Wave: " + wave + "\tName: " + name);
	}
}
