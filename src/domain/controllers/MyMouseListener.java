package domain.controllers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;

public class MyMouseListener implements MouseListener {

    private Runnable mouseClickedAction;

    // Constructor
    public MyMouseListener() {
        // Default constructor
    }

    // Setter method
    public MyMouseListener setMouseClickedAction(Runnable mouseClickedAction) {
        this.mouseClickedAction = mouseClickedAction;
        return this; // Return the instance of MyMouseListener
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mouseClickedAction != null) {
            mouseClickedAction.run();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Implementation for mousePressed
        if (mouseClickedAction != null) {
            mouseClickedAction.run();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Implementation for mouseReleased
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JComponent component = (JComponent) e.getComponent();
        if(e.getSource() instanceof JButton){((JButton) component).setContentAreaFilled(true);}
        else{component.setOpaque(true);}
        component.setBackground(Color.WHITE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JComponent component = (JComponent) e.getComponent();
        if(e.getSource() instanceof JButton){((JButton) component).setContentAreaFilled(false);}
        else{component.setOpaque(false);}

    }
}
