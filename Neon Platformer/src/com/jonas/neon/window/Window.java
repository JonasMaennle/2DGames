package com.jonas.neon.window;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Window {
	
	public static int width;
	public static int height;

	public Window(String title, Game game)
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		width = (int) tk.getScreenSize().getWidth();
		height = (int) tk.getScreenSize().getHeight();
		game.setPreferredSize(new Dimension(width, height));
		game.setMaximumSize(new Dimension(width, height));
		game.setMinimumSize(new Dimension(width, height));
		
		JFrame frame = new JFrame(title);
		frame.setUndecorated(true);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
}
