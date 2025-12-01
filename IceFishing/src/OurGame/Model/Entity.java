package OurGame.Model;

 import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    protected double x;     
    protected double y;     // current position
    protected Sprite sprite;    
    protected double dx;    // speed in x direction
    protected double dy;    // speed in y direction
    public String fileName;

    // Animation frames (optional)
    protected Sprite[] frames = null;
    protected int frameIndex = 0;
    protected long lastFrameChange = 0;
    protected long frameDelay = 200; // ms

    private Rectangle myBounds = new Rectangle();
    private Rectangle himBounds = new Rectangle();

    protected double moveSpeed;

    public Entity(String fileName, int x, double y) {
        this.x = x;
        this.y = y;
        this.fileName = fileName;
        sprite = (SpriteStorage.get().getSprite(fileName));
    }

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
            // Ignore if frames can't be loaded
        }
    }

    public int getFrameCount() {
        return frames == null ? 0 : frames.length;
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
    	 sprite = (SpriteStorage.get()).getSprite(x);
    }
     
    public void setMoveSpeed(int moveSpeed) {
 	    this.moveSpeed = moveSpeed;
 	}

    public void move(long delta){
        x += dx * delta / 1000;
        y += dy * delta / 1000;
    }

    public void setHorizontalMovement(double dx){
        this.dx = dx;
    }
    public void setVerticalMovement(double dy){
        this.dy = dy;
    }
    public double getHorizontalMovement(){
        return dx;
    }
    public double getVerticalMovement(){
        return dy;
    }
    private static final double SCALE = 1.0 / 2.4; // escala aplicada às entidades

    public int getX(){
        return (int) x;
    }
    public int getY(){
        return (int) y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Retorna largura/altura escaladas (usadas para desenho e colisões)
    public int getWidth() {
        return (int) (currentSprite().getWidth() * SCALE);
    }

    public int getHeight() {
        return (int) (currentSprite().getHeight() * SCALE);
    }

    public void draw(Graphics g){
        int w = getWidth();
        int h = getHeight();
        boolean flip = getHorizontalMovement() < 0;
        currentSprite().draw(g, (int)x, (int)y, w, h, flip);
    }

    public void ownLogic() {
        if (frames != null && frames.length > 1) {
            long now = System.currentTimeMillis();
            if (now - lastFrameChange >= frameDelay) {
                frameIndex = (frameIndex + 1) % frames.length;
                lastFrameChange = now;
            }
        }
    }

    public boolean collidesWith(Entity other){
        myBounds.setBounds((int)x, (int)y, getWidth(), getHeight());
        himBounds.setBounds((int)other.getX(), (int)other.getY(), other.getWidth(), other.getHeight());
        return myBounds.intersects(himBounds);
    }

    public abstract void collidedWith(Entity other);

    public boolean closeBy(Entity other){
        myBounds.setBounds((int)x - 50, (int)y - 50, getWidth() + 100, getHeight() + 100);
        himBounds.setBounds((int)other.getX() -50, (int)other.getY() - 50, other.getWidth() + 100, other.getHeight() + 100);
        return myBounds.intersects(himBounds);
    }

    public void closeByed(Entity other) {}
}
