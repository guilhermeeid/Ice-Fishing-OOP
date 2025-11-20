package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class Shark extends Entity {
    private Startgame game;

    public Shark(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Removes bait and hooked fish
    }
    
    @Override
    public void closeByed(Entity other) {
        // Shark opens mouth when close to bait
    }
}
