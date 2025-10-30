import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
    
public class Homepage extends JPanel {
    Image backgroundImage;
    public Homepage() {
        ImageIcon icon = new ImageIcon("src/Sprites/pixil-layer-1.png");
        backgroundImage = icon.getImage();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
    
    public static void main(String[] args) throws Exception {
        
        JFrame frame = new JFrame("Homepage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.add(new Homepage(), BorderLayout.CENTER);
        
        frame.setVisible(true);
    }
}
