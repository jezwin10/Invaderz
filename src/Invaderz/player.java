package Invaderz;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class player extends Sprite implements Constants{
	//starting coordinates of the player
	private final int START_Y = 250;
	private final int START_X = 640;
	
	private final String player = "res/Sprite/ship.png";
	private int width;
	
	public player() 
	{
		ImageIcon ii = new ImageIcon(this.getClass().getResource(player));
		
		width = ii.getImage().getWidth(null);
		
		setImage(ii.getImage());
		setY(START_Y);
		setX(START_X);
	}
	
	public void act() 
	{
		x += dx;
		if (x <= 2)
			x = 2;
		if (x >= CANVAS_WIDTH - 2*width)
			x = CANVAS_WIDTH - 2*width;
	}
	
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_LEFT)
		{
			dx = -2;
		}
		
		if (key == KeyEvent.VK_RIGHT)
		{
			dx = 2;
		}
	}
	
	public void keyReleased(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_LEFT)
		{
			dx = 0;
		}
		
		if (key == KeyEvent.VK_RIGHT)
		{
			dx = 0;
		}
	}
}
