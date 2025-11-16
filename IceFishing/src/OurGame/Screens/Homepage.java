package OurGame.Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import OurGame.GameTest;

public class Homepage extends JPanel {

    public Homepage(GameTest frame) {
        setLayout(null); // controle manual

        JButton startButton = new JButton("Start Game");
        startButton.setBounds(660, 400, 300, 60);
        startButton.addActionListener((ActionEvent e) -> {
            frame.showScreen("Startgame");
        });

        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.setBounds(660, 480, 300, 60);
        instructionsButton.addActionListener((ActionEvent e) -> {
            frame.showScreen("Instructions");
        });

        add(startButton);
        add(instructionsButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Exemplo de fundo colorido
        g.setColor(new Color(135, 206, 250));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 80));
        g.drawString("ICE FISHING", 570, 300);
    }
}
