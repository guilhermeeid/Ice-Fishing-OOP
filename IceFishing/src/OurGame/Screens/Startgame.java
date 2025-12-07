package OurGame.Screens;

import OurGame.GameTest;
import OurGame.Model.Entities.*;
import OurGame.Model.Entity;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Startgame extends JPanel implements MouseMotionListener, MouseListener {

    private final Image backgroundImage;
    private final Image iceLayer;
    private final Image[] fishBoxImages = new Image[7];
    private final int[] fishBoxSrcWidths = new int[7];
    private final int[] fishBoxSrcHeights = new int[7];
    
    private final Image shockedOverlay;
    private boolean showShocked = false;
    private long shockedStartTime = 0L;
    private static final long SHOCKED_DURATION = 1000L;

    // Game Over screen
    private final Image gameOverScreen;
    private boolean isGameOverVisible = false;
    private Rectangle playAgainButton;
    private Rectangle homeButton;

    private Color normalLineColor = new Color(80, 60, 40);
    private Color shockedLineColor = new Color(0x5EC5FF);

    private Hook hook;

    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> removeEnt = new ArrayList<>();

    private boolean gameRunning = true;
    private int score;
    private int remainingWorms;
    private int caughtGoldenFish;
    private long startTime = 0;
    private long elapsedTime = 0;

    private enum BaitType {
        WORM, GOLDEN_FISH
    }
    private BaitType currentBait = BaitType.WORM;
    private Entity hookedFish = null;

    private int mouseY = 0;
    private final int hookX = 750;
    private int hookY = 190;
    private final int hookMinY = 190;
    private final int hookMaxY = 845;

    private Rectangle fishBoxArea = new Rectangle(280, 130, 280, 140);
    private Rectangle wormCanArea = new Rectangle(1155, 150, 55, 100);

    private Timer gameTimer;
    private Random random = new Random();
    private long lastSpawnTime = 0;
    private static final long SPAWN_INTERVAL = 2000;

    private GameTest frame;
    private JButton closeToHomeButton;

    private Font jerseyFont;

    public Startgame(GameTest frame) {
        this.frame = frame;

        loadCustomFont();

        // Load images
        backgroundImage = new ImageIcon(getClass().getResource("/assets/sprites/background/background_sea.png")).getImage();
        iceLayer = new ImageIcon(getClass().getResource("/assets/sprites/background/background_ice.png")).getImage();

        
        Image tempGameOver = null;
        try {
            URL gameOverUrl = getClass().getResource("/assets/sprites/ui/game_over_screen.png");
            if (gameOverUrl != null) {
                System.out.println("=== GAME OVER SCREEN DEBUG ===");
                System.out.println("URL found: " + gameOverUrl);
                System.out.println("Protocol: " + gameOverUrl.getProtocol());
                System.out.println("Full path: " + gameOverUrl.getPath());
                
                
                try {
                    
                    java.awt.image.BufferedImage buffered = javax.imageio.ImageIO.read(gameOverUrl);
                    if (buffered != null) {
                        tempGameOver = buffered;
                        System.out.println("Loaded via ImageIO - Dimensions: " + 
                                         buffered.getWidth() + "x" + buffered.getHeight());
                    }
                } catch (Exception e1) {
                    System.err.println("ImageIO failed: " + e1.getMessage());
                    
                    // Method 2: Toolkit (fallback)
                    tempGameOver = Toolkit.getDefaultToolkit().createImage(gameOverUrl);
                    MediaTracker tracker = new MediaTracker(this);
                    tracker.addImage(tempGameOver, 0);
                    tracker.waitForAll();
                    System.out.println("Loaded via Toolkit");
                }
                
                System.out.println("=== END DEBUG ===");
            } else {
                System.err.println("ERROR: Game Over screen URL is NULL!");
                System.err.println("Check if file exists at: /assets/sprites/ui/game_over_screen.png");
            }
        } catch (Exception e) {
            System.err.println("ERROR loading game over screen: " + e.getMessage());
            e.printStackTrace();
        }
        gameOverScreen = tempGameOver;

        
        playAgainButton = new Rectangle(680, 580, 250, 80); 
        homeButton = new Rectangle(990, 580, 250, 80);     

        // Load fish box variants
        String[] refs = new String[] {
            "/assets/sprites/ui/fish_box_empty.png",
            "/assets/sprites/ui/fish_box_01.png",
            "/assets/sprites/ui/fish_box_02.png",
            "/assets/sprites/ui/fish_box_03.png",
            "/assets/sprites/ui/fish_box_04.png",
            "/assets/sprites/ui/fish_box_05.png",
            "/assets/sprites/ui/fish_box_06.png"
        };
        Image emptyFallback = null;
        for (int i = 0; i < refs.length; i++) {
            try {
                URL u = getClass().getResource(refs[i]);
                if (u != null) {
                    ImageIcon ic = new ImageIcon(u);
                    fishBoxImages[i] = ic.getImage();
                    fishBoxSrcWidths[i] = ic.getIconWidth();
                    fishBoxSrcHeights[i] = ic.getIconHeight();
                    if (i == 0) emptyFallback = fishBoxImages[0];
                } else {
                    fishBoxImages[i] = null;
                    fishBoxSrcWidths[i] = 0;
                    fishBoxSrcHeights[i] = 0;
                }
            } catch (Exception ex) {
                fishBoxImages[i] = null;
                fishBoxSrcWidths[i] = 0;
                fishBoxSrcHeights[i] = 0;
            }
        }
        if (emptyFallback == null) {
            try {
                ImageIcon ic = new ImageIcon(getClass().getResource("/assets/sprites/ui/fish_box_empty.png"));
                emptyFallback = ic.getImage();
                fishBoxSrcWidths[0] = ic.getIconWidth();
                fishBoxSrcHeights[0] = ic.getIconHeight();
            } catch (Exception e) {
                emptyFallback = null;
                fishBoxSrcWidths[0] = 840;
                fishBoxSrcHeights[0] = 420;
            }
        }
        for (int i = 0; i < fishBoxImages.length; i++) {
            if (fishBoxImages[i] == null) {
                fishBoxImages[i] = emptyFallback;
                fishBoxSrcWidths[i] = fishBoxSrcWidths[0] > 0 ? fishBoxSrcWidths[0] : 840;
                fishBoxSrcHeights[i] = fishBoxSrcHeights[0] > 0 ? fishBoxSrcHeights[0] : 420;
            }
        }
        
        fishBoxArea = new Rectangle(280, 130, 280, 140);

        Image tmp = null;
        URL shockedUrl = getClass().getResource("/assets/sprites/background/background_ice_shocked.png");
        if (shockedUrl != null) {
            tmp = new ImageIcon(shockedUrl).getImage();
            System.out.println("Shocked overlay loaded: background_ice_shocked.png");
        } else {
            System.err.println("Shocked overlay not found, using normal ice layer");
            tmp = iceLayer;
        }
        shockedOverlay = tmp;

        setLayout(null);
        setFocusable(true);

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
    }

    private void loadCustomFont() {
        try {
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/assets/fonts/Jersey10-Regular.ttf"));
            jerseyFont = baseFont.deriveFont(Font.BOLD, 28f);
        } catch (Exception e) {
            System.err.println("Error loading Jersey 10 font: " + e.getMessage());
            e.printStackTrace();
            jerseyFont = new Font("Arial", Font.BOLD, 28);
        }
    }

    private Font getJerseyFont(float size, int style) {
        if (jerseyFont != null) {
            return jerseyFont.deriveFont(style, size);
        }
        return new Font("Arial", style, (int) size);
    }

    public void startGame() {
        entities.clear();
        removeEnt.clear();

        score = 0;
        remainingWorms = 3;
        caughtGoldenFish = 0;
        currentBait = BaitType.WORM;
        hookedFish = null;
        gameRunning = true;
        isGameOverVisible = false;

        startTime = System.currentTimeMillis();

        hook = new Hook(this, "/assets/sprites/player/hook_worm.png", hookX, hookY);
        entities.add(hook);

        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        gameTimer = new Timer(16, e -> gameLoop());
        gameTimer.start();
    }

    private void gameLoop() {
        if (!gameRunning) {
            if (!isGameOverVisible) {
                gameTimer.stop();
                showGameOver();
            }
            return;
        }

        long currentTime = System.currentTimeMillis();
        long delta = 16;

        hook.setY(hookY);
        hook.updateBaseX(hookX);

        for (Entity entity : entities) {
            entity.move(delta);
            entity.ownLogic();
        }

        for (Entity entity : entities) {
            if (entity instanceof OurGame.Model.Entities.Shark) {
                OurGame.Model.Entities.Shark s = (OurGame.Model.Entities.Shark) entity;
                boolean open = false;
                if (hookedFish != null) {
                    if (s.closeBy(hookedFish)) {
                        open = true;
                    }
                } else if (currentBait == BaitType.GOLDEN_FISH && hook != null) {
                    if (s.closeBy(hook)) {
                        open = true;
                    }
                }

                if (open) {
                    s.openMouth();
                } else {
                    s.closeMouth();
                }
            }
        }

        if (showShocked && (currentTime - shockedStartTime) > SHOCKED_DURATION) {
            showShocked = false;
        }

        checkCollisions();

        entities.removeAll(removeEnt);
        removeEnt.clear();

        if (currentTime - lastSpawnTime > SPAWN_INTERVAL) {
            spawnRandomEntity();
            lastSpawnTime = currentTime;
        }

        if (remainingWorms <= 0 && currentBait == BaitType.WORM && hookedFish == null && !showShocked) {
            gameRunning = false;
        }

        repaint();
    }

    private void spawnRandomEntity() {
        int type = random.nextInt(100);
        int startX = random.nextBoolean() ? -100 : 1920;
        int startY = 280 + random.nextInt(475);

        Entity newEntity = null;
        double speed = 0;

        if (type < 16) {
            newEntity = new GrayFish(this, "/assets/sprites/fish/fish_grey_swim_01.png", startX, startY);
            speed = GrayFish.getBaseSpeed() + random.nextInt(30);
        } else if (type < 50) {
            newEntity = new GoldenFish(this, "/assets/sprites/fish/fish_gold_swim_01.png", startX, startY);
            speed = GoldenFish.getBaseSpeed() + random.nextInt(40);
        } else if (type < 60) {
            newEntity = new MulletFish(this, "/assets/sprites/fish/fish_mullet_swin_01.png", startX, startY);
            speed = MulletFish.getBaseSpeed() + random.nextInt(50);
        } else if (type < 75) {
            newEntity = new Shark(this, "/assets/sprites/hazards/hazards_shark_01.png", startX, startY);
            speed = Shark.getBaseSpeed() + random.nextInt(60);
        } else if (type < 85) {
            newEntity = new JellyFish(this, "/assets/sprites/hazards/hazards_jellyfish_swin_01.png", startX, startY);
            speed = JellyFish.getBaseSpeed() + random.nextInt(20);
        } else if (type < 95) {
            newEntity = new Boot(this, "/assets/sprites/obstacles/boot.png", startX, startY);
            speed = 60 + random.nextInt(40);
        } else {
            newEntity = new MetalCan(this, "/assets/sprites/obstacles/metal_can.png", startX, startY);
            speed = 40 + random.nextInt(30);
        }

        if (newEntity != null) {
            newEntity.setHorizontalMovement(startX < 960 ? speed : -speed);
            entities.add(newEntity);
        }
    }

    private void checkCollisions() {
        for (Entity entity : entities) {
            if (entity == hook) {
                continue;
            }
            if (entity instanceof MetalCan) {
                int lineX = 767;
                int lineTop = 170;
                int lineBottom = hookY;
                int tol = 18;
                Rectangle lineRect = new Rectangle(lineX - tol, lineTop, tol * 2, Math.max(1, lineBottom - lineTop));
                Rectangle entRect = new Rectangle(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
                if (lineRect.intersects(entRect)) {
                    handleCollision(entity);
                    continue;
                }
            }

            if (entity instanceof Shark) {
                
                if (hookedFish != null || currentBait == BaitType.GOLDEN_FISH) {
                    if (hook.collidesWith(entity)) {
                        handleCollision(entity);
                    }
                }
            } else {
                if (hook.collidesWith(entity)) {
                    handleCollision(entity);
                }
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
                removeEnt.add(entity);
                entity.setHorizontalMovement(0);
                entity.setVerticalMovement(0);

                if (hook != null) {
                    if (entity instanceof GoldenFish) {
                        hook.setHookSprite("/assets/sprites/player/hook_gold_fish.png", -15);
                    } else if (entity instanceof GrayFish) {
                        hook.setHookSprite("/assets/sprites/player/hook_grey_fish.png", -20);
                    }
                }
            }
        } else if (entity instanceof MulletFish) {
            if (currentBait == BaitType.GOLDEN_FISH && hookedFish == null) {
                hookedFish = entity;
                removeEnt.add(entity);
                entity.setHorizontalMovement(0);
                entity.setVerticalMovement(0);
                currentBait = BaitType.WORM;

                if (hook != null) {
                    hook.setHookSprite("/assets/sprites/player/hook_mullet_fish.png", -55);
                }
            }
        } else if (entity instanceof JellyFish) {
            if (!entity.consumeInteractionOnce()) {
                return;
            }
            showShocked = true;
            shockedStartTime = System.currentTimeMillis();

            if (currentBait == BaitType.WORM) {
                remainingWorms--;
            } else {
                currentBait = BaitType.WORM;
                hook.resetSprite();
            }

            if (hookedFish != null) {
                removeEnt.add(hookedFish);
                hookedFish = null;
                if (hook != null) {
                    hook.resetSprite();
                }
            }
        } else if (entity instanceof Shark) {
            if (hookedFish != null) {
                removeEnt.add(hookedFish);
                hookedFish = null;
                if (hook != null) {
                    hook.resetSprite();
                }
            }
            
            if (currentBait == BaitType.WORM) {
                remainingWorms--;
            } else if (currentBait == BaitType.GOLDEN_FISH) {
                currentBait = BaitType.WORM;
                if (hook != null) {
                    hook.resetSprite();
                }
            }
        } else if (entity instanceof MetalCan) {
            gameRunning = false;
        } else if (entity instanceof Boot) {
            if (hookedFish != null) {
                removeEnt.add(hookedFish);
                hookedFish = null;
                if (hook != null) {
                    hook.resetSprite();
                }
            }
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
            if (hook != null) {
                hook.resetSprite();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isGameOverVisible) {
            return; // Don't move hook during Game Over
        }
        
        mouseY = e.getY();
        hookY = mouseY;

        if (hookY < hookMinY) {
            hookY = hookMinY;
        }
        if (hookY > hookMaxY) {
            hookY = hookMaxY;
        }

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

        // If Game Over is visible, check button clicks
        if (isGameOverVisible) {
            if (playAgainButton.contains(x, y)) {
                isGameOverVisible = false;
                startGame();
                return;
            }
            if (homeButton.contains(x, y)) {
                isGameOverVisible = false;
                frame.showScreen("Homepage");
                return;
            }
        }

        // Normal game logic
        if (fishBoxArea.contains(x, y) && hookY <= hookMinY + 20) {
            if (caughtGoldenFish > 0 && currentBait == BaitType.WORM && hookedFish == null) {
                currentBait = BaitType.GOLDEN_FISH;
                caughtGoldenFish--;
                score--;
                
                if (hook != null) {
                    hook.setHookSprite("/assets/sprites/player/hook_gold_fish.png", -15);
                }
            }
        }

        if (wormCanArea.contains(x, y) && hookY <= hookMinY + 20) {
            if (currentBait == BaitType.GOLDEN_FISH) {
                caughtGoldenFish++;
                score++;
                currentBait = BaitType.WORM;
                
                if (hook != null) {
                    hook.resetSprite();
                }
            }
        }
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

    private void showGameOver() {
        elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        isGameOverVisible = true;
        repaint();
    }

    public void removeEntity(Entity entity) {
        removeEnt.add(entity);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Ocean background
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Fishing line
        Color currentLineColor = showShocked ? shockedLineColor : normalLineColor;
        g.setColor(currentLineColor);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(767, 170, 767, hookY);

        // Entities
        for (Entity entity : entities) {
            entity.draw(g);
        }

        // Ice layer
        g.drawImage(iceLayer, 0, 0, getWidth(), getHeight(), this);

        // Shocked overlay
        if (showShocked && shockedOverlay != null) {
            g.drawImage(shockedOverlay, 0, 0, getWidth(), getHeight(), this);
        }

        // Fish Box
        int idx = caughtGoldenFish;
        if (idx < 0) idx = 0;
        if (idx > 6) idx = 6;
        Image boxImg = fishBoxImages[idx];
        if (boxImg == null) boxImg = fishBoxImages[0];
        int destW = 280;
        int destH = 140;
        int baseX = 280;
        int baseY = 130;
        int drawY = baseY;
        if (idx > 0) {
            drawY = baseY - 40;
            destH = 180;
        }
        g.drawImage(boxImg, baseX, drawY, destW, destH, this);

        // Normal UI
        drawUI(g);

        // Game Over Screen - drawn on top of everything
        if (isGameOverVisible) {
            drawGameOverScreen(g);
        }
    }

    private void drawUI(Graphics g) {
        g.setFont(getJerseyFont(80f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString(remainingWorms + "x", 70, 105);

        g.setFont(getJerseyFont(28f, Font.BOLD));
        g.setColor(Color.BLACK);
        g.drawString("Fish: " + score, 430, 225);

        g.setFont(getJerseyFont(20f, Font.PLAIN));
        g.setColor(Color.BLACK);

        if (hookY <= hookMinY + 20) {
            g.setColor(new Color(0x6B686B));
            g.drawString("Click on the box below to use gold fish as bait.",
                    fishBoxArea.x + -15,
                    fishBoxArea.y - 45);
        }

        if (hookY <= hookMinY + 20) {
            g.setColor(new Color(0x6B686B));
            g.drawString("Click on the metal can below to use worm as bait.",
                    wormCanArea.x + -5,
                    wormCanArea.y - 15);
        }

        long currentElapsed = (System.currentTimeMillis() - startTime) / 1000;
        g.setColor(new Color(0x6B686B));
        g.setFont(getJerseyFont(24f, Font.BOLD));
        g.drawString("Time: " + currentElapsed + "s", getWidth() - 185, 68);
    }

    private void drawGameOverScreen(Graphics g) {
        // Draw Game Over sprite at screen size (like background_ice)
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        
        if (gameOverScreen != null) {
            // Draw at full screen size
            g.drawImage(gameOverScreen, 0, 0, screenWidth, screenHeight, this);
        } else {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, screenWidth, screenHeight);
        }
        
        g.setFont(getJerseyFont(50f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        
        String fishText = score + " fish!";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(fishText);
        int textX = (screenWidth - textWidth) / 2 + 240;
        int textY = screenHeight / 2 + 140; 
        
        g.drawString(fishText, textX, textY);
        
        playAgainButton.setBounds(screenWidth / 2 - 245, screenHeight / 2 + 258, 230, 80);
        homeButton.setBounds(screenWidth / 2 + 10, screenHeight / 2 + 258, 230, 80);
    }
}