package Invaderz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Canvas extends JPanel implements Runnable, Constants {

	private Dimension d;
	private ArrayList aliens;
	private player player;
	private bullet bullet;
	
	private int alienX = 150;
	private int alienY = 5;
	private int direction = -1;
	private int deaths = 0;
	
	private boolean ingame = true;
	private final String alien = "res/Sprite/alien.png";
	private String message = "Game Over";
	
	private Thread animator;
	
	public Canvas()
	{
		addKeyListener(new TAdapter());
		setFocusable(true);
		d = new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
		setBackground(Color.black);
		
		gameInit();
		setDoubleBuffered(true);
	}
	
	public void addNotify() {
		super.addNotify();
		gameInit();
	}
	
	public void gameInit() {
		aliens = new ArrayList();
		ImageIcon ii = new ImageIcon(this.getClass().getResource(alien));
		
		for (int i=0; i < 4; i++) {
			for (int j=0; j < 6; j++) {
				Alien alien = new Alien(alienX + 18*j, alienY + 18*i);
				alien.setImage(ii.getImage());
				aliens.add(alien);
			}
		}
		
		player = new player();
		bullet = new bullet();
		
		if (animator == null || !ingame) {
			animator = new Thread(this);
			animator.start();
		}
	}
	
	public void drawAliens(Graphics g)
	{
		Iterator it = aliens.iterator();
		
		while (it.hasNext()) {
			Alien alien = (Alien) it.next();
			
			if (alien.isVisible()) {
				g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
			}
			
			if (alien.isDying()) {
				alien.die();
			}
		}
	}
	
	public void drawPlayer(Graphics g) {
		if (player.isVisible()) {
			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
		}
		
		if (player.isDying()) {
			player.die();
			ingame = false;
		}
	}
	
	public void drawBullet(Graphics g) {
		if (bullet.isVisible())
			g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
	}
	
	public void drawBomb(Graphics g) {
		Iterator i3 = aliens.iterator();
		
		while(i3.hasNext()) {
			Alien a = (Alien) i3.next();
			
			Alien.Bomb b = a.getBomb();
			
			if (!b.isDestroyed()) {
				g.drawImage(b.getImage(), b.getX(), b.getY(), this);
			}
		}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.green);
		
		if (ingame) {
			g.drawLine(0,  GROUND,  CANVAS_WIDTH,  GROUND);
			drawAliens(g);
			drawPlayer(g);
			drawBullet(g);
			drawBomb(g);
		}
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
	
	public void gameOver()
	{
		Graphics g = this.getGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		
		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, CANVAS_WIDTH/2 - 30, CANVAS_WIDTH-100, 50);
		g.setColor(Color.white);
		g.drawRect(50,  CANVAS_WIDTH/2 - 30,  CANVAS_WIDTH-100, 50);
		
		Font small = new Font("Helvatica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);
		
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message,  (CANVAS_WIDTH - metr.stringWidth(message))/2, CANVAS_WIDTH/2);
	}
	
	public void animationCycle() {
		if (deaths == NUMBER_OF_ALIENS) {
			ingame = false;
			message = "Game Won!";
		}
		
		//player
		
		player.act();
		
		//bullet
		if (bullet.isVisible()) {
			Iterator it = aliens.iterator();
			int bulletX = bullet.getX();
			int bulletY = bullet.getY();
			
			while (it.hasNext()) {
				Alien alien = (Alien) it.next();
				int alienX = alien.getX();
				int alienY = alien.getY();
				
				if (alien.isVisible () && bullet.isVisible()) {
					if (bulletX >= (alienX) && bulletX <= (alienX + ALIEN_WIDTH) && bulletY >= (alienY) && bulletY <= (alienY+ALIEN_HEIGHT) )
					{						
						alien.setDying(true);
						deaths++;
						bullet.die();
					}
				}
			}
			
			int y = bullet.getY();
			y -= 4;
			if (y < 0)
				bullet.die();
			else bullet.setY(y);
		}
		
		//aliens
		
		Iterator it1 = aliens.iterator();
		
		while (it1.hasNext()) {
			Alien a1 = (Alien) it1.next();
			int x = a1.getX();
			
			if (x >= CANVAS_WIDTH - CANVAS_RIGHT && direction != -1) {
				direction = -1;
				
				Iterator i2 = aliens.iterator();
				while (i2.hasNext()) {
					Alien a = (Alien)i2.next();
					a.setY(a.getY() + GO_DOWN);
				}
			}
		}
		
		Iterator it = aliens.iterator();
		
		while (it.hasNext()) {
			while (it.hasNext()) {
				Alien alien = (Alien) it.next();
				if (alien.isVisible()) {
					
					int y = alien.getY();
					
					if (y > GROUND - ALIEN_HEIGHT) {
						ingame = false;
						message = "They're coming!";
					}
					alien.act(direction);
				}
			}
			
			//bombs
			
			Iterator i3 = aliens.iterator();
			Random generator = new Random();
			
			while (i3.hasNext()) {
				int bullet = generator.nextInt(15);
				Alien a = (Alien) i3.next();
				Alien.Bomb b = a.getBomb();
				if (bullet == CHANCE && a.isVisible() && b.isDestroyed()) {
					b.setDestroyed(false);
					b.setX(a.getX());
					b.setY(a.getY());
				}
				
				int bombX = b.getX();
				int bombY = b.getY();
				int playerX = player.getX();
				int playerY = player.getY();
				
				if (player.isVisible() && !b.isDestroyed()) {
					if(bombX >= (playerX) && bombX <= (playerX+PLAYER_WIDTH) && bombY >= (playerY) && bombY <= (playerY+PLAYER_HEIGHT) ) {
						player.setDying(true);
						b.setDestroyed(true);
					}
				}
				
				if (!b.isDestroyed()) {
					b.setY(b.getY() + 1);
					if (b.getY() >= GROUND - BOMB_HEIGHT) {
						b.setDestroyed(true);
					}
				}
			}
		}
			
	}
		
		public void run() {
			long beforeTime, timeDiff, sleep;
			
			beforeTime = System.currentTimeMillis();
			
			while (ingame) {
				repaint();
				animationCycle();
				
				timeDiff = System.currentTimeMillis() - beforeTime;
				sleep = DELAY - timeDiff;
				
				if (sleep < 0)
					sleep = 2;
				try {
					Thread.sleep(sleep);
				}
				catch (InterruptedException e) {
					System.out.println("Interrupted");
				}
				beforeTime = System.currentTimeMillis();
				}
			gameOver();
			}
		
		private class TAdapter extends KeyAdapter {
			public void keyReleased(KeyEvent e) {
				player.keyReleased(e);
			}
			
			public void keyPressed(KeyEvent e) {
				player.keyPressed(e);
				
				int x = player.getX();
				int y = player.getY();
				
				if (ingame)
				{
					if (e.isAltDown()) {
						if (!bullet.isVisible())
						bullet = new bullet(x, y);
				}
			}
		}
	}
}