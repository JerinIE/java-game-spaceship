package spaceship;

import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * Represents the container for the game board.
 * @author Erik Kralj
 *
 */
public class Spaceship extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
	/**
	 * Prepares the JFrame by settings its title, size,
	 * close operation, location, visibility and adds a
	 * new instance of Board to its self. 
	 */
	public Spaceship() {
		setTitle("Spaceship Game by Erik Kralj");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		add(new Board(getContentPane().getWidth(), getContentPane().getHeight()));
	}
	
	/**
	 * Program main entry point.
	 * Creates an instance of JFrame.
	 * @param args CLR
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Spaceship();
			}
		});
	}

}
