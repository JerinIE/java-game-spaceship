package spaceship;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.function.Predicate;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import quicktime.std.clocks.RateCallBack;
import spaceship.GameObject.Direction;
import sun.misc.Queue;

public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private int width;
	private int height;
	
	// Controls being pressed (movement)
	private boolean moveLeft, moveRight, moveUp, moveDown, isFiring;
	private Queue<Integer> menuControlsBuffer;
	
	// Environment variables
	private Date lastBulletFiredAt, lastRockCreatedAt, gameStartedAt;
	private int score, bulletFiringRate, rockCreatingRate;
	
	// Game objects
	private Ship ship;
	private ArrayList<Rock> rocks;
	private ArrayList<Bullet> bullets;
	private ArrayList<Explosion> explosions;
	
	// Resource variables
	private Image rock32Image, rock48Image, rock64Image, backgroundImage, shipImage;
	private ObjectBounds rock32Bounds, rock48Bounds, rock64Bounds, shipBounds;
	private ArrayList<Image> explosionImages = new ArrayList<Image>();
	
	private int gameState = 0;
	private int selectedMenuItem = 0;
	
	private ArrayList<Long> gameTicks = new ArrayList<Long>();
	
	private Timer timer;
	
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		
		// Start listening for key input
		addKeyListener(new TAdapter());
		
		// Prepare JPanel
		setFocusable(true);
		setDoubleBuffered(true);
		setBackground(Color.black);
		setSize(width, height);
		setPreferredSize(new java.awt.Dimension(width, height));
		setVisible(true);
		
		System.out.println("Panel width: " + getWidth() + ", height: " + getHeight());
		
		// Load all resources into RAM
		loadResources();
		
		// Set variables to their default values
		resetGame();
		
		// Start the game loop - it goes on forever!!! :D (should work like 30FPS but needs improvement)
		timer = new Timer(33, this);
		timer.start();
	}
	
	private void loadResources() {
		ClassLoader classLoader = getClass().getClassLoader();
		
		// Load images
		rock32Image = new ImageIcon(classLoader.getResource("resources/rock32.png")).getImage();
		rock48Image = new ImageIcon(classLoader.getResource("resources/rock48.png")).getImage();
		rock64Image = new ImageIcon(classLoader.getResource("resources/rock64.png")).getImage();
		backgroundImage = new ImageIcon(classLoader.getResource("resources/starfield.png")).getImage();
		shipImage = new ImageIcon(classLoader.getResource("resources/ship64.png")).getImage();
		
		for(int i = 0; i <= 17; i++) {
			explosionImages.add(new ImageIcon(classLoader.getResource("resources/explosion/ex"+i+".png")).getImage());
		}
		
		// Load bounds for images
		rock32Bounds = ObjectBounds.parseFromFile(classLoader.getResource("resources/rock32.txt"));
		rock48Bounds = ObjectBounds.parseFromFile(classLoader.getResource("resources/rock48.txt"));
		rock64Bounds = ObjectBounds.parseFromFile(classLoader.getResource("resources/rock64.txt"));
		shipBounds = ObjectBounds.parseFromFile(classLoader.getResource("resources/ship64.txt"));
	}
	
	private void resetGame() {
		// Reset movements
		moveLeft = false;
		moveRight = false;
		moveUp = false;
		moveDown = false;
		
		// Re-created game objects
		ship = new Ship(shipImage, shipBounds, width, height);
		rocks = new ArrayList<Rock>();
		bullets = new ArrayList<Bullet>();
		explosions = new ArrayList<Explosion>();
		
		// Reset environment variables
		score = 0;
		bulletFiringRate = 250;
		rockCreatingRate = 500;
		
		// Re-create timings
		gameStartedAt = new Date();
		lastBulletFiredAt = new Date();
		lastRockCreatedAt = new Date();
		lastRockCreatedAt.setTime(lastRockCreatedAt.getTime() - 15000);
		
		selectedMenuItem = 0;
		menuControlsBuffer = new Queue<Integer>();
	}
	
	private void doLogic() {
		if(gameState == 0) { // Main Menu
			doMenuLogic();
		} else if(gameState == 1) { // Game Play
			doGameLogic();
		} else { // Game Over
			doGameOverLogic();
		}
	}
	
	private void doMenuLogic() {
		if(menuControlsBuffer.isEmpty()) {
			return;
		}
		
		int key = 0;
		
		try {
			key = menuControlsBuffer.dequeue();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(key == KeyEvent.VK_UP) {
			if(selectedMenuItem == 0) {
				selectedMenuItem = 2;
			} else {
				selectedMenuItem--;
			}
		} else if(key == KeyEvent.VK_DOWN) {
			if(selectedMenuItem == 2) {
				selectedMenuItem = 0;
			} else {
				selectedMenuItem++;
			}
		} else if(key == KeyEvent.VK_ENTER) {
			if(selectedMenuItem == 0) { // Start button
				resetGame();
				gameState = 1;
			} else if(selectedMenuItem == 1) { // Settings button
				// TODO: Create the settings menu
			} else { // Exit button
				timer.stop();
				//Spaceship.spaceship.dispose();
			}
		} 
	}
	
	private void doGameLogic() {
		handleObjectCreations();
		handleObjectMovements();
		handleObjectCollisions();
		handleObjectEndings();
		handleExplosions();
	}

	private void handleObjectCreations() {
		Date now = new Date();
		
		// Create new rock if possible
		if(now.getTime() - lastRockCreatedAt.getTime() > rockCreatingRate) {
			int rnd = GameObject.randomWithRange(2, 4);
			Image imageToPass = null;
			ObjectBounds boundsToPass = null;
			
			
			if(rnd == 2) {
				imageToPass = rock32Image;
				boundsToPass = (ObjectBounds)rock32Bounds.clone();
			} else if(rnd == 3) {
				imageToPass = rock48Image;
				boundsToPass = (ObjectBounds)rock48Bounds.clone();
			} else if(rnd == 4) {
				imageToPass = rock64Image;
				boundsToPass = (ObjectBounds)rock64Bounds.clone();
			}
			
			rocks.add(new Rock(rnd, imageToPass, boundsToPass, width, height));
			
			lastRockCreatedAt = now;
		}
		
		// Create a bullet if possible
		if(now.getTime() - lastBulletFiredAt.getTime() > bulletFiringRate && isFiring) {
			bullets.add(new Bullet((int)(ship.getX() + (ship.getWidth() / 2)), ship.getY(), this.width, this.height));
			
			lastBulletFiredAt = now;
		}
	}
	
	private void handleObjectMovements() {
		for(Rock rock : rocks) {
			rock.advanceY(Direction.DOWN);
		}
		
		for(Bullet bullet : bullets) {
			bullet.advanceY(Direction.UP);
		}
		
		if(moveUp) {
			ship.advanceY(GameObject.Direction.UP);
		}
		
		if(moveDown) {
			ship.advanceY(GameObject.Direction.DOWN);
		}
		
		if(moveLeft) {
			ship.advanceX(GameObject.Direction.LEFT);
		}
		
		if(moveRight) {
			ship.advanceX(GameObject.Direction.RIGHT);
		}
	}
	
	private void handleObjectCollisions() {
		for(Rock rock : rocks) {
			if(rock.getBounds().isInside(ship.getBounds())) {
				// Collision with ship detected
				gameState = 2;
				break;
			}
			
			for(Bullet bullet : bullets) {
				if(rock.getBounds().isInside(bullet.getBounds())) {
					rocks.remove(rock);
					bullets.remove(bullet);
					explosions.add(new Explosion(rock.getX(), rock.getY(), rock.getWidth()));
					handleObjectCollisions();
					return;
				}
			}
		}
	}
	
	private void handleObjectEndings() {
		for(Rock rock : rocks) {
			if(rock.isOutsideScreen(Direction.DOWN)) {
				rocks.remove(rock);
				handleObjectEndings();
				return;
			}
		}
		
		for(Bullet bullet : bullets) {
			if(bullet.isOutsideScreen(Direction.UP)) {
				bullets.remove(bullet);
				handleObjectEndings();
				return;
			}
		}
	}
	
	private void handleExplosions() {
		handleExplosionRemoval();
		
		for(Explosion explosion : explosions) {
			explosion.stepInto();
		}
	}
	
	private void handleExplosionRemoval() {
		for(Explosion explosion : explosions) {
			if(explosion.isDone()) {
				explosions.remove(explosion);
				handleExplosionRemoval();
				return;
			}
		}
	}
	
	private void doGameOverLogic() {
		//TODO: Create GameOver logic
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(backgroundImage, 0, 0, width, height, null);
		
		if(gameState == 0) { // Main Menu
			paintMenu(g);
		} else if(gameState == 1) { // Game Play
			paintGame(g);
		} else { // Game Over
			paintGameOver(g);
		}
	}
	
	private void paintMenu(Graphics g) {
		
		String menuStartName= "START";
		Rectangle2D menuStartBounds = g.getFontMetrics().getStringBounds(menuStartName, g);
		String menuSettingsName = "SETTINGS";
		Rectangle2D menuSettingsBounds = g.getFontMetrics().getStringBounds(menuSettingsName, g);
		String menuExitName = "QUIT";
		Rectangle2D menuExitBounds = g.getFontMetrics().getStringBounds(menuExitName, g);
		
		int padding = 30;
		int middleY = (int)(height / 2);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Helvetica", Font.PLAIN, 24));
		
		int startX = (int)((width / 2) - (menuStartBounds.getWidth()));
		int startY = middleY - padding - (int)(menuStartBounds.getHeight());
		int settingsX = (int)((width / 2) - (menuSettingsBounds.getWidth()));
		int settingsY = middleY - (int)(menuSettingsBounds.getHeight() / 2);
		int exitX = (int)((width / 2) - (menuExitBounds.getWidth()));
		int exitY = middleY + padding;
		
		// Display START button
		g.drawString(menuStartName, startX, startY);
		
		// Display SETTINGS button
		g.drawString(menuSettingsName, settingsX, settingsY);
		
		// Display EXIT button
		g.drawString(menuExitName, exitX, exitY);
		
		// Draw a line under the selected menu item
		if(selectedMenuItem == 0) {
			g.drawLine(startX, startY+5, startX + (int)menuStartBounds.getWidth()*2, startY+5);
		} else if(selectedMenuItem == 1) {
			g.drawLine(settingsX, settingsY+5, settingsX + (int)menuSettingsBounds.getWidth()*2, settingsY+5);
		} else if(selectedMenuItem == 2) {
			g.drawLine(exitX, exitY+5, exitX + (int)menuExitBounds.getWidth()*2, exitY+5);
		}
	}
	
	private void paintGame(Graphics g) {
		for(Rock rock : rocks) { // draw all rocks
			g.drawImage(rock32Image, rock.getX(), rock.getY(), rock.getWidth(), rock.getHeight(), null);
			
			//g.setColor(Color.WHITE);
			//for(Point point : rock.getBounds().getPoints()) {
			//	g.drawLine(point.getX()+rock.getX(), point.getY()+rock.getY(), point.getX()+rock.getX(), point.getY()+rock.getY());
			//}
		}
		
		for(Explosion explosion : explosions) {
			g.drawImage(explosionImages.get(explosion.getAnimationStep()), explosion.getX(), explosion.getY(), explosion.getSize(), explosion.getSize(), null);
		}
		
		for(Bullet bullet : bullets) {
			g.setColor(Color.BLUE);
			g.fillRect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
		}
		
		g.drawImage(shipImage, ship.getX(), ship.getY(), ship.getWidth(), ship.getHeight(), null);
		
		//g.setColor(Color.WHITE);
		//for(Point point : ship.getBounds().getPoints()) {
		//	g.drawLine(point.getX()+ship.getX(), point.getY()+ship.getY(), point.getX()+ship.getX(), point.getY()+ship.getY());
		//}
		
		// draw score
		String score = "SCORE: " + this.score;
		
		g.setColor(Color.blue);
		g.setFont(new Font("Helvetica", Font.PLAIN, 18));
		
		int scoreWidth = (int)g.getFontMetrics().getStringBounds(score, g).getWidth();
		int startX = this.width - scoreWidth - 10;
		int startY = 20;
		
		g.drawString(score, startX, startY);
		
		g.drawString("Time elapsed: " + (int)((new Date().getTime() - gameStartedAt.getTime()) / 1000), 10, 20);
	}
	
	private void paintGameOver(Graphics g) {
		g.setColor(Color.blue);
		g.setFont(new Font("Helvetica", Font.PLAIN, 48));
		
		String gameOverText = "GAME OVER";
		Rectangle2D gameOverBounds = g.getFontMetrics().getStringBounds(gameOverText, g);
        int startX = (int)((width / 2) - (gameOverBounds.getWidth() / 2));
        int startY = (int)((height / 2) - (gameOverBounds.getHeight() / 2));
        
		g.drawString(gameOverText, startX, startY);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Date loopStart = new Date();
		
		doLogic();
		repaint();
		
		Date loopEnd = new Date();
		
		if(gameTicks.size() < 30) {
			gameTicks.add(loopEnd.getTime() - loopStart.getTime());
		} else {
			Long total = new Long(0L);
			
			for(Long one : gameTicks) {
				total += one;
			}
			
			System.out.println("Game tick averages at about: " + (total / gameTicks.size()) + " milliseconds");
			
			gameTicks.clear();
		}
	}
	
	class TAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if(gameState == 0) { // Main Menu
				menuControlsBuffer.enqueue(key);
				return;
			}
			
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
