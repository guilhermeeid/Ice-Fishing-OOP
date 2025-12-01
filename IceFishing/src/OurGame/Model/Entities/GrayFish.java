package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class GrayFish extends Entity {
    private Startgame game;

    public GrayFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        String alt = fileName.contains("-0") ? fileName.replace("-0", "-1") : fileName.replace("_0", "_1");
        setFrames(new String[]{fileName, alt}, 250);
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Handled in Startgame
    }
    
}