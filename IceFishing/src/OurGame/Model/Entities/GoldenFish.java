package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class GoldenFish extends Entity {
    private Startgame game;
    
    // Configurações específicas do peixe dourado
    private static final long ANIMATION_SPEED = 250; // ms entre frames
    private static final double BASE_SPEED = 80; // velocidade base

    public GoldenFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        
        // Carrega animação automaticamente
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