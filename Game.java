import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.*; 

public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

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
        setFocusable(true);
        requestFocusInWindow();
    }

    public void run() {
        try {
            while (true) {
                Thread.currentThread();
				Thread.sleep(5);
                repaint();
            }
        } catch (Exception e) {
        }
    }

    public void screen(Graphics g2d) {
        switch (screen) {

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

                if (!petal.isEmpty())
                    drawPetal(g2d);
                if (!leaf.isEmpty())
                    drawLeaf(g2d);
                if (!feather.isEmpty())
                    drawFeather(g2d);
                if (!dragonfly.isEmpty())
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
                Color lightBlue = new Color(132, 173, 245);
                g2d.setColor(lightBlue);
                g2d.drawString("Game Over!", 500, 200);
                g2d.setFont(new Font("Georgia", Font.PLAIN, 50));
                g2d.setColor(lightBlue);
                g2d.drawString("Press r to restart!", 490, 300);
            } else if (screen == 'W') {
                g2d.setFont(new Font("Georgia", Font.BOLD, 60));
                g2d.setColor(Color.blue);
                g2d.drawString("You Won!", 500, 200);
                g2d.setFont(new Font("Georgia", Font.PLAIN, 50));
                g2d.setColor(Color.blue);
                g2d.drawString("Press r to restart!", 490, 300);
            } else {
                screen(g2d);
            }
        }

        g2d.setColor(Color.pink);
        g2d.setFont(new Font("Georgia", Font.BOLD, 50));
        g2d.drawString("Score: " + score, 100, 40);
        g2d.drawString("Lives: " + lives, 100, 90);

        twoDgraph.drawImage(back, null, 0, 0);
    }

    public void drawCharacter(Graphics g2d) {
        g2d.drawImage(character.getPic().getImage(), character.getX(), character.getY(),
                      character.getWidth(), character.getHeight(), this);
    }

    public void getPetal(Graphics g2d) {
        if (count % 600 == 0) {
            int screenWidth = getWidth();
            int padding = 10;
            int randX = padding + (int) (Math.random() * (screenWidth - (2 * padding)));
            petal.add(new Petal(randX));
        }
    }

    public void drawPetal(Graphics g2d) {
        for (Petal p : petal) {
            g2d.drawImage(p.getPic().getImage(), p.getX(), p.getY(),
                          p.getWidth(), p.getHeight(), this);
            p.setDy(2);
        }
    }

    public void removeOOBPetal(ArrayList<Petal> petalList) {
        for (int i = 0; i < petalList.size(); i++) {
            Petal p = petalList.get(i);
            if (p.getY() > 1000) {
                petalList.remove(i);
                i--;
            }
        }
    }

    public void drawFeather(Graphics g2d) {
        for (Feather f : feather) {
            g2d.drawImage(f.getPic().getImage(), f.getX(), f.getY(),
                          f.getWidth(), f.getHeight(), this);
            f.setDy(2);
        }
    }

    public void removeOOBFeather(ArrayList<Feather> featherList) {
        for (int i = 0; i < featherList.size(); i++) {
            Feather f = featherList.get(i);
            if (f.getY() > 1000) {
                featherList.remove(i);
                i--;
            }
        }
    }

    public void removeItems() {
        removeOOBLeaf(leaf);
        removeOOBPetal(petal);
        removeOOBFeather(feather);
        removeOOBDragonfly(dragonfly);
    }

    public void checkCollisions() {
        checkObjectCollisionsa(leaf, 2);
        checkObjectCollisionsb(feather, 1);
        checkObjectCollisionsc(petal, 2);
        checkObjectCollisionsd(dragonfly, -1);
        checkGameStatus();
    }

    private void checkObjectCollisionsa(ArrayList<Leaf> leafList, int points) {
        for (int i = 0; i < leafList.size(); i++) {
            Leaf l = leafList.get(i);
            if (l.collidesWith(character)) {
                score += points;
                leafList.remove(i);
                i--;
            } 
        }
    }

    private void checkObjectCollisionsb(ArrayList<Feather> featherList, int points) {
        for (int i = 0; i < featherList.size(); i++) {
            Feather f = featherList.get(i);
            if (f.collidesWith(character)) {
                score += points;
                featherList.remove(i);
                i--;
            } 
        }
    }

    private void checkObjectCollisionsc(ArrayList<Petal> petalList, int points) {
        for (int i = 0; i < petalList.size(); i++) {
            Petal p = petalList.get(i);
            if (p.collidesWith(character)) {
                score += points;
                petalList.remove(i);
                i--;
            } 
        }
    }

    private void checkObjectCollisionsd(ArrayList<Dragonfly> dragonflyList, int points) {
        for (int i = 0; i < dragonflyList.size(); i++) {
            Dragonfly d = dragonflyList.get(i);
            if (d.collidesWith(character)) {
                score += points;
                lives--;
                dragonflyList.remove(i);
                i--;
            }
        }
    }

    private void checkGameStatus() {
        if (lives <= 0) {
            System.out.println("Game Over!");
            screen = 'L';
        } else if (score >= 20) {
            System.out.println("You Win!");
            screen = 'W';
        }
    }

    public void getLeaf(Graphics g2d) {
        if (count % 500 == 0) {
            int screenWidth = getWidth();
            int padding = 10;
            int randX = padding + (int) (Math.random() * (screenWidth - (2 * padding)));
            leaf.add(new Leaf(randX));
        }
    }

    public void drawLeaf(Graphics g2d) {
        for (Leaf l : leaf) {
            g2d.drawImage(l.getPic().getImage(), l.getX(), l.getY(),
                          l.getWidth(), l.getHeight(), this);
            l.setDy(2);
        }
    }

    public void removeOOBLeaf(ArrayList<Leaf> leafList) {
        for (int i = 0; i < leafList.size(); i++) {
            Leaf l = leafList.get(i);
            if (l.getY() > 1000) {
                leafList.remove(i);
                i--;
            }
        }
    }

    public void getDragonfly(Graphics g2d) {
        if (count % 600 == 0) {
            int screenWidth = getWidth();
            int padding = 10;
            int randX = padding + (int) (Math.random() * (screenWidth - (2 * padding)));
            dragonfly.add(new Dragonfly(randX));
        }
    }

    public void drawDragonfly(Graphics g2d) {
        for (Dragonfly d : dragonfly) {
            g2d.drawImage(d.getPic().getImage(), d.getX(), d.getY(),
                          d.getWidth(), d.getHeight(), this);
            d.setDy(2);
        }
    }

    public void removeOOBDragonfly(ArrayList<Dragonfly> dragonflyList) {
        for (int i = 0; i < dragonflyList.size(); i++) {
            Dragonfly d = dragonflyList.get(i);
            if (d.getY() > 1000) {
                dragonflyList.remove(i);
                i--;
            }
        }
    }

    public void getFeather(Graphics g2d) {
        if (count % 600 == 0) {
            int screenWidth = getWidth();
            int padding = 10;
            int randX = padding + (int) (Math.random() * (screenWidth - (2 * padding)));
            feather.add(new Feather(randX));
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();
        System.out.println(key);

        if (key == KeyEvent.VK_LEFT) {
            character.setDx(-3);
        }

        if (key == KeyEvent.VK_RIGHT) {
            character.setDx(3);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int released = e.getKeyCode();

        if (released == KeyEvent.VK_LEFT || released == KeyEvent.VK_RIGHT) {
            character.setDx(0);
        }

        if (released == KeyEvent.VK_SPACE) {
            if (startScreen) {
                startScreen = false;
                screen = 'G';
            }
        }

        if (released == KeyEvent.VK_R) {
            resetGame();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent m) {
        character.setX(m.getX());
    }

    @Override
    public void mouseClicked(MouseEvent e) {        
    }

    @Override
    public void mousePressed(MouseEvent e) {        
    }

    @Override
    public void mouseReleased(MouseEvent e) {       
    }

    @Override
    public void mouseEntered(MouseEvent e) {        
    }

    @Override
    public void mouseExited(MouseEvent e) {     
    }
}
