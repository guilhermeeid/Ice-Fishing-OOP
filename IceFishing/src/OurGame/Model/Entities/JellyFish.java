package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class JellyFish extends Entity {
    private Startgame game;

    public JellyFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Removes bait and hooked fish
    }
    
}
