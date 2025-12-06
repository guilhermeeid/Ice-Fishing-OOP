package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class JellyFish extends Entity {
    private Startgame game;
    
    private static final long ANIMATION_SPEED = 220;
    private static final double BASE_SPEED = 50; // Mais lenta

    public JellyFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        setFramesAuto(fileName, ANIMATION_SPEED);
    }
    
    public static double getBaseSpeed() {
        return BASE_SPEED;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Removes bait and hooked fish
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();
        
        int w = getWidth();
        int h = getHeight();
        boolean movingRight = getHorizontalMovement() > 0;
        
        // Calcula o centro da sprite para rotação
        int centerX = (int)x + w / 2;
        int centerY = (int)y + h / 2;
        
        // Aplica rotação
        g2d.translate(centerX, centerY);
        
        if (movingRight) {
            // Movendo para direita: -25 graus (inclina para baixo à direita)
            g2d.rotate(Math.toRadians(25));
        } else {
            // Movendo para esquerda: +25 graus (inclina para baixo à esquerda)
            g2d.rotate(Math.toRadians(-25));
            // Flip horizontal para esquerda
            g2d.scale(-1, 1);
        }
        
        // Desenha a sprite centralizada na origem (que agora é o centro)
        currentSprite().draw(g2d, -w / 2, -h / 2, w, h);
        
        // Restaura a transformação original
        g2d.setTransform(oldTransform);
    }
    
    @Override
    public Rectangle getCollisionBounds() {
        int w = getWidth();
        int h = getHeight();
        boolean movingRight = getHorizontalMovement() > 0;
        
        // Centro da sprite
        double centerX = x + w / 2.0;
        double centerY = y + h / 2.0;
        
        // Cria retângulo base (não rotacionado)
        Rectangle2D baseRect = new Rectangle2D.Double(-w / 2.0, -h / 2.0, w, h);
        
        // Cria transformação de rotação
        AffineTransform transform = new AffineTransform();
        transform.translate(centerX, centerY);
        
        if (movingRight) {
            transform.rotate(Math.toRadians(25));
        } else {
            transform.rotate(Math.toRadians(-25));
        }
        
        // Aplica a rotação ao retângulo
        Area area = new Area(baseRect);
        area.transform(transform);
        
        // Retorna o bounding box da área rotacionada
        return area.getBounds();
    }
}