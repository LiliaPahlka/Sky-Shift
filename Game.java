import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.*; 


public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener{

	
	private BufferedImage back; 
	private int key, count, score, lives;
	private char screen;
	private Character character; 
	private ArrayList<Feather> feather;
	private ArrayList<Leaf> leaf;
	private ArrayList<Petal> petal;
	private ArrayList<Dragonfly> dragonfly;
	private ImageIcon background;

	private boolean startScreen = true;
	private String bgMusic;


	
	public Game() {
		new Thread(this).start();	
		this.addKeyListener(this);		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		screen = 'G';
		character = new Character();
		feather = new ArrayList<Feather>();
		leaf = new ArrayList<Leaf>();
		petal = new ArrayList<Petal>();
		dragonfly = new ArrayList<Dragonfly>();
		count = 0;
		score = 0;
		lives = 5;
		background = new ImageIcon("skybg.png");
		bgMusic = "skybgm.wav";
		SoundManager.playBackgroundMusic(bgMusic, true);

	}

	
	
	public void run()
	   {
	   	try
	   	{
	   		while(true)
	   		{
	   		   Thread.currentThread().sleep(5);
	            repaint();
	         }
	      }
	   		catch(Exception e)
	      {
	      }
	  	}


		public void screen(Graphics g2d){
			
			switch(screen){

			case 'S':

			break;
			
			case 'G':
			count++;
			getPetal(g2d);
			getLeaf(g2d);
			getDragonfly(g2d);
			getFeather(g2d);
			drawCharacter(g2d);
			character.move();
			checkCollisions();

			if(!petal.isEmpty())
			drawPetal(g2d);
			if(!leaf.isEmpty())
			drawLeaf(g2d);
			if(!feather.isEmpty())
			drawFeather(g2d);
			if(!dragonfly.isEmpty())
			drawDragonfly(g2d);

			removeItems();
			break;

			case 'W':

			break;

			case 'L':

			break;


			}
		}
	
	
	public void paint(Graphics g) {
    Graphics2D twoDgraph = (Graphics2D) g;

    if (back == null) {
        back = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    Graphics g2d = back.createGraphics();
    g2d.clearRect(0, 0, getWidth(), getHeight());

    if (startScreen) {
		g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        g2d.setFont(new Font("Georgia", Font.PLAIN, 50));
        g2d.drawString("Press the spacebar to start", 400, 300);
        g2d.setFont(new Font("Georgia", Font.BOLD, 50));
        g2d.drawString("Welcome to Sky Shift", 320, 200);
    } else {
        g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);

        if (screen == 'L') {
            g2d.setFont(new Font("Georgia", Font.BOLD, 60));
            Color lightBlue= new Color(132, 173, 245);
		g2d.setColor(lightBlue);
            g2d.drawString("Game Over!", 500, 200);
			g2d.setFont(new Font("Georgia", Font.PLAIN, 50));
			g2d.setColor(lightBlue);
            g2d.drawString("Press r to restart!", 490, 300);
        } else if (screen == 'W') {
            g2d.setFont(new Font("Georgia", Font.BOLD, 60));
            g2d.setColor(Color.Blue);
            g2d.drawString("You Won!", 500, 200);
			g2d.setFont(new Font("Georgia", Font.PLAIN, 50));
			g2d.setColor(Color.Blue);
            g2d.drawString("Press r to restart!", 490, 300);
        } else {
            screen(g2d);
        }
    }

	g2d.setColor(Color.Pink);
	g2d.setFont(new Font("Georgia", Font.BOLD, 50));
	g2d.drawString("Score: " + score, 100, 40);
	g2d.drawString("Lives: " + lives, 100, 90);

    twoDgraph.drawImage(back, null, 0, 0);
}

	public void drawCharacter(Graphics g2d){
		g2d.drawImage(character.getPic().getImage(), character.getX(), character.getY(),
		character.getWidth(), character.getHeight(), this);
	}
	
	public void getPetal(Graphics g2d){

		if(count % 600 == 0){
			// get the screen width
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

			// define the padding
			int padding = 10;

			// generate a random x coordinate within the screen width, ensuring the object is fully displayed
			int randX = padding + (int) (Math.random() * (screenWidth - (2 * padding)));

			// add a new object at the random x coordinate
			petal.add(new Petal(randX));
		}

	}

	public void drawFeather(Graphics g2d){
		for(Feather feather: feather){
			g2d.drawImage(feather.getPic().getImage(), feather.getX(), feather.getY(),
			feather.getWidth(), feather.getHeight(), this);
			feather.setDy(2);
		}
	}

	public void removeOOBFeather(ArrayList<Feather>feather){
		for(int i=0; i<feather.size(); i++){
			Feather feather = feather.get(i);
			if(feather.getY() > 1000){
				feather.remove(i);
				i--;
			}
		}
	}

	public void removeItems(){
		removeOOBLeaf(leaf);
		removeOOBPetal(petal);
		removeOOBFeather(feather);
		removeOOBDragonfly(dragonfly);
	}

	public void checkCollisions(){
		checkObjectCollisionsa(leaf, 2);
		checkObjectCollisionsb(petal, 2);
		checkObjectCollisionsc(feather, 1);
		checkObjectCollisionsd(dragonfly, -1);

		checkGameStatus();
	}

	private void checkObjectCollisionsa(ArrayList<Leaf> leaf, int points){
		for (int i = 0; i < leaf.size(); i++){
			Leaf leaf = leaf.get(i);
			if(leaf.collidesWith(character)){
				score += points;
				leaf.remove(i);
				i--;
			} 
		}
	}

	private void checkObjectCollisionsb(ArrayList<Feather> feather, int points){
		for (int i = 0; i < feather.size(); i++){
			Feather feather = feather.get(i);
			if(feather.collidesWith(character)){
				score += points;
				feather.remove(i);
				i--;
			} 
		}
	}

	private void checkObjectCollisionsc(ArrayList<Petal> petal, int points){
		for (int i = 0; i < petal.size(); i++){
			Petal petal = petal.get(i);
			if(petal.collidesWith(character)){
				score += points;
				petal.remove(i);
				i--;
			} 
		}
	}

	private void checkObjectCollisionsd(ArrayList<Dragonfly> dragonfly, int points) {
    for (int i = 0; i < dragonfly.size(); i++) {
        Dragonfly dragonfly = dragonfly.get(i);
        if (dragonfly.collidesWith(character)) {
            score += points;
            lives--;
            dragonfly.remove(i);
            i--;
        }
    }
}

	private void checkGameStatus(){
		if (lives <= 0){
			System.out.println("Game Over!");
			screen = 'L';
		}
		else if (score >= 20){
			System.out.println("You Win!");
			screen = 'W';
		}
	}

	public void getLeaf(Graphics g2d){

		if(count % 500 == 0){
			// get the screen width
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

			// define the padding
			int padding = 10;

			// generate a random x coordinate within the screen width, ensuring the object is fully displayed
			int randX = padding + (int) (Math.random() * (screenWidth - (2 * padding)));

			// add a new object at the random x coordinate
			leaf.add(new Leaf(randX));
		}

	}

	public void drawLeaf(Graphics g2d){
		for(Leaf leaf: leaf){
			g2d.drawImage(leaf.getPic().getImage(), leaf.getX(), leaf.getY(),
			leaf.getWidth(), leaf.getHeight(), this);
			leaf.setDy(2);

		}
	}

	public void removeOOBLeaf(ArrayList<Leaf>leaf){
		for(int i=0; i<leaf.size(); i++){
			Leaf leaf = leaf.get(i);
			if(leaf.getY() > 1000){
				leaf.remove(i);
				i--;
			}
		}
	}

	public void getDragonfly(Graphics g2d){

		if(count % 600 == 0){
			// get the screen width
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

			// define the padding
			int padding = 10;

			// generate a random x coordinate within the screen width, ensuring the object is fully displayed
			int randX = padding + (int) (Math.random() * (screenWidth - (2 * padding)));

			// add a new object at the random x coordinate
			dragonfly.add(new Dragonfly(randX));
		}

	}

	public void drawDragonfly(Graphics g2d){
		for(Dragonfly dragonfly: dragonfly){
			g2d.drawImage(dragonfly.getPic().getImage(), dragonfly.getX(), dragonfly.getY(),
			dragonfly.getWidth(), dragonfly.getHeight(), this);
			dragonfly.setDy(2);

		}
	}

	public void removeOOBDragonfly(ArrayList<Dragonfly>dragonfly){
		for(int i=0; i<dragonfly.size(); i++){
			Dragonfly dragonfly = dragonfly.get(i);
			if(dragonfly.getY() > 1000){
				dragonfly.remove(i);
				i--;
			}
		}
	}

	public void getFeather(Graphics g2d){

		if(count % 600 == 0){
			// get the screen width
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

			// define the padding
			int padding = 10;

			// generate a random x coordinate within the screen width, ensuring the object is fully displayed
			int randX = padding + (int) (Math.random() * (screenWidth - (2 * padding)));

			// add a new object at the random x coordinate
			feather.add(new Feather(randX));
		}

	}


	public void removeOOBPetal(ArrayList<Petal>petal){
		for(int i=0; i<petal.size(); i++){
			Petal petal = petal.get(i);
			if(petal.getY() > 1000){
				petal.remove(i);
				i--;
			}
		}
	}


public void resetGame() {
    screen = 'G';
    startScreen = true;
    score = 0;
    lives = 5;
    count = 0;

    character = new Character();
    petal.clear();
    leaf.clear();
    feather.clear();
    dragonfly.clear();
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
		
	if(key == 37){
		character.setDx(-3);
	}
		
	if(key == 39){
		character.setDx(3);
	}
	
	}


	//DO NOT DELETE
	@Override
	public void keyReleased(KeyEvent e) {
		
	if(key == 37){
		character.setDx(0);
	}

	if(key == 39){
		character.setDx(0);
	}

	if (key == 32) { // Space key
    if (startScreen) {
        startScreen = false;
		screen = 'G';
	}
}

	if (key == 82) {
		resetGame();
	}
		
		
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}



	@Override
	public void mouseMoved(MouseEvent m) {
		// TODO Auto-generated method stub
		character.setX(m.getX());
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	

	
}
