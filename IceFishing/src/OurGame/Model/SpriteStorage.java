package OurGame.Model;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;


public class SpriteStorage {
    private static SpriteStorage single = new SpriteStorage();
    private HashMap<String, Sprite> sprites = new HashMap<>();

    public static SpriteStorage get(){
        return single;
    }

    public Sprite getSprite(String ref){
        
        if (sprites.get(ref) != null){
            return sprites.get(ref);
        }
        
        BufferedImage sourceImage = null;

        try{
            URL url = this.getClass().getResource(ref);
            if (url == null) {
                System.out.println("Resource not found: " + ref);
                System.exit(0);
            }
            sourceImage = ImageIO.read(url);
        }
        catch (IOException e){
            System.out.println("Failed to load image: " + ref);
            System.exit(0);
        }

        GraphicsConfiguration graphconf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = graphconf.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

      
        image.getGraphics().drawImage(sourceImage, 0, 0, null);

        Sprite sprite = new Sprite(image);
        sprites.put(ref, sprite);
      
        return sprite;
    }
}
