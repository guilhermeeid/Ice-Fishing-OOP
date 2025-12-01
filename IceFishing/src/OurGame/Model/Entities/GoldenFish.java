package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class GoldenFish extends Entity {
    private Startgame game;

    private double moveSpeed = 0;

    public GoldenFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        // try to load a second frame (replace -0 with -1) if exists
        String alt = fileName.contains("-0") ? fileName.replace("-0", "-1") : fileName.replace("_0", "_1");
        setFrames(new String[]{fileName, alt}, 250);
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Handled in Startgame
    }
    
}
