package OurGame.Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
 
public class Sprite {

  public Image image;
  
  public Sprite (Image i) {
    image = i;
  }

  public int getWidth() {
     return image.getWidth(null);
  }

  public int getHeight() {
     return image.getHeight(null);
  }

   // draw the sprite at the given location (natural size)
  public void draw(Graphics g, int x, int y) {
     g.drawImage(image, x, y, null);
  } 

  // draw the sprite scaled to the given width/height
  public void draw(Graphics g, int x, int y, int width, int height) {
     g.drawImage(image, x, y, width, height, null);
  }

  // draw the sprite scaled to the given width/height with optional horizontal flip
  public void draw(Graphics g, int x, int y, int width, int height, boolean flipHorizontal) {
     if (!flipHorizontal) {
        draw(g, x, y, width, height);
        return;
     }

     Graphics2D g2 = (Graphics2D) g;
     AffineTransform old = g2.getTransform();
     // Flip horizontally around the vertical center of the drawn image
     g2.translate(x + width, y);
     g2.scale(-1, 1);
     g2.drawImage(image, 0, 0, width, height, null);
     g2.setTransform(old);
  }

}
