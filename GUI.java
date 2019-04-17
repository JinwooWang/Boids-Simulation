import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Container;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class GUI extends JPanel{

    public FishBoids fb = new FishBoids();
    //fish movement simulator
    private void fishBoidsSimulation()
    {
        fb.init();
        repaint();
        for(int i = 0;i < fb.getNumSteps();i ++)
        {
            fb.fishMovement();
            try {
                Thread.sleep (60);
            }
            catch (InterruptedException e) {
                break;
            }
            repaint();
        }
    }
    //paint canvas and fishes
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Point[] fishes = fb.getFishes();

        if(fishes == null)
        {
            return;
        }
        int radius = fb.getRadius();
        g.setColor(Color.gray);
        g.fillRect(0, 0, 2 * fb.getBounder() + fb.getWidth(), 2 * fb.getBounder() + fb.getHeight());
        g.setColor(Color.white);
        g.fillRect(fb.getBounder(), fb.getBounder(), fb.getWidth() + fb.getRadius(), fb.getHeight() + fb.getRadius());
        for(int i = 0;i < fishes.length;i ++)
        {
            Point fish = fishes[i];
            int topleftx = fish.x - radius;
            int toplefty =  2 * fb.getBounder() + fb.getHeight() - fish.y + radius;
            if (i == fb.getLeader())
                g.setColor(Color.red);
            else
                g.setColor(Color.blue);
            g.fillRoundRect(topleftx, toplefty, 2 * radius, 2 * radius, 10, 10);
            g.setColor(Color.black);
            g.drawRoundRect(topleftx, toplefty, 2 * radius, 2 * radius, 10, 10);
        }

    }
    //get frame
    public void getFrame()
    {
        JFrame frame = new JFrame();
        frame.setSize(2 * fb.getBounder() + fb.getWidth(), 3 * fb.getBounder() + fb.getHeight());
        frame.setTitle("Fish Boids Simulation");
        Container cPane = frame.getContentPane();
        cPane.add (this, BorderLayout.CENTER);
        frame.setVisible (true);
    }
    //main function
    public static void main(String[] argv)
    {
        GUI m = new GUI();
        m.getFrame();
        m.fishBoidsSimulation();
    }

}
