package spaceship;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Spaceship extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static final int screenWidth = 640;
	public static final int screenHeight = 480;
	
	private Board board = new Board();
	
	public Spaceship() {
		add(board);
		
		setResizable(false);
		setTitle("Spaceship Game by Erik Kralj");
		setSize(screenWidth, screenHeight);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Spaceship ss = new Spaceship();
				ss.setVisible(true);
			}
		});
	}

}
