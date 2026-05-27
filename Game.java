import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*; 

public class Game  extends JPanel implements Runnable, KeyListener{

	
	private BufferedImage back; 
	private int key;
	private int score = 0; 
	private Background bg;
	private ImageIcon background;
	private ImageIcon loseBG;
	private char screen;
	private Flappy c;
	private ArrayList<Obstacle> obstacles;
	private javax.swing.Timer obstacleTimer;
	private Random random;
	
	public Game() {
		new Thread(this).start();	
		this.addKeyListener(this);
		key =-1; 
		screen = 'S';
		c = new Flappy();
		SoundManager.playBackgroundMusic(bgm,true);
		obstacles = new ArrayList<Obstacle>();
		random = new Random();


	}

	public void screen(Graphics g2d){		
		switch(screen){

			case 'S':
			g2d.clearRect(0,0,getWidth(),getHeight());
			g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
			g2d.setFont(new Font("Georgia", Font.BOLD, 65));
			Color GreenColor= new Color(74, 158, 122);
			g2d.setColor(GreenColor);
			g2d.drawString("Press S to Start", getWidth()/2 - 250, getHeight()/2 - 60);
			g2d.drawString("and P to Pause", getWidth()/2 - 250, getHeight()/2 + 30);
			break;

			case 'G':
			break;

			case 'P':
			g2d.clearRect(0,0,getSize().width,getSize().height);
			g2d.drawImage(background.getImage(), bg.getX(), 0, getWidth(), getHeight(), this);
			g2d.drawImage(background.getImage(), bg.getX() + getWidth(), 0, getWidth(), getHeight(), this);
			g2d.setFont(new Font("Georgia", Font.BOLD, 75));
			Color stemGreenColor= new Color(74, 158, 122);
			g2d.setColor(stemGreenColor);
			int x = getWidth()/2 - 150;
			int y = getHeight()/2; 
			g2d.drawString("Paused", x, y);



			break;

			case 'L':
			
			g2d.clearRect(0, 0, getWidth(), getHeight());
			g2d.drawImage(loseBG.getImage(), 0, 0, getWidth(), getHeight(), this);
			g2d.setFont(new Font("Georgia", Font.BOLD, 65)); 
			g2d.setColor(Color.blue); 
			g2d.drawString("You Lost!", getWidth()/2 - 160, getHeight()/2 - 60); 
			g2d.drawString("Score: " + score, getWidth()/2 - 130, getHeight()/2 + 10);
			g2d.drawString("Press R to Restart", getWidth()/2 - 310, getHeight()/2 + 80);
			if(!playedloseMusic){
				SoundManager.stopBackgroundMusic();
				SoundManager.playBackgroundMusic(lm, false);
				playedloseMusic = true;
			}
			
			return;		
		}
		}

	public void initializeBackground(){
		bg = new Background(getWidth());
		background = new ImageIcon(bg.getBackground());
		loseBG = new ImageIcon ("losebg.jpg");
	}
	
	public void run()
	   {
	   	try
	   	{
	   		while(true)
	   		{
				if(screen != 'P'){
	   		   Thread.currentThread().sleep(10);
	        	repaint();
	        }
			
			else{
				Thread.currentThread().sleep(10);
			}
			
			}
			 
	      }
	   		catch(Exception e)
	      {
	      }
	  	}
	
	public void paint(Graphics g){
		
		Graphics2D twoDgraph = (Graphics2D) g; 
		if( back ==null)
			back=(BufferedImage)( (createImage(getWidth(), getHeight()))); 
		if (background == null){
			initializeBackground();
		}

		Graphics g2d = back.createGraphics();

	
		g2d.clearRect(0,0,getSize().width, getSize().height);
		
		g2d.drawImage(background.getImage(), bg.getX(), 0, getWidth(), getHeight(), this);
		g2d.drawImage(background.getImage(), bg.getX() + getWidth(), 0, getWidth(), getHeight(), this);

		if (screen == 'G'){
			bg.move();
			c.move();
			checkCollisions();
			moveObstacles();
			updateScore(); 
		}

		g2d.drawImage(c.getPic().getImage(), c.getX(), c.getY(), c.getWidth(), c.getHeight(), this);
		drawObstacles(g2d);

		if (screen == 'G'){
			g2d.setFont(new Font("Georgia", Font.BOLD, 50));
			Color blueish= new Color(141, 115, 217);
			g2d.setColor(blueish);
			g2d.drawString("Score: " + score, 30, 60); }

		screen(g2d);

		twoDgraph.drawImage(back, null, 0, 0);

	}

	public void startObstacleSpawner(){

		if (obstacleTimer != null && obstacleTimer.isRunning()){
			obstacleTimer.stop();
		}

		int delay = 1000 + random.nextInt(1000);

		obstacleTimer = new javax.swing.Timer(delay, e -> buildObstacles());
		obstacleTimer.start();

	}

	public void updateScore(){
		for (Obstacle o : obstacles){
		if (!o.hasScored() && o.getX() + o.getWidth() < c.getX()){
			score++;
			o.setScored(true);
		}
	}
}


	public void buildObstacles(){
		int screenWidth = getWidth();
		int screenHeight = getHeight();
		int x = screenWidth + 100;
		int width = 70 + random.nextInt(91);
		int height = 200 + random.nextInt(151);
		boolean isTop = random.nextBoolean();
		int y = isTop ? 0 : screenHeight - height;
		ImageIcon image = new ImageIcon(isTop ? "top.png" : "bottom.png");

		Obstacle obstacle = new Obstacle(x,y,width,height,isTop,image);
		obstacles.add(obstacle);
	}

	public void checkCollisions(){
		for(Obstacle obstacle : obstacles){
			if(c.checkCollision(obstacle)){
				screen = 'L';
				break;
			}
		}
		if (c.getY() > getHeight()){
			screen = 'L'; 
		}
	}

	public void drawObstacles(Graphics g2d){
		for(Obstacle o : obstacles){ 
			ImageIcon img = o.getImage();
			int originalW = img.getIconWidth(); 
			int originalH = img.getIconHeight(); 
			int drawW = o.getWidth(); 
			double ratio = (double) originalH / originalW; 
			int drawH = (int)(drawW * ratio); 
			o.setHeight(drawH);
			int drawY = o.isTop() ? 0 : getHeight() - drawH;
			o.setY(drawY);
			g2d.drawImage( img.getImage(), o.getX(), drawY, drawW, drawH, this ); 
		}
	}

	public void moveObstacles(){
		for(int i = 0; i < obstacles.size(); i++){
			Obstacle o = obstacles.get(i);
			o.slide();

			if(o.getX() + o.getWidth() <0){
				obstacles.remove(i);
				i--;
			}
		}
	}

	public void resetGame(){
		SoundManager.playBackgroundMusic(bgm, true);
		obstacles.clear();
		c = new Flappy();
		score  = 0;
		screen = 'S';
		if (obstacleTimer != null){
			obstacleTimer.stop();
		}
	}

	//DO NOT DELETE
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}




//DO NOT DELETE
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		key= e.getKeyCode();
		System.out.println(key);
		
		if(key == 32){ // Space key
			c.jump();
		}
		
		if (key == 83) { // S key 
			if (screen == 'S'){
				screen = 'G';
				startObstacleSpawner();
			} else{
				c.activateNoFall(); 
			} 
		}

		if (key == 72){ // H key
			c.setWidth(90);
			c.setHeight(90);
		}

		if (key == 82) { // R key 
			resetGame(); }

		if(key == 80){ // P key
			if(screen == 'G'){
				screen = 'P';
			}
			else if (screen == 'P'){
				screen = 'G';
			}
		}
		
		if(key == 66){ // B key
			screen = 'G';
			startObstacleSpawner();
		}
	
	}


	//DO NOT DELETE
	@Override
	public void keyReleased(KeyEvent e) {
		
		
		
		
	}
	
	
	

	
}
