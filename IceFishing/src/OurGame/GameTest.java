package OurGame;

import javax.swing.*;
import OurGame.Screens.*;

import java.awt.*;

public class GameTest extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public GameTest() {
        setTitle("Ice Fishing");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        Homepage homepage = new Homepage(this);
        Instructions instructions = new Instructions(this);
        Startgame startgame = new Startgame();

        mainPanel.add(homepage, "Homepage");
        mainPanel.add(instructions, "Instructions");
        mainPanel.add(startgame, "Startgame");

        add(mainPanel);
        setVisible(true);
    }

    
    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        new GameTest();
    }
}
