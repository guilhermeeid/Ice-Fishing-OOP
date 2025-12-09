package OurGame;

import javax.swing.*;
import OurGame.Screens.*;
import OurGame.Audio.SoundManager;

import java.awt.*;

public class GameTest extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Homepage homepage;
    private Instructions instructions;
    private Startgame startgame;
    private SoundManager bgm;

    public GameTest() {
        setTitle("Ice Fishing");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        bgm = new SoundManager("/assets/sounds/themeicefishing.wav");

        homepage = new Homepage(this, bgm);
        instructions = new Instructions(this, bgm);
        startgame = new Startgame(this, bgm);

        mainPanel.add(homepage, "Homepage");
        mainPanel.add(instructions, "Instructions");
        mainPanel.add(startgame, "Startgame");
        

        add(mainPanel);
        setVisible(true);

        bgm.playLoop(-12f);
    }

    public void startGame() {
        if (startgame != null) startgame.startGame();
    }

    
    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
        switch (name) {
            case "Homepage":
                bgm.playLoop(-12f);
                break;
            case "Instructions":
                bgm.playLoop(-12f);
                break;
            case "Startgame":
                bgm.playLoop(-6f);
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        new GameTest();
    }
}
