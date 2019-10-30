
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

enum GameStatus{
	Won,Lost,Undecided
}
public class Game extends JPanel implements KeyListener, ActionListener 
{

	private boolean play = false;
	private int score = 0;
	private int totalBricks = 48;
	private Timer timer;
	private int delay=8;
	private int playerX = 310;
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	private int paddleLength = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/10); //Ten percent of the screen width.
	public GameStatus status = GameStatus.Undecided;
	private Renderer gameRenderer;
	private Map map;
	public Game()
	{		
		map = new Map(4, 12);
		addKeyListener(this);
		timer=new Timer(delay,this);
		timer.start();
		Renderer gameRenderer = new Renderer(this);

	}

	public Renderer getGameRenderer() {
		return gameRenderer;
	}

	public int getScore() {
		return score;
	}

	public int getPlayerX() {
		return playerX;
	}

	public int getBallposX() {
		return ballposX;
	}

	public int getBallposY() {
		return ballposY;
	}

	public int getBallXdir() {
		return ballXdir;
	}

	public int getBallYdir() {
		return ballYdir;
	}

	public Renderer getRenderer() {
		return this.gameRenderer;
	}
	public int getL() {
		return paddleLength;
	}
	public GameStatus getGameStatus() {
		return status;
	}
	public Map getMap() {
		return map;
	}
	
	public void decideGameStatus() {
		if(totalBricks <= 0)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			status = GameStatus.Won;
		}
		if(ballposY > 570)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			status = GameStatus.Lost;
		}

	}
	
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{        
			if(playerX >= 600)
			{
				playerX = 600;
			}
			else
			{
				moveRight();
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{          
			if(playerX < 10)
			{
				playerX = 10;
			}
			else
			{
				moveLeft();
			}
		}		
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{          
			if(!play)
			{

				play = true;
				ballposX = 120;
				ballposY = 530;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 21;

				repaint();
			}else switchMode();
		}		
	}


	public void switchMode() {

		if(timer.isRunning()) {
			timer.stop();
		}else timer.start();

	}





	public void keyReleased(KeyEvent e) {

	}
	public void keyTyped(KeyEvent e) {}

	public void moveRight()
	{
		play = true;
		playerX+=20;	
	}

	public void moveLeft()
	{
		play = true;
		playerX-=20;	 	
	}

	public void actionPerformed(ActionEvent e) 
	{
		timer.start();
		if(play)
		{			
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8)))
			{
				ballYdir = -ballYdir;
				ballXdir = -2;
			}
			else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8)))
			{
				ballYdir = -ballYdir;
				ballXdir = ballXdir + 1;
			}
			else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8)))
			{
				ballYdir = -ballYdir;
			}

			// check map collision with the ball		
			A: for(int i = 0; i<map.map.length; i++)
			{
				for(int j =0; j<map.map[0].length; j++)
				{				
					if(map.map[i][j] > 0)
					{
						//scores++;
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;

						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);					
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;

						if(ballRect.intersects(brickRect))
						{					
							map.setBrickValue(0, i, j);
							score+=5;	
							totalBricks--;

							// when ball hit right or left of brick
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)	
							{
								ballXdir = -ballXdir;
							}
							// when ball hits top or bottom of brick
							else
							{
								ballYdir = -ballYdir;				
							}

							break A;
						}
					}
				}
			}

			ballposX += ballXdir;
			ballposY += ballYdir;

			if(ballposX < 0)
			{
				ballXdir = -ballXdir;
			}
			if(ballposY < 0)
			{
				ballYdir = -ballYdir;
			}
			if(ballposX > 670)
			{
				ballXdir = -ballXdir;
			}		
			decideGameStatus();
			gameRenderer.repaint();
		}
	}
}
