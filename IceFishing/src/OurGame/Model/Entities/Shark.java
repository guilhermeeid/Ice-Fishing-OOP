package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;
import java.awt.Rectangle;

public class Shark extends Entity {
    private Startgame game;

    public Shark(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        // Try multiple conventions for an 'open mouth' frame:
        // 1) replace ".png" with "_open.png"
        // 2) replace "shark1" with "shark2" (common alternate sprite)
        String alt1 = fileName.replace(".png", "_open.png");
        String alt2 = fileName;
        if (fileName.contains("shark1")) {
            alt2 = fileName.replace("shark1", "shark2");
        } else if (fileName.contains("_1")) {
            alt2 = fileName.replace("_1", "_2");
        }
        setFrames(new String[]{fileName, alt1, alt2}, 120);
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
        // Place mouth area on the side the shark is facing:
        // if moving left (dx < 0) consider mouth on leftmost third, otherwise rightmost
        int mouthX = (getHorizontalMovement() < 0) ? getX() : getX() + (fullW - mouthW);
        int mouthY = getY();
        return new Rectangle(mouthX, mouthY, mouthW, mouthH);
    }
}
