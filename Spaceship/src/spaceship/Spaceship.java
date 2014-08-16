package spaceship;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class Spaceship extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	public Spaceship() {
		setTitle("Spaceship Game by Erik Kralj");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		add(new Board(getContentPane().getWidth(), getContentPane().getHeight()));
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Spaceship();
			}
		});
	}

}
