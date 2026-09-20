import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SimulationPanel extends JPanel {

    Sandbox sandbox;

    public SimulationPanel(Sandbox sandbox) {
        this.sandbox = sandbox;

        // Converting mouse to pixels and allows us to spawn our material
        MouseAdapter mouse = new MouseAdapter() {

            void place(MouseEvent e) {

                int col = e.getX() / Sandbox.CELL_SIZE;
                int row = e.getY() / Sandbox.CELL_SIZE;

                // Collision detection to ensure that we cant click outside of the simulated
                // enviroment/grid//
                if (row >= 0 && row < sandbox.grid.getRows() &&
                        col >= 0 && col < sandbox.grid.getCols()) {
//
                    Elements selected = sandbox.tester.get();
//
                    // fire needs its own object per pixel so each one burns out on its own timer//
                    Elements toPlace = (selected instanceof Fire) ? new Fire() : selected;
                    try {
                        sandbox.grid.set(row, col, toPlace);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("Error placing element: " + ex.getMessage());
                    }
                    repaint();
                }
            }

            // mous events for click and click+drag
            @Override
            public void mousePressed(MouseEvent e) {
                place(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                place(e);
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        // Simulation Timer
        Timer timer = new Timer(16, e -> {
            sandbox.step();
            repaint();
        });

        timer.start();
    }

    // called whenever the panel needs to redraw
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int s = Sandbox.CELL_SIZE;
//
        try {
            for (int row = 0; row < sandbox.grid.getRows(); row++) {
                for (int col = 0; col < sandbox.grid.getCols(); col++) {
//
                    Elements element = sandbox.grid.get(row, col);
//
                    if (element != null) {
//
                        g.setColor(element.getColor());
//
                        g.fillRect(
                                col * s,
                                row * s,
                                s,
                                s);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Rendering error: Invalid grid position - " + e.getMessage());
        }
    }
}