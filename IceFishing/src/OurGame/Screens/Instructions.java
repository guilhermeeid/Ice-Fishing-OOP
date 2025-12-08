package OurGame.Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import OurGame.GameTest;

public class Instructions extends JPanel {
    
    private Image background;
    private Font jerseyFont;

    public Instructions(GameTest frame) {

        background = new ImageIcon(getClass().getResource("/assets/sprites/ui/instructions_screen.png")).getImage();
        loadCustomFont();
        
        setLayout(null);

        JButton backButton = new JButton("");
        backButton.setBounds(1125, 713, 388, 93);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backButton.addActionListener((ActionEvent e) -> {
            frame.showScreen("Homepage");
        });
        add(backButton);
    }

    private void loadCustomFont() {
        try {
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/assets/fonts/Jersey10-Regular.ttf"));
            jerseyFont = baseFont.deriveFont(Font.BOLD, 28f);
        } catch (Exception e) {
            System.err.println("Error loading Jersey 10 font: " + e.getMessage());
            e.printStackTrace();
            jerseyFont = new Font("Arial", Font.BOLD, 28);
        }
    }

    public Font getJerseyFont(float size, int style) {
        if (jerseyFont != null) {
            return jerseyFont.deriveFont(style, size);
        }
        return new Font("Arial", style, (int) size);
    }

    public void drawUI(Graphics g) {
        g.setFont(getJerseyFont(70f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("Back", 1265, 777);

        g.setFont(getJerseyFont(60f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("MOVE", 35, 285);

        g.setFont(getJerseyFont(30f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("Move your mouse up", 35, 310);
        g.drawString("and down to raise and", 35, 330);
        g.drawString("lower your hook.", 35, 350);

        g.setFont(getJerseyFont(60f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("CHANGE BAIT", 1200, 65);

        g.setFont(getJerseyFont(30f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("Click on the box to use golden", 1200, 85);
        g.drawString("fish as a bait, and click", 1200, 105);
        g.drawString("in the metal can to use worm.", 1200, 125);

        g.setFont(getJerseyFont(60f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("FISHES", 35, 475);

        g.setFont(getJerseyFont(30f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("Mullet fish: eats golden fish", 390, 600);
        g.drawString("Gray fish: eats worms", 390, 720);
        g.drawString("Golden fish: eats worms", 390, 800);

        g.setFont(getJerseyFont(60f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("DANGER!", 795, 475);

        g.setFont(getJerseyFont(30f, Font.PLAIN));
        g.drawString("Shark: eats fish and take a life", 1100, 443);
        g.drawString("Jellyfish: shocks the line, takes the fish", 1100, 463);
        g.drawString("Boot: takes the fish", 1100, 483);
        g.drawString("Can: cuts the line and end the game", 1100, 503);

        g.setFont(getJerseyFont(60f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("LIFE", 855, 740);
        
        g.setFont(getJerseyFont(30f, Font.PLAIN));
        g.setColor(new Color(0x6B686B));
        g.drawString("1 worm = 1 life", 855, 765);
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        drawUI(g);
    }
}
