package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class Hook extends Entity {
    private Startgame game;
    private String defaultSpriteRef;

    public Hook(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        this.defaultSpriteRef = fileName;
    }
    
    @Override
    public void collidedWith(Entity other) {
    }
    
    @Override
    public void closeByed(Entity other) {
    }

    public void setHookSprite(String spriteRef) {
        if (spriteRef != null) changeSprite(spriteRef);
    }

    public void resetSprite() {
        if (defaultSpriteRef != null) changeSprite(defaultSpriteRef);
    }
}