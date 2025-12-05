package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;
import java.awt.Rectangle;

public class Shark extends Entity {
    private Startgame game;
    
    private static final long ANIMATION_SPEED = 180; // Animação mais rápida
    private static final double BASE_SPEED = 150; // Muito rápido!

    public Shark(Startgame game, String fileName, int x, double y) {
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
    public void closeByed(Entity other) {
        // Shark opens mouth when close to bait
    }

    public void openMouth() {
        if (getFrameCount() > 1) setFrameIndex(1);
    }

    public void closeMouth() {
        if (getFrameCount() > 0) setFrameIndex(0);
    }

    @Override
    public Rectangle getCollisionBounds() {
        // Reduce shark hitbox to the right third (mouth area)
        int fullW = getWidth();
        int fullH = getHeight();
        int mouthW = Math.max(1, fullW / 3);
        int mouthH = fullH;
        int mouthX = (getHorizontalMovement() < 0) ? getX() : getX() + (fullW - mouthW);
        int mouthY = getY();
        return new Rectangle(mouthX, mouthY, mouthW, mouthH);
    }
}