package OurGame.Model;

 import java.awt.*;

public abstract class Entity {
    protected double x;     
    protected double y;     // current position
    protected Sprite sprite;    
    protected double dx;    // speed in x direction
    protected double dy;    // speed in y direction
    public String fileName;

    private Rectangle myBounds = new Rectangle();
    private Rectangle himBounds = new Rectangle();

    protected double moveSpeed;

    public Entity(String fileName, int x, double y) {
        this.x = x;
        this.y = y;
        this.fileName = fileName;
        sprite = (SpriteStorage.get().getSprite(fileName));
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
        return (int) (sprite.getWidth() * SCALE);
    }

    public int getHeight() {
        return (int) (sprite.getHeight() * SCALE);
    }

    public void draw(Graphics g){
        int w = getWidth();
        int h = getHeight();
        boolean flip = getHorizontalMovement() < 0;
        sprite.draw(g, (int)x, (int)y, w, h, flip);
    }

    public void ownLogic() {}

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
