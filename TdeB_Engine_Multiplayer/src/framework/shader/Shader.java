package framework.shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Shader {
	
	private int programId;
	private int fragmentShader;
	private StringBuilder fragmentShaderSource;
	private float input;
	
	public Shader(float input) {
		programId = glCreateProgram();
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		this.input = input;
	}
	
	public void loadFragmentShader() {
		fragmentShaderSource = new StringBuilder();

		try {
			String line;
			InputStreamReader in = new InputStreamReader(this.getClass().getResourceAsStream("/shaders/shader.frag"));
			BufferedReader reader = new BufferedReader(in);
			while ((line = reader.readLine()) != null) 
			{
				fragmentShaderSource.append(line); // .append("\n")
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fragmentShaderSource.replace(187, 191, "" + input + "f");
		
		//System.out.println(fragmentShaderSource.toString());
		glShaderSource(fragmentShader, fragmentShaderSource);
	}
	
//	public void changeShader(float input)
//	{
//		this.input = input;
//		//System.out.println(fragmentShaderSource.substring(187, 191));
//		fragmentShaderSource.replace(187, 191, "" + this.input + "f");
//		//System.out.println(fragmentShaderSource.toString());
//		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
//		
//		programId = glCreateProgram();
//		glShaderSource(fragmentShader, fragmentShaderSource);
//		compile();
//		
//		unUse();
//	}
	
	public void compile() {
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Fragment shader not compiled!");
		}
		glAttachShader(programId, fragmentShader);
		glLinkProgram(programId);
		glValidateProgram(programId);
	}
	
	public void useProgram() {
		glUseProgram(programId);
	}
	
	public void unUse() {
		glUseProgram(0);
	}
	
	public void clean() {
		glDeleteShader(fragmentShader);
		glDeleteProgram(programId);
	}
	
	public int getProgram() {
		return programId;
	}

	public float getInput() {
		return input;
	}

	public void setInput(float input) {
		this.input = input;
	}
}
