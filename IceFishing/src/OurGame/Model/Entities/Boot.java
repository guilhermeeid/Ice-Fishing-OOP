package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;
import java.awt.Graphics;

public class Boot extends Entity {
    private Startgame game;

    public Boot(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Removes only hooked fish, not bait
    }
    
    @Override
    public void draw(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        // Inverte a lógica: flip quando movimento for POSITIVO (direita)
        // Assim a bota original (virada para esquerda) fica correta indo para esquerda
        // E é invertida quando vai para direita
        boolean flip = getHorizontalMovement() > 0;
        currentSprite().draw(g, (int)x, (int)y, w, h, flip);
    }
}