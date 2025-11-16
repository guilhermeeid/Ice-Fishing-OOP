package OurGame.Model;

import java.awt.Graphics;
 import java.awt.Image;
 
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

    // draw the sprite at the given location
   public void draw(Graphics g, int x, int y) {
      g.drawImage(image, x, y, null);
   } 

} 
