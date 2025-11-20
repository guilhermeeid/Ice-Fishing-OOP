package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;


public class Hook extends Entity {
    private int y;
    private Startgame game;

    public Hook(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Collision handling is done in Startgame
    }
    
    @Override
    public void closeByed(Entity other) {
        // Not used for Hook
    }
}
