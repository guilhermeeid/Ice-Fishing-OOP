package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class MulletFish extends Entity {
    private Startgame game;
    
    private static final long ANIMATION_SPEED = 200; // Mais rápido
    private static final double BASE_SPEED = 120; // Peixe mais veloz

    public MulletFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        setFramesAuto(fileName, ANIMATION_SPEED);
    }
    
    public static double getBaseSpeed() {
        return BASE_SPEED;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Handled in Startgame
    }
}