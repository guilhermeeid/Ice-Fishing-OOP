package OurGame.Model;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    protected double x;     
    protected double y;
    protected Sprite sprite;    
    protected double dx;
    protected double dy;
    public String fileName;

    // Sistema de animação melhorado
    protected Sprite[] frames = null;
    protected int frameIndex = 0;
    protected long lastFrameChange = 0;
    protected long frameDelay = 200;
    private boolean interactionConsumed = false;

    private Rectangle myBounds = new Rectangle();
    private Rectangle himBounds = new Rectangle();

    protected double moveSpeed;
    private static final double SCALE = 1.0 / 2.4;

    public Entity(String fileName, int x, double y) {
        this.x = x;
        this.y = y;
        this.fileName = fileName;
        sprite = SpriteStorage.get().getSprite(fileName);
    }

    /**
     * Carrega frames de animação automaticamente baseado no padrão de nomes.
     * Detecta automaticamente quantos frames existem (_01, _02, _03, etc).
     * 
     * @param baseSpritePath caminho base (ex: "/assets/sprites/fish/fish_gold_swim_01.png")
     * @param delayMs tempo entre frames em milissegundos
     */
    public void setFramesAuto(String baseSpritePath, long delayMs) {
        try {
            List<Sprite> list = new ArrayList<>();
            
            // Detectar o padrão: _01, _02, _03 ou similar
            String basePath = baseSpritePath;
            int frameNum = 1;
            
            // Tentar carregar frames sequencialmente até falhar
            while (frameNum <= 10) { // limite de 10 frames por segurança
                String framePath = generateFramePath(baseSpritePath, frameNum);
                URL u = this.getClass().getResource(framePath);
                
                if (u != null) {
                    list.add(SpriteStorage.get().getSprite(framePath));
                    frameNum++;
                } else {
                    break; // Para quando não encontrar mais frames
                }
            }
            
            // Se encontrou frames, configura a animação
            if (!list.isEmpty()) {
                frames = list.toArray(new Sprite[0]);
                frameIndex = 0;
                frameDelay = delayMs;
                lastFrameChange = System.currentTimeMillis();
                System.out.println("Loaded " + frames.length + " frames for: " + baseSpritePath);
            } else {
                System.err.println("No animation frames found for: " + baseSpritePath);
            }
        } catch (Exception ex) {
            System.err.println("Error loading frames: " + ex.getMessage());
        }
    }

    /**
     * Gera o caminho do frame baseado no número.
     * Suporta padrões: _01, _02, _03
     */
    private String generateFramePath(String basePath, int frameNumber) {
        // Substitui o número do frame no padrão _01, _02, etc
        String pattern = String.format("_%02d", frameNumber);
        
        // Se o caminho já tem _01, substitui
        if (basePath.contains("_01")) {
            return basePath.replace("_01", pattern);
        }
        
        // Se não tem, tenta adicionar antes da extensão
        if (basePath.endsWith(".png")) {
            return basePath.replace(".png", pattern + ".png");
        }
        
        return basePath + pattern + ".png";
    }

    /**
     * Método legado para compatibilidade (pode ser removido depois)
     */
    public void setFrames(String[] refs, long delayMs) {
        try {
            List<Sprite> list = new ArrayList<>();
            for (String r : refs) {
                URL u = this.getClass().getResource(r);
                if (u != null) {
                    list.add(SpriteStorage.get().getSprite(r));
                }
            }
            if (!list.isEmpty()) {
                frames = list.toArray(new Sprite[0]);
                frameIndex = 0;
                frameDelay = delayMs;
                lastFrameChange = System.currentTimeMillis();
            }
        } catch (Exception ex) {
            System.err.println("Error in setFrames: " + ex.getMessage());
        }
    }

    public int getFrameCount() {
        return frames == null ? 0 : frames.length;
    }

    public boolean consumeInteractionOnce() {
        if (interactionConsumed) return false;
        interactionConsumed = true;
        return true;
    }

    public void setFrameIndex(int idx) {
        if (frames != null && idx >= 0 && idx < frames.length) {
            frameIndex = idx;
        }
    }

    protected Sprite currentSprite() {
        if (frames != null && frames.length > 0) return frames[frameIndex];
        return sprite;
    }

    public void changeSprite(String x) {
        sprite = SpriteStorage.get().getSprite(x);
    }
     
    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void move(long delta) {
        x += dx * delta / 1000;
        y += dy * delta / 1000;
    }

    public void setHorizontalMovement(double dx) {
        this.dx = dx;
    }
    
    public void setVerticalMovement(double dy) {
        this.dy = dy;
    }
    
    public double getHorizontalMovement() {
        return dx;
    }
    
    public double getVerticalMovement() {
        return dy;
    }

    public int getX() {
        return (int) x;
    }
    
    public int getY() {
        return (int) y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return (int) (currentSprite().getWidth() * SCALE);
    }

    public int getHeight() {
        return (int) (currentSprite().getHeight() * SCALE);
    }

    public void draw(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        boolean flip = getHorizontalMovement() < 0;
        currentSprite().draw(g, (int)x, (int)y, w, h, flip);
    }

    /**
     * Atualiza a lógica da entidade (animações, etc)
     */
    public void ownLogic() {
        // Atualizar animação
        if (frames != null && frames.length > 1) {
            long now = System.currentTimeMillis();
            if (now - lastFrameChange >= frameDelay) {
                frameIndex = (frameIndex + 1) % frames.length;
                lastFrameChange = now;
            }
        }
    }

    public boolean collidesWith(Entity other) {
        myBounds.setBounds((int)x, (int)y, getWidth(), getHeight());
        Rectangle otherBounds = other.getCollisionBounds();
        return myBounds.intersects(otherBounds);
    }

    public Rectangle getCollisionBounds() {
        return new Rectangle((int)x, (int)y, getWidth(), getHeight());
    }

    public abstract void collidedWith(Entity other);

    public boolean closeBy(Entity other) {
        myBounds.setBounds((int)x - 50, (int)y - 50, getWidth() + 100, getHeight() + 100);
        himBounds.setBounds((int)other.getX() - 50, (int)other.getY() - 50, other.getWidth() + 100, other.getHeight() + 100);
        return myBounds.intersects(himBounds);
    }

    public void closeByed(Entity other) {}
}