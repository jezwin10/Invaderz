package Invaderz;

import javax.swing.JFrame;

public class Invaders extends JFrame implements Constants{


	public Invaders()
	{
		add(new Canvas());
		setTitle("Invaderz");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}
	
	public static void main(String[] args) 
	{
		new Invaders();
	}
}


