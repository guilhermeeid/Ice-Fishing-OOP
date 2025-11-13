package OurGame.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import OurGame.GameTest;

public class Instructions extends JPanel {

    public Instructions(GameTest frame) {
        setLayout(null);

        JLabel text = new JLabel("<html><center>Use o mouse para pescar peixes.<br>Evite os obstáculos!</center></html>", SwingConstants.CENTER);
        text.setFont(new Font("Arial", Font.PLAIN, 32));
        text.setBounds(430, 300, 700, 200);
        add(text);

        JButton backButton = new JButton("Voltar");
        backButton.setBounds(660, 600, 200, 60);
        backButton.addActionListener((ActionEvent e) -> {
            frame.showScreen("Homepage");
        });
        add(backButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
