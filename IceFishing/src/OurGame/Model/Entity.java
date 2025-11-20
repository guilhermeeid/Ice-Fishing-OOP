package OurGame.Model;

 import java.awt.*;

public abstract class Entity {
    protected double x;
    protected double y;
    protected Sprite sprite;
    protected double dx;
    protected double dy;
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
    public int getX(){
        return (int) x;
    }
    public int getY(){
        return (int) y;
    }

    public void draw(Graphics g){
        sprite.draw(g, (int)x, (int)y);
    }

    public void ownLogic() {}

    public boolean collidesWith(Entity other){
        myBounds.setBounds((int)x, (int)y, sprite.getWidth(), sprite.getHeight());
        himBounds.setBounds((int)other.getX(), (int)other.getY(), other.sprite.getWidth(), other.sprite.getHeight());
        return myBounds.intersects(himBounds);
    }

    public abstract void collidedWith(Entity other);

    public boolean closeBy(Entity other){
        myBounds.setBounds((int)x - 50, (int)y - 50, sprite.getWidth() + 100, sprite.getHeight() + 100);
        himBounds.setBounds((int)other.getX() -50, (int)other.getY() - 50, other.sprite.getWidth() + 100, other.sprite.getHeight() + 100);
        return myBounds.intersects(himBounds);
    }

    public void closeByed(Entity other) {}
}
