package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class Hook extends Entity {
    private Startgame game;
    private String defaultSpriteRef;
    private int xOffset = 0; // Offset X para sprites específicas
    private int baseX; // Guarda a posição X base

    public Hook(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        this.defaultSpriteRef = fileName;
        this.baseX = x; // Salva a posição X original
    }
    
    @Override
    public void collidedWith(Entity other) {
    }
    
    @Override
    public void closeByed(Entity other) {
    }

    // Define sprite com offset X
    public void setHookSprite(String spriteRef, int offsetX) {
        if (spriteRef != null) {
            changeSprite(spriteRef);
            this.xOffset = offsetX;
            // Atualiza a posição X real da entidade
            this.x = baseX + offsetX;
        }
    }
    
    // Mantém compatibilidade (sem offset)
    public void setHookSprite(String spriteRef) {
        setHookSprite(spriteRef, 0);
    }

    public void resetSprite() {
        if (defaultSpriteRef != null) {
            changeSprite(defaultSpriteRef);
            this.xOffset = 0;
            // Volta para a posição base
            this.x = baseX;
        }
    }
    
    // Método para atualizar a posição base (chamado pelo game loop)
    public void updateBaseX(int newBaseX) {
        this.baseX = newBaseX;
        this.x = baseX + xOffset;
    }
    
    // Override do setX para manter o baseX sincronizado
    @Override
    public void setX(int x) {
        this.baseX = x;
        this.x = baseX + xOffset;
    }
    
    public int getXOffset() {
        return xOffset;
    }
}