package framework.core;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Image;

import object.Particle;
import static framework.helper.Graphics.*;

public class ParticleManager {
	private CopyOnWriteArrayList<Particle> list;
	private Random rand;
	private CopyOnWriteArrayList<Image> orangeParticles;
	private CopyOnWriteArrayList<Image> greenParticles;
	
	public ParticleManager(){
		this.list = new CopyOnWriteArrayList<>();
		this.rand = new Random();
		// add orange particles
		this.orangeParticles = new CopyOnWriteArrayList<Image>();
		for(int i = 0; i < 8; i++) {
			orangeParticles.add(quickLoaderImage("enemy/particle_" + i));
		}	
		// add green particles
		this.greenParticles = new CopyOnWriteArrayList<Image>();
		for(int i = 0; i < 8; i++) {
			greenParticles.add(quickLoaderImage("enemy/particle_green_" + i));
		}	
	}
	
	public void addEvent(String color, int numberOfParticles, int x, int y) {	
		switch (color) {
		case "orange":
			for(int i = 0; i < numberOfParticles; i++) {
				//           Particle(int x, int y, int width, int height, float velX, float velY, float speed, Image image, float angle)
				list.add(new Particle(x, y, rand.nextInt(8) + 4, rand.nextInt(8) + 4, rand.nextFloat() - 0.5f, rand.nextFloat() - 0.5f, rand.nextInt(10) + 4, orangeParticles.get(rand.nextInt(orangeParticles.size())), rand.nextInt(25)));
			}
			break;
		case "green":
			for(int i = 0; i < numberOfParticles; i++) {
				//           Particle(int x, int y, int width, int height, float velX, float velY, float speed, Image image, float angle)
				list.add(new Particle(x, y, rand.nextInt(8) + 4, rand.nextInt(8) + 4, rand.nextFloat() - 0.5f, rand.nextFloat() - 0.5f, rand.nextInt(10) + 4, greenParticles.get(rand.nextInt(greenParticles.size())), rand.nextInt(25)));
			}
			break;
		default:
			break;
		}
	}
	
	public void update(){
		for(Particle p : list){
			if(p.isOutOfMap()){
				list.remove(p);
			}else if(p.isDead()) {
				list.remove(p);
			}else{
				p.update();
			}
		}
	}
	
	public void draw(){
		for(Particle p : list){
			p.draw();
		}
	}
}
