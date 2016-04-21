package Invaderz;

import javax.swing.ImageIcon;

public class bullet extends Sprite {
	private String bullet = "res/Sprite/bullet.png";
	private final int H_SPACE = 6;
	private final int V_SPACE = 1;
	
	public bullet() {
	}
	
	public bullet(int x, int y) {
		ImageIcon ii = new ImageIcon(this.getClass().getResource(bullet));
		setImage(ii.getImage());
		setX(x + H_SPACE);
		setY(y - V_SPACE);
	}
}