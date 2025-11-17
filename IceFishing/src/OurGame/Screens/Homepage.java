package OurGame.Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import OurGame.GameTest;

public class Homepage extends JPanel {

    private Image background;

    public Homepage(GameTest frame) {

        background = new ImageIcon(getClass().getResource("/SpritesHD/Start Screen.png")).getImage();

        setLayout(null);

        JButton startButton = new JButton("");
        startButton.setBounds(570, 360, 400, 90);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        startButton.addActionListener((ActionEvent e) -> {
            frame.showScreen("Startgame");
        });

        JButton instructionsButton = new JButton("");
        instructionsButton.setBounds(570, 495, 400, 90);
        instructionsButton.setBorderPainted(false);
        instructionsButton.setContentAreaFilled(false);
        instructionsButton.setFocusPainted(false);
        instructionsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        instructionsButton.addActionListener((ActionEvent e) -> {
            frame.showScreen("Instructions");
        });

        add(startButton);
        add(instructionsButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
