package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class JellyFish extends Entity {
    private Startgame game;

    public JellyFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        // Try to load 3 frames: replace _1 with _2 and _3 if they exist
        String f1 = fileName;
        String f2 = fileName.contains("_1") ? fileName.replace("_1", "_2") : fileName.replace("1", "2");
        String f3 = fileName.contains("_1") ? fileName.replace("_1", "_3") : fileName.replace("1", "3");
        setFrames(new String[]{f1, f2, f3}, 220);
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Removes bait and hooked fish
    }
    
}
