package OurGame.Model;

import OurGame.Screens.Startgame;

// ========== GOLDEN FISH ==========
class GoldenFish extends Entity {
    private Startgame game;

    public GoldenFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Handled in Startgame
    }
    
    @Override
    public void closeByed(Entity other) {}
}

// ========== GRAY FISH ==========
class GrayFish extends Entity {
    private Startgame game;

    public GrayFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Handled in Startgame
    }
    
    @Override
    public void closeByed(Entity other) {}
}

// ========== MULLET FISH ==========
class MulletFish extends Entity {
    private Startgame game;

    public MulletFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Handled in Startgame
    }
    
    @Override
    public void closeByed(Entity other) {}
}

// ========== SHARK ==========
class Shark extends Entity {
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
    public void closeByed(Entity other) {}
}

// ========== JELLYFISH ==========
class Jellyfish extends Entity {
    private Startgame game;

    public Jellyfish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Removes bait and hooked fish
    }
    
    @Override
    public void closeByed(Entity other) {}
}

// ========== METAL CAN ==========
class MetalCan extends Entity {
    private Startgame game;

    public MetalCan(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Cuts the line - ends game
    }
    
    @Override
    public void closeByed(Entity other) {}
}

// ========== BOOT ==========
class Boot extends Entity {
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
    public void closeByed(Entity other) {}
}