package OurGame.Model;

import javax.swing.*;
import java.awt.*;

public class Startgame extends JPanel {

    private Image backgroundImage;
    private Image iceLayer;
    private Image penguinImage;

    public Startgame() {
        // Load images from the classpath
        backgroundImage = new ImageIcon(getClass().getResource("/SpritesHD/Ocean_HD.png")).getImage();
        iceLayer = new ImageIcon(getClass().getResource("/SpritesHD/Ice_HD.png")).getImage();
        penguinImage = new ImageIcon(getClass().getResource("/SpritesHD/penguin_HD.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Ocean Background
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Ice Layer
        g2d.drawImage(iceLayer, 0, 0, getWidth(), getHeight(), this);

        g2d.drawImage(penguinImage, 910, 0, penguinImage.getWidth(null), penguinImage.getHeight(null), this);
    }
}
