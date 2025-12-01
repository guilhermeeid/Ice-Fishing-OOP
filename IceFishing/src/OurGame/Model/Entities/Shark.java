package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class Shark extends Entity {
    private Startgame game;

    public Shark(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        // If multiple shark frames exist (e.g., mouth open), load them
        String alt1 = fileName.replace(".png", "_open.png");
        setFrames(new String[]{fileName, alt1}, 120);
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
}
