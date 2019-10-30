import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Renderer extends JPanel {
	
	private Game belongingGame;

	public Renderer(Game game) {
		
		this.belongingGame = game;
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

	}

	@Override
	protected void paintComponent(Graphics g) {
		Image img = Toolkit.getDefaultToolkit().getImage("background.jpg");
		super.paintComponent(g);

		g.drawImage(img, 0, 0, null);
	}

	public void paint(Graphics g)
	{    		
		paintComponent(g);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//The paddle length L is 10% of the screen width.
		//This L is going to be used as bases to measure other variables in the rest of this document.
		//The paddle thickness T is 20px.
		// the screen height
		screenSize.getHeight();

		// the screen width
		screenSize.getWidth();     	

		// background

		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);

		// drawing map
		belongingGame.getMap().draw((Graphics2D) g);

		// borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);

		// the scores 		
		g.setColor(Color.white);
		g.setFont(new Font("serif",Font.BOLD, 25));
		g.drawString(""+belongingGame.getScore(), 590,30);

		// the paddle
		g.setColor(Color.green);
		g.fillRect(belongingGame.getPlayerX(), 550, belongingGame.getL(), 20);

		// the ball
		g.setColor(Color.yellow);
		g.fillOval(belongingGame.getBallposX(), belongingGame.getBallposX(), 20, 20);

		if(belongingGame.getGameStatus() == GameStatus.Won) paintWon(g);
		
		if(belongingGame.getGameStatus() == GameStatus.Lost) paintLost(g);
		
		g.dispose();
	}	
	public void paintWon(Graphics g) {

		g.setColor(Color.RED);
		g.setFont(new Font("serif",Font.BOLD, 30));
		g.drawString("You Won", 260,300);

		g.setColor(Color.RED);
		g.setFont(new Font("serif",Font.BOLD, 20));           
		g.drawString("Press (Enter) to Restart", 230,350);  
	}
	public void paintLost(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font("serif",Font.BOLD, 30));
		g.drawString("Game Over, Scores: "+belongingGame.getScore(), 190,300);

		g.setColor(Color.RED);
		g.setFont(new Font("serif",Font.BOLD, 20));           
		g.drawString("Press (Enter) to Restart", 230,350);     	
	}
}
