import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGamepanel extends JPanel implements ActionListener, KeyListener{

	private class Tile{
		int x;
		int y;
		
		Tile(int x,int y){
			this.x=x;
			this.y=y;
		}
	}
	int boardwidth, boardheight;
	int tileSize=25;
	
	//Snake
	Tile snakeHead;
	ArrayList<Tile> snakebody;
	
	//Food
	Tile snakeFood;
	Random random;
	
	//	game logic
	
	Timer gameloop;
	int velocityX, velocityY;
	boolean GameOver=false;
	
	SnakeGamepanel(int boardwidth, int boardheight){
		this.boardwidth= boardwidth;
		this.boardheight= boardheight;
		setPreferredSize(new Dimension(this.boardwidth, this.boardheight));
		setBackground(Color.black);
		addKeyListener(this);
		setFocusable(true);
		
		snakeHead=new Tile(5,5);
		snakebody= new ArrayList<Tile>();
		
		snakeFood= new Tile(10,10);
		random = new Random();
		placeFood();
		
		velocityX=0;
		velocityY= 0;
		gameloop =new Timer(100,this);
		gameloop.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		//GRID
//		for(int i=0;i<boardwidth/tileSize;i++) {
//			g.drawLine(i*tileSize, 0, i*tileSize, boardheight);
//			g.drawLine(0,i*tileSize, boardwidth, i*tileSize);
//
//		}
		
		//Food
		
		g.setColor(Color.red);
		g.fill3DRect(snakeFood.x*tileSize, snakeFood.y*tileSize,tileSize, tileSize, getFocusTraversalKeysEnabled());
		
		//Snake
		g.setColor(Color.green);
		g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, getFocusTraversalKeysEnabled());
		
		//Snake body as it eats the food
		for(int i=0;i<snakebody.size();i++) {
			Tile snakepart= snakebody.get(i);
//			g.fillRoundRect(snakepart.x*tileSize, snakepart.y*tileSize, tileSize, tileSize, tileSize/2, tileSize/2);
			g.fill3DRect(snakepart.x*tileSize, snakepart.y*tileSize, tileSize, tileSize, true);
		}
		
		//Score and Game Over declaration
		g.setFont(new Font("Ink Free", Font.BOLD, 30)); // Larger font

		if (GameOver) {
		    g.setColor(Color.RED);
		    
		    // The message to display
		    String gameOverText = "GAME OVER!";
		    String scoreText = "Your Score: " + snakebody.size();
		    String restartText = "Press [SPACE] to restart";
		    
		    // Get FontMetrics to calculate text width/height
		    FontMetrics metrics = g.getFontMetrics();
		    
		    // Calculate X position to center each line
		    int gameOverX = (getWidth() - metrics.stringWidth(gameOverText)) / 2;
		    int scoreX = (getWidth() - metrics.stringWidth(scoreText)) / 2;
		    int restartX = (getWidth() - metrics.stringWidth(restartText)) / 2;
		    
		    // Calculate Y position (stacked vertically)
		    int yPos = getHeight() / 2; // Start at the middle
		    
		    // Draw each line with spacing
		    g.drawString(gameOverText, gameOverX, yPos-30); // Above center
		    g.drawString(scoreText, scoreX, yPos + 10); // Below "GAME OVER!"
		    g.drawString(restartText, restartX, yPos + 50); // Further below
		} 
		
		else {
		    // Original score display when playing
		    g.setColor(Color.WHITE); 
		    g.drawString("SCORE: " + snakebody.size(), tileSize - 20, tileSize);
		}
	}
	
	public void placeFood(){
		snakeFood.x= random.nextInt(boardwidth/tileSize);//0to24
		snakeFood.y= random.nextInt(boardheight/tileSize);
	}
	
	public boolean collision(Tile t1, Tile t2) {
		return t1.x==t2.x && t1.y==t2.y;
	}

	public void move() {
		//food-ate
		if(collision(snakeHead, snakeFood)) {
			snakebody.add(new Tile(snakeFood.x, snakeFood.y));
			placeFood();
		}
		
		//snake body movement
		for(int i=snakebody.size()-1;i>=0;i--) {
			Tile snakepart= snakebody.get(i);
			if(i==0) {
				snakepart.x=snakeHead.x;
				snakepart.y=snakeHead.y;
			}
			else {
				Tile prevsnakebody= snakebody.get(i-1);
				snakepart.x=prevsnakebody.x;
				snakepart.y=prevsnakebody.y;
			}
		}
		
		//Snake head
		snakeHead.x+=velocityX;
		snakeHead.y+=velocityY;

		//game over condition
		for(int i=0;i<snakebody.size();i++) {
			Tile snakepart= snakebody.get(i);
			//if snake collides with itself
			if(collision(snakeHead, snakepart)) {
				GameOver=true;
			}
		}
		
		if(snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardwidth ||
			snakeHead.y*tileSize < 0 || snakeHead.y *tileSize > boardheight) {
			GameOver=true;
		}
	}
	
	public void resetGame() {
	    // Reset snake
	    snakeHead = new Tile(5, 5); // Initial position
	    snakebody.clear();
	    
	    // Reset food
	    placeFood();
	    
	    // Reset movement
	    velocityX = 0;
	    velocityY = 0;
	    
	    // Reset game state
	    GameOver = false;
	    
	    // Restart timer
	    gameloop.start();
	    
	    // Force a repaint to clear "Game Over" screen
	    repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		move();
		repaint();
		if(GameOver) {
			gameloop.stop();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if(e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1) {
			velocityX=0;
			velocityY=-1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1) {
			velocityX=0;
			velocityY=1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1) {
			velocityX=1;
			velocityY=0;
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1) {
			velocityX=-1;
			velocityY=0;
		}
		 // Restart game on SPACE if GameOver
	    else if (e.getKeyCode() == KeyEvent.VK_SPACE && GameOver) {
	        resetGame();
	    }
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
