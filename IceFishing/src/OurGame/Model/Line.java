package OurGame.Model;

import OurGame.Screens.Startgame;

public class Line extends Entity {
    private Startgame game;
    private boolean isCaught = false;


    public Line(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
        
    }
    
    @Override
    public void closeByed(Entity other) {
    // Not used for Line, only for Shark
    }
}
