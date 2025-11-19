package OurGame.Screens;

import javax.swing.*;
import java.awt.*;
import OurGame.Model.Entity;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import OurGame.Model.Line;

public class Startgame extends JPanel implements MouseMotionListener {

    private Image backgroundImage;
    private Image iceLayer;
    private Image penguinImage;

    private Entity hook;

    private Boolean gameRunning = true;
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private ArrayList<Entity> removeEnt = new ArrayList<Entity>();

    private int score = 0;

    private int remainingWarms = 3;

    private long startTime = 0;
    private long currentTime = 0;

    private int mouseY = 0;
    private int hookX = 960;     //idk the real center now, will fix later.
    private int hookY = 0;       //The initial position of the hook is under the top of line, and will be updated later.
    private int hookMinY = 100;  //Suposing the ice layer is at y = 100. (To be adjusted later)
    private int hookMaxY = 1080; //Same as above.

    public Startgame() {
        // Load images from the classpath
        backgroundImage = new ImageIcon(getClass().getResource("/SpritesHD/Ocean_HD.png")).getImage();
        iceLayer = new ImageIcon(getClass().getResource("/SpritesHD/Ice_HD.png")).getImage();
        penguinImage = new ImageIcon(getClass().getResource("/SpritesHD/fishing_penguin.png")).getImage();

        startEntities();
    }

    private void startEntities() {
        // We don't have hook image yet, so using a placeholder for now.
        hook = new Line(this, "/SpritesHD/hook.png", hookX, hookY);
        entities.add(hook);
    }

    public void removeEntity(Entity entity) {
           removeEnt.add(entity);
    }
     

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseY = e.getY();   

        hookY = mouseY;     

        if (hookY < hookMinY) hookY = hookMinY;
        if (hookY > hookMaxY) hookY = hookMaxY;

        repaint(); 
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Ocean Background
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Ice Layer
        g.drawImage(iceLayer, 0, 0, getWidth(), getHeight(), this);

        //need to resize the penguin properly later @gui
        g.drawImage(penguinImage, 300, 0, penguinImage.getWidth(null), penguinImage.getHeight(null), this);
    }
}
