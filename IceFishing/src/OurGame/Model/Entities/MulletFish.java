package OurGame.Model.Entities;

import OurGame.Model.Entity;
import OurGame.Screens.Startgame;

public class MulletFish extends Entity {
    private Startgame game;

    public MulletFish(Startgame game, String fileName, int x, double y) {
        super(fileName, x, y);
        this.game = game;
        // Try to load 3 frames (1,2,3) if available
        String f1 = fileName;
        String f2 = fileName.contains("_1") ? fileName.replace("_1", "_2") : fileName.replace("1", "2");
        String f3 = fileName.contains("_1") ? fileName.replace("_1", "_3") : fileName.replace("1", "3");
        setFrames(new String[]{f1, f2, f3}, 200);
    }
    
    @Override
    public void collidedWith(Entity other) {
        // Handled in Startgame
    }
    

}
