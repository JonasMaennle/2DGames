package framework.shader;

import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;

public class Light implements Serializable{

	private static final long serialVersionUID = 6244870893102900385L;
	public Vector2f location;
	public float red;
	public float green;
	public float blue;
	private float radius;
	private Shader shader;

	public Light(Vector2f location, float red, float green, float blue, float radius) {
		this.location = location;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.radius = radius;
		
		initNewShader(radius);
	}
	
	private void initNewShader(float lightSize){
		shader = new Shader(lightSize);
		shader.loadFragmentShader();
		shader.compile();

		glEnable(GL_STENCIL_TEST);
		glClearColor(0, 0, 0, 0);
	}
	
	public void setLocation(Vector2f location) {
		this.location = location;
	}
	
	public Vector2f getLocation() {
		return location;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public void setRadius(float radius){
		this.radius = radius;
		initNewShader(radius);
	}

	public Shader getShader() {
		return shader;
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}
}

