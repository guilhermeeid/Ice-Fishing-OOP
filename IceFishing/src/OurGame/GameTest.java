package OurGame;
import javax.swing.JFrame;

import OurGame.Model.Startgame;


public class GameTest extends JFrame {
    public GameTest() {
        add(new Startgame());
        setTitle("Ice Fishing");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.setResizable(false);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new GameTest();
    
    }
}
