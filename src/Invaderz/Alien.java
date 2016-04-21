//class for the alien sprite. an alien has a bomb class in it

package Invaderz;

import javax.swing.ImageIcon;

public class Alien extends Sprite 
	{
	private Bomb bomb;
	private final String shot = "res/Sprite/alien.png";
	
	public Alien(int x, int y) 
	{
		this.x = x;
		this.y = y;
		
		bomb = new Bomb(x, y);
		ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
		setImage(ii.getImage());
		
	}
	
	public void act(int direction) { //used to position alien in horizontal direction
		this.x += direction;
	}
	
	public Bomb getBomb() { //called when bomb is about to be dropped
		return bomb;
	}
	
	public class Bomb extends Sprite
	{
		
		private final String bomb = "res/Sprite/bomb.png";
		private boolean destroyed;
		
		public Bomb(int x, int y) 
		{
			setDestroyed(true);
			this.x = x;
			this.y = y;
			ImageIcon ii = new ImageIcon(this.getClass().getResource(bomb));
			setImage(ii.getImage());
		}
		
		public void setDestroyed(boolean destroyed) 
		{
			this.destroyed = destroyed;
		}
		
		public boolean isDestroyed() 
		{
			return destroyed;
		}
	}
}