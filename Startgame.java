package OurGame.Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import OurGame.GameTest;
import OurGame.Model.*;

public class Startgame extends JPanel implements MouseMotionListener, MouseListener {

    private Image backgroundImage;
    private Image iceLayer;
    private Image penguinImage;
    private Image fishBoxImage;
    private Image wormCanImage;

    private Line line;
    private Hook hook;
    
    private boolean gameRunning = true;
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> removeEnt = new ArrayList<>();

    // Game state
    private int score = 0;
    private int remainingWorms = 3;
    private int caughtGoldenFish = 0;
    private long startTime;
    private long elapsedTime = 0;
    
    // Bait system
    private enum BaitType { WORM, GOLDEN_FISH }
    private BaitType currentBait = BaitType.WORM;
    private Entity hookedFish = null;  // Fish currently on the hook
    
    // Hook position
    private int mouseY = 0;
    private int hookX = 960;
    private int hookY = 150;
    private int hookMinY = 150;
    private int hookMaxY = 1000;
    
    // UI areas (clickable)
    private Rectangle fishBoxArea = new Rectangle(1300, 30, 100, 100);
    private Rectangle wormCanArea = new Rectangle(1450, 30, 100, 100);
    private Rectangle closeButtonArea = new Rectangle(1600, 20, 65, 65);
    
    // Game loop
    private Timer gameTimer;
    private Random random = new Random();
    private long lastSpawnTime = 0;
    private static final long SPAWN_INTERVAL = 2000; // 2 seconds

    private GameTest frame;

    public Startgame(GameTest frame) {
        this.frame = frame;
        
        // Load images
        backgroundImage = new ImageIcon(getClass().getResource("/SpritesHD/Ocean_HD.png")).getImage();
        iceLayer = new ImageIcon(getClass().getResource("/SpritesHD/Ice_HD.png")).getImage();
        penguinImage = new ImageIcon(getClass().getResource("/SpritesHD/fishing_penguin.png")).getImage();
        
        setLayout(null);
        setFocusable(true);
        
        addMouseMotionListener(this);
        addMouseListener(this);
        
        startGame();
    }

    private void startGame() {
        entities.clear();
        removeEnt.clear();
        
        score = 0;
        remainingWorms = 3;
        caughtGoldenFish = 0;
        currentBait = BaitType.WORM;
        hookedFish = null;
        gameRunning = true;
        
        startTime = System.currentTimeMillis();
        
        // Create hook
        hook = new Hook(this, "/SpritesHD/hook.png", hookX, hookY);
        entities.add(hook);
        
        // Start game loop
        gameTimer = new Timer(16, e -> gameLoop()); // ~60 FPS
        gameTimer.start();
    }

    private void gameLoop() {
        if (!gameRunning) {
            gameTimer.stop();
            showGameOver();
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        long delta = 16; // milliseconds per frame
        
        // Update hook position
        hook.y = hookY;
        
        // Move all entities
        for (Entity entity : entities) {
            entity.move(delta);
            entity.ownLogic();
        }
        
        // Check collisions
        checkCollisions();
        
        // Remove entities marked for removal
        entities.removeAll(removeEnt);
        removeEnt.clear();
        
        // Spawn new entities
        if (currentTime - lastSpawnTime > SPAWN_INTERVAL) {
            spawnRandomEntity();
            lastSpawnTime = currentTime;
        }
        
        // Check game over conditions
        if (remainingWorms <= 0 && currentBait == BaitType.WORM) {
            gameRunning = false;
        }
        
        repaint();
    }

    private void spawnRandomEntity() {
        int type = random.nextInt(100);
        int startX = random.nextBoolean() ? -100 : 1920;
        int startY = 200 + random.nextInt(700);
        double speed = 50 + random.nextInt(100);
        
        Entity newEntity = null;
        
        if (type < 30) { // 30% Gray Fish
            newEntity = new GrayFish(this, "/SpritesHD/GreyFish-0_HD", startX, startY);
        } else if (type < 50) { // 20% Golden Fish
            newEntity = new GoldenFish(this, "/SpritesHD/GoldFish-0_HD.png", startX, startY);
        } else if (type < 60) { // 10% Mullet Fish
            newEntity = new MulletFish(this, "/SpritesHD/MulletFish-1_HD.png", startX, startY);
        } else if (type < 75) { // 15% Shark
            newEntity = new Shark(this, "/SpritesHD/shark1_HD.png", startX, startY);
        } else if (type < 85) { // 10% Jellyfish
            newEntity = new Jellyfish(this, "/SpritesHD/JellyFish_1_HD.png", startX, startY);
        } else if (type < 95) { // 10% Boot
            newEntity = new Boot(this, "/SpritesHD/Boot_HD.png", startX, startY);
        } else { // 5% Metal Can
            newEntity = new MetalCan(this, "/SpritesHD/can_HD.png", startX, startY);
        }
        
        if (newEntity != null) {
            newEntity.setHorizontalMovement(startX < 960 ? speed : -speed);
            entities.add(newEntity);
        }
    }

    private void checkCollisions() {
        for (Entity entity : entities) {
            if (entity == hook) continue;
            
            // Check collision with hook
            if (hook.collidesWith(entity)) {
                handleCollision(entity);
            }
            
            // Remove entities that went off screen
            if (entity.getX() < -200 || entity.getX() > 2100) {
                removeEnt.add(entity);
            }
        }
    }

    private void handleCollision(Entity entity) {
        if (entity instanceof GrayFish || entity instanceof GoldenFish) {
            if (currentBait == BaitType.WORM && hookedFish == null) {
                hookedFish = entity;
                entity.setHorizontalMovement(0);
                entity.setVerticalMovement(0);
            }
        } else if (entity instanceof MulletFish) {
            if (currentBait == BaitType.GOLDEN_FISH && hookedFish == null) {
                hookedFish = entity;
                entity.setHorizontalMovement(0);
                entity.setVerticalMovement(0);
                // Golden fish is consumed
                caughtGoldenFish--;
                currentBait = BaitType.WORM;
            }
        } else if (entity instanceof Shark || entity instanceof Jellyfish) {
            // Remove bait and hooked fish
            if (currentBait == BaitType.WORM) {
                remainingWorms--;
            } else {
                caughtGoldenFish--;
                currentBait = BaitType.WORM;
            }
            if (hookedFish != null) {
                removeEnt.add(hookedFish);
                hookedFish = null;
            }
            removeEnt.add(entity);
        } else if (entity instanceof MetalCan) {
            // Game over - line cut
            gameRunning = false;
        } else if (entity instanceof Boot) {
            // Remove only hooked fish
            if (hookedFish != null) {
                removeEnt.add(hookedFish);
                hookedFish = null;
            }
            removeEnt.add(entity);
        }
    }

    private void checkSurfaceCapture() {
        // Called when hook reaches surface with a fish
        if (hookedFish != null && hookY <= hookMinY + 10) {
            if (hookedFish instanceof GoldenFish) {
                caughtGoldenFish++;
                score++;
            } else if (hookedFish instanceof GrayFish) {
                score++;
            } else if (hookedFish instanceof MulletFish) {
                score++;
            }
            removeEnt.add(hookedFish);
            hookedFish = null;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseY = e.getY();
        hookY = mouseY;
        
        if (hookY < hookMinY) hookY = hookMinY;
        if (hookY > hookMaxY) hookY = hookMaxY;
        
        checkSurfaceCapture();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        // Check if clicked on fish box (to equip golden fish)
        if (fishBoxArea.contains(x, y) && hookY <= hookMinY + 20) {
            if (caughtGoldenFish > 0 && currentBait == BaitType.WORM) {
                currentBait = BaitType.GOLDEN_FISH;
                caughtGoldenFish--;
            }
        }
        
        // Check if clicked on worm can (to equip worm)
        if (wormCanArea.contains(x, y) && hookY <= hookMinY + 20) {
            if (currentBait == BaitType.GOLDEN_FISH) {
                // Return golden fish to box
                caughtGoldenFish++;
                currentBait = BaitType.WORM;
            }
        }
        
        // Check close button
        if (closeButtonArea.contains(x, y)) {
            gameTimer.stop();
            frame.showScreen("Homepage");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    private void showGameOver() {
        elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Fim de Jogo!\n\nPeixes Pescados: " + score + "\nTempo: " + elapsedTime + "s\n\nJogar novamente?",
            "Game Over",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            startGame();
        } else {
            frame.showScreen("Homepage");
        }
    }

    public void removeEntity(Entity entity) {
        removeEnt.add(entity);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw ocean background
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        
        // Draw line from penguin to hook
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(960, 100, hookX, hookY);
        
        // Draw entities
        for (Entity entity : entities) {
            entity.draw(g);
            
            // Draw hooked fish following hook
            if (entity == hookedFish) {
                entity.x = hookX;
                entity.y = hookY + 40;
            }
        }
        
        // Draw ice layer
        g.drawImage(iceLayer, 0, 0, getWidth(), 150, this);
        
        // Draw penguin
        g.drawImage(penguinImage, 748, 0, 410, 255, this);
        
        // Draw UI elements
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        
        // Worm counter (top left)
        g.drawString("Minhocas: " + remainingWorms, 30, 50);
        
        // Score
        g.drawString("Pescados: " + score, 30, 90);
        
        // Golden fish counter
        g.drawString("Peixes Dourados: " + caughtGoldenFish, 30, 130);
        
        // Current bait indicator
        String baitText = currentBait == BaitType.WORM ? "Isca: Minhoca" : "Isca: Peixe Dourado";
        g.drawString(baitText, 30, 170);
        
        // Draw fish box (clickable area)
        g.setColor(new Color(139, 69, 19));
        g.fillRect(fishBoxArea.x, fishBoxArea.y, fishBoxArea.width, fishBoxArea.height);
        g.setColor(Color.WHITE);
        g.drawRect(fishBoxArea.x, fishBoxArea.y, fishBoxArea.width, fishBoxArea.height);
        g.drawString("Caixa", fishBoxArea.x + 10, fishBoxArea.y + 30);
        g.drawString("Peixes", fishBoxArea.x + 5, fishBoxArea.y + 60);
        
        // Draw worm can (clickable area)
        g.setColor(new Color(100, 100, 100));
        g.fillRect(wormCanArea.x, wormCanArea.y, wormCanArea.width, wormCanArea.height);
        g.setColor(Color.WHITE);
        g.drawRect(wormCanArea.x, wormCanArea.y, wormCanArea.width, wormCanArea.height);
        g.drawString("Lata", wormCanArea.x + 15, wormCanArea.y + 30);
        g.drawString("Minhocas", wormCanArea.x, wormCanArea.y + 60);
        
        // Close button
        g.setColor(Color.RED);
        g.fillOval(closeButtonArea.x, closeButtonArea.y, closeButtonArea.width, closeButtonArea.height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString("X", closeButtonArea.x + 20, closeButtonArea.y + 45);
    }
}