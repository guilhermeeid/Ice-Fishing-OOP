package OurGame.Model;
import javax.swing.JPanel;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Startgame extends JPanel {

    private Image backgroundImage;

    public Startgame() {
        ImageIcon icon = new ImageIcon("SpritesHD\\Background_HD.png");
        backgroundImage = icon.getImage();
    }

    public void paint(Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        graphics.drawImage(backgroundImage, 0, 0, null);
        g.dispose();
    }
    
}
