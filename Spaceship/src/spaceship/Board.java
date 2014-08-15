package spaceship;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Timer timer;
	private boolean gameRunning;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Rock> rocks = new ArrayList<Rock>();
	private Date lastRockCreationTime = new Date();
	private Date lastBulletFire = new Date();
	private Ship ship = new Ship(Spaceship.screenWidth, Spaceship.screenHeight);
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean isFiring = false;
	private Image background, rockImage, shipImage;
	private ArrayList<Image> explosion = new ArrayList<Image>();
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	private int score = 0;
	private Date gameStartTime = new Date();
	private int firingRate = 250;
	private int rockCreatingRate = 500;
	
	public Board() {
		initGame();
	}
	
	private void initGame() {
		// Start listening for key input
		addKeyListener(new TAdapter());
		
		setFocusable(true);
		setDoubleBuffered(true);
		setBackground(Color.black);
		
		// Load up essentials images into RAM
		background = new ImageIcon(getClass().getClassLoader().getResource("resources/starfield.png")).getImage();
		rockImage = new ImageIcon(getClass().getClassLoader().getResource("resources/asteroid.png")).getImage();
		shipImage = new ImageIcon(getClass().getClassLoader().getResource("resources/ship.png")).getImage();
		
		// Load up explosion images into RAM for later use in animation
		for(int i = 0; i <= 17; i++) {
			explosion.add(new ImageIcon(getClass().getClassLoader().getResource("resources/explosion2/ex"+i+".png")).getImage());
		}
		
		gameRunning = true;
		
		// Create an start the game loop (not the best way - should be fixed)
		timer = new Timer(30, this);
		timer.start();
	}
	
	private void processGameLogic() {
		Date now = new Date();
		
		int sizeOfRocks = rocks.size();
		for(int i = 0; i < sizeOfRocks; i++) {
			rocks.get(i).move(); // Move each rock
			
			// If rock is outside of the screen, remove it
			if(rocks.get(i).getLocationY() > this.getHeight()) {
				rocks.remove(i);
				sizeOfRocks--;
			}
		}
		
		// If enough time has elapsed, create a new rock
		if(now.getTime() - lastRockCreationTime.getTime() > rockCreatingRate) {
			// lets create a new rock
			rocks.add(new Rock(this.getWidth(), this.getHeight()));
			
			// update last rock creating time
			lastRockCreationTime = now;
		}
		
		// Move the ship if keys are active
		if(moveUp)
			ship.moveUp();
		
		if(moveDown)
			ship.moveDown();
		
		if(moveLeft)
			ship.moveLeft();
		
		if(moveRight)
			ship.moveRight();
		
		// Check ship-rock collision
		for(Rock rock : rocks) {
			Dimension rockDimension = new Dimension(rock.getLocationX(), rock.getLocationY(), rock.getSize(), rock.getSize());
			Dimension shipDimension = new Dimension(ship.getLocationX(), ship.getLocationY(), ship.getSize(), ship.getSize());
			
			if(shipDimension.anyPointIsInside(rockDimension)) {
				gameRunning = false;
			}
		}
		
		// Fire a bullet if enough time has elapsed
		if(now.getTime() - lastBulletFire.getTime() > firingRate && isFiring) {
			bullets.add(new Bullet((int)(ship.getLocationX() + (int)(ship.getSize() / 2)), ship.getLocationY()));
			lastBulletFire = now;
		}
		
		// Handle explosion animations
		int sizeOfExplosions = explosions.size();
		for(int i = 0; i < sizeOfExplosions; i++) {
			Explosion explosion = explosions.get(i);
			
			
			if(explosion.isDone()) { // If animation is complete remove the instance
				explosions.remove(i);
				sizeOfExplosions--;
				i--;
			} else { // If animation is in progress, advance the step
				explosion.stepInto();
			}
		}
		
		// Handle bullet movements, collisions and endings
		int sizeOfBullets = bullets.size();
		for(int i = 0; i < sizeOfBullets; i++) {
			Bullet bullet = bullets.get(i);
			Dimension bulletDimension = new Dimension(bullet.getLocationX(), bullet.getLocationY(), 1, bullet.getLength());
			
			bullet.move();
			
			boolean removeBullet = false;
			
			// Check current bullet for collisions
			for(int j = 0; j < rocks.size(); j++) {
				Rock rock = rocks.get(j);
				Dimension rockDimension = new Dimension(rock.getLocationX(), rock.getLocationY(), rock.getSize(), rock.getSize());
				
				if(bulletDimension.anyPointIsInside(rockDimension)) {
					// create an explosion animation
					explosions.add(new Explosion(rock.getLocationX(), rock.getLocationY(), rock.getSize()));
					
					// remove the rock
					rocks.remove(j);
					
					// remove the bullet
					removeBullet = true;
					
					// add score o
					score += 50;
				}
			}
			
			// Check bullet ending
			if(!removeBullet) {
				if(bullet.getLocationY() + bullet.getLength() < 0) {
					removeBullet = true;
				}
			}
			
			if(removeBullet) {
				bullets.remove(i);
				sizeOfBullets--;
				i--;
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background, 0, 0, Spaceship.screenWidth, Spaceship.screenHeight, null);
		
		if(gameRunning) {
			
			for(Rock rock : rocks) { // draw all rocks
				g.drawImage(rockImage, rock.getLocationX(), rock.getLocationY(), rock.getSize(), rock.getSize(), null);
			}
			
			for(Explosion explosion : explosions) { // draw all explosions
				g.drawImage(this.explosion.get(explosion.getAnimationStep()), explosion.getLocationX(), explosion.getLocationY(), explosion.getSize(), explosion.getSize(), null);
			}
			
			for(Bullet bullet : bullets) { // draw all bullets
				g.setColor(Color.white);
				g.drawLine(bullet.getLocationX(), bullet.getLocationY(), bullet.getLocationX(), bullet.getLocationY() + bullet.getLength());
			}
			
			// Draw the ship
			g.drawImage(shipImage, ship.getLocationX(), ship.getLocationY(), ship.getSize(), ship.getSize(), null);
			g.setColor(Color.white);
		} else {
			// display game over text on center of the screen
			String txt = "GAME OVER";
			
			g.setColor(Color.blue);
			g.setFont(new Font("Helvetica", Font.PLAIN, 48));
			
			int txtWidth = (int)g.getFontMetrics().getStringBounds(txt, g).getWidth();  
	        int startX = Spaceship.screenWidth/2 - txtWidth/2;
			
	        int txtHeight = (int)g.getFontMetrics().getStringBounds(txt, g).getHeight();
	        int startY = Spaceship.screenHeight/2 - txtHeight/2;
	        
			g.drawString(txt, startX, startY);
			
			timer.stop();
		}
		
		// draw score
		String score = "SCORE: " + this.score;
		
		g.setColor(Color.blue);
		g.setFont(new Font("Helvetica", Font.PLAIN, 18));
		
		int scoreWidth = (int)g.getFontMetrics().getStringBounds(score, g).getWidth();
		int startX = Spaceship.screenWidth - scoreWidth - 10;
		int startY = 20;
		
		g.drawString(score, startX, startY);
		
		g.drawString("Time elapsed: " + (int)((new Date().getTime() - gameStartTime.getTime()) / 1000) + " seconds", 10, 20);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(gameRunning) {
			processGameLogic();
			repaint();
		} else {
			timer.stop();
		}
	}
	
	class TAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT) {
				moveRight = false;
				moveLeft = true;
			} else if(key == KeyEvent.VK_RIGHT) {
				moveLeft = false;
				moveRight = true;
			} else if(key == KeyEvent.VK_UP) {
				moveDown = false;
				moveUp = true;
			} else if(key == KeyEvent.VK_DOWN) {
				moveUp = false;
				moveDown = true;
			} else if(key == KeyEvent.VK_F) {
				isFiring = true;
			}
			
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT) {
				moveLeft = false;
			} else if(key == KeyEvent.VK_RIGHT) {
				moveRight = false;
			} else if(key == KeyEvent.VK_UP) {
				moveUp = false;
			} else if(key == KeyEvent.VK_DOWN) {
				moveDown = false;
			} else if(key == KeyEvent.VK_F) {
				isFiring = false;
			}
		}
	}
	
}
