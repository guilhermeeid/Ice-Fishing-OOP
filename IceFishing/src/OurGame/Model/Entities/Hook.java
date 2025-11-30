package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class Hook extends Entity {
    private Startgame game;

    public Hook(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
    }
    
    @Override
    public void closeByed(Entity other) {
    }
}
