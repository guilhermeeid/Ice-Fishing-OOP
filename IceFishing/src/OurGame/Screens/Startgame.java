package OurGame.Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import OurGame.GameTest;
import OurGame.Model.Entity;
import OurGame.Model.Entities.*;

public class Startgame extends JPanel implements MouseMotionListener, MouseListener {

    private final Image backgroundImage;
    private final Image iceLayer;
    private final Image fishBoxImage;

    private Hook hook;
    
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> removeEnt = new ArrayList<>();

    // Game State
    private boolean gameRunning = true; 
    private int score;
    private int remainingWorms;
    private int caughtGoldenFish;
    private long startTime = 0;
    private long elapsedTime = 0;

    private enum BaitType { WORM, GOLDEN_FISH }
    private BaitType currentBait = BaitType.WORM;
    private Entity hookedFish = null;

    // Hook position
    private int mouseY = 0;
    private int hookX = 760;
    private int hookY = 190;
    private final int hookMinY = 190;
    private final int hookMaxY = 845;

    // UI clickable areas - AJUSTADAS para as posições das imagens
    private Rectangle fishBoxArea = new Rectangle(290, 100, 280, 140);
    private Rectangle wormCanArea = new Rectangle(1155, 150, 55, 100);

    private Timer gameTimer;
    private Random random = new Random();
    private long lastSpawnTime = 0;
    private static final long SPAWN_INTERVAL = 2000;

    private GameTest frame;
    private JButton closeToHomeButton;

    public Startgame(GameTest frame) {
        this.frame = frame;

        // Load images
        backgroundImage = new ImageIcon(getClass().getResource("/SpritesHD/Ocean_HD.png")).getImage();
        iceLayer = new ImageIcon(getClass().getResource("/assets/sprites/background/background_ice.png")).getImage();
        fishBoxImage = new ImageIcon(getClass().getResource("/SpritesHD/Box_0.png")).getImage();

        setLayout(null);
        setFocusable(true);

        // Close button
        closeToHomeButton = new JButton("");
        closeToHomeButton.setBounds(1450, 20, 65, 65);
        closeToHomeButton.setBorderPainted(false);
        closeToHomeButton.setContentAreaFilled(false);
        closeToHomeButton.setFocusPainted(false);
        closeToHomeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        closeToHomeButton.addActionListener((ActionEvent e) -> {
            if (gameTimer != null && gameTimer.isRunning()) {
                gameTimer.stop();
            }
            frame.showScreen("Homepage");
        });

        add(closeToHomeButton);
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
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        gameTimer = new Timer(16, e -> gameLoop());
        gameTimer.start();
    }

    private void gameLoop() {
        if (!gameRunning) {
            gameTimer.stop();
            showGameOver();
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        long delta = 16;
        
        // Update hook position - CORRIGIDO
        hook.setY(hookY);
        
        // Move all entities
        for (Entity entity : entities) {
            entity.move(delta);
            entity.ownLogic();
        }

        // Shark proximity: open mouth when close to hook
        for (Entity entity : entities) {
            if (entity instanceof OurGame.Model.Entities.Shark) {
                OurGame.Model.Entities.Shark s = (OurGame.Model.Entities.Shark) entity;
                if (s.closeBy(hook)) s.openMouth(); else s.closeMouth();
            }
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
        if (remainingWorms <= 0 && currentBait == BaitType.WORM && hookedFish == null) {
            gameRunning = false;
        }
        
        repaint();
    }

    private void spawnRandomEntity() {
        int type = random.nextInt(100);
        int startX = random.nextBoolean() ? -100 : 1920;
        int startY = 280 + random.nextInt(600);
        double speed = 50 + random.nextInt(100);
        
        Entity newEntity = null;
        
        if (type < 16) {
            newEntity = new GrayFish(this, "/SpritesHD/GreyFish-0_HD.png", startX, startY);
        } else if (type < 50) {
            newEntity = new GoldenFish(this, "/SpritesHD/GoldFish-0_HD.png", startX, startY);
        } else if (type < 60) {
            newEntity = new MulletFish(this, "/SpritesHD/MulletFish_1_HD.png", startX, startY);
        } else if (type < 75) {
            newEntity = new Shark(this, "/SpritesHD/shark1_HD.png", startX, startY);
        } else if (type < 85) {
            newEntity = new JellyFish(this, "/SpritesHD/JellyFish_1_HD.png", startX, startY);
        } else if (type < 95) {
            newEntity = new Boot(this, "/SpritesHD/Boot_HD.png", startX, startY);
        } else {
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
            
            if (hook.collidesWith(entity)) {
                handleCollision(entity);
            }
            
            if (entity.getX() < -200 || entity.getX() > 2100) {
                removeEnt.add(entity);
            }
        }
    }

    private void handleCollision(Entity entity) {
        if (entity instanceof GrayFish || entity instanceof GoldenFish) {
            if (currentBait == BaitType.WORM && hookedFish == null) {
                hookedFish = entity;
                // mark for removal so it won't be drawn twice (removed after collision loop)
                removeEnt.add(entity);
                entity.setHorizontalMovement(0);
                entity.setVerticalMovement(0);
                // update hook sprite according to bait and fish type
                if (hook != null) {
                    if (currentBait == BaitType.WORM) {
                        hook.setHookSprite("/SpritesHD/warm_hooked.png");

                    } else {
                        if (entity instanceof OurGame.Model.Entities.GoldenFish) {
                            hook.setHookSprite("/SpritesHD/yellow_hooked.png");
                        } else if (entity instanceof OurGame.Model.Entities.GrayFish) {
                            hook.setHookSprite("/SpritesHD/grey_hooked.png");
                        } else {
                            hook.setHookSprite("/SpritesHD/warm_hooked.png");
                        }
                    }
                }
            }
        } else if (entity instanceof MulletFish) {
            if (currentBait == BaitType.GOLDEN_FISH && hookedFish == null) {
                hookedFish = entity;
                // mark for removal so it won't be drawn twice (removed after collision loop)
                removeEnt.add(entity);
                entity.setHorizontalMovement(0);
                entity.setVerticalMovement(0);
                caughtGoldenFish--;
                currentBait = BaitType.WORM;
                if (hook != null) {
                    // use grey hooked for mullet (fallback)
                    hook.setHookSprite("/SpritesHD/grey_hooked.png");
                }
            }
        } else if (entity instanceof Shark || entity instanceof JellyFish) {
            if (currentBait == BaitType.WORM) {
                remainingWorms--;
            } else {
                caughtGoldenFish--;
                currentBait = BaitType.WORM;
            }
            if (hookedFish != null) {
                // ensure hooked fish is removed (it may have been removed from entities earlier)
                removeEnt.add(hookedFish);
                hookedFish = null;
                if (hook != null) hook.resetSprite();
            }
            removeEnt.add(entity);
        } else if (entity instanceof MetalCan) {
            gameRunning = false;
        } else if (entity instanceof Boot) {
            if (hookedFish != null) {
                removeEnt.add(hookedFish);
                hookedFish = null;
                if (hook != null) hook.resetSprite();
            }
            removeEnt.add(entity);
        }
    }

    private void checkSurfaceCapture() {
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
            if (hook != null) hook.resetSprite();
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
        
        // Fish box - equip golden fish as bait
        if (fishBoxArea.contains(x, y) && hookY <= hookMinY + 20) {
            if (caughtGoldenFish > 0 && currentBait == BaitType.WORM && hookedFish == null) {
                currentBait = BaitType.GOLDEN_FISH;
                caughtGoldenFish--;
            }
        }
        
        // Worm can - return to worm bait
        if (wormCanArea.contains(x, y) && hookY <= hookMinY + 20) {
            if (currentBait == BaitType.GOLDEN_FISH) {
                caughtGoldenFish++;
                currentBait = BaitType.WORM;
            }
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

        // Ocean background
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        
        // DESENHAR LINHA DE PESCA (do pinguim até o anzol)
        g.setColor(new Color(80, 60, 40)); // Cor marrom da linha
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(767, 170, 767, hookY);
        
        // DESENHAR ENTIDADES (peixes, anzol, etc)
        for (Entity entity : entities) {
            entity.draw(g);
        }

        // Desenhar peixe fisgado separadamente (foi removido de `entities` ao ser fisgado)
        if (hookedFish != null) {
            hookedFish.setX(hookX - hookedFish.getWidth() / 2);
            hookedFish.setY(hookY + 30);
            hookedFish.draw(g);
        }
        
        // Ice layer (sobrepõe a linha acima do gelo)
        g.drawImage(iceLayer, 0, 0, getWidth(), getHeight(), this);

        // Fish Box
        g.drawImage(fishBoxImage, 290, 100, 280, 140, this);
        
        // DESENHAR UI (textos e informações)
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(Color.BLACK);
        
        // Contadores (canto superior esquerdo)
        g.drawString("🪱 Minhocas: " + remainingWorms, 30, 50);
        g.drawString("🎣 Pescados: " + score, 30, 90);
        g.drawString("🐟 Dourados: " + caughtGoldenFish, 30, 130);
        
        // Isca atual
        String baitIcon = currentBait == BaitType.WORM ? "🪱" : "🐟";
        String baitText = currentBait == BaitType.WORM ? "Minhoca" : "Peixe Dourado";
        g.setColor(Color.BLACK);
        g.drawString("Isca: " + baitIcon + " " + baitText, 30, 170);
        
        // Labels das áreas clicáveis
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        
        // Label da caixa de peixes
        if (hookY <= hookMinY + 20) {
            g.setColor(Color.BLACK);
            g.drawString("↓ Clique para usar Peixe Dourado", 
                fishBoxArea.x + 20, 
                fishBoxArea.y -20);
        }
        
        // Label da lata de minhocas
        if (hookY <= hookMinY + 20) {
            g.setColor(Color.BLACK);
            g.drawString("↓ Clique para usar Minhoca", 
                wormCanArea.x + 20, 
                wormCanArea.y - 30);
        }
        
        // Timer
        long currentElapsed = (System.currentTimeMillis() - startTime) / 1000;
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("⏱️ Tempo: " + currentElapsed + "s", getWidth() - 200, 50);
        
        // Debug info (pode remover depois)
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Hook Y: " + hookY + " Hook X: " + hookX + " | Entities: " + entities.size(), 30, getHeight() - 20);
    }
}
