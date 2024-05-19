package domain.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {

    private Runnable keyTypedAction;
    private Runnable keyPressedAction;
    private Runnable keyReleasedAction;

    // Constructor
    public MyKeyListener() {
        // Default constructor
    }

    // Setter methods
    public MyKeyListener setKeyTypedAction(Runnable keyTypedAction) {
        this.keyTypedAction = keyTypedAction;
        return this; // Return the instance of MyKeyListener
    }

    public MyKeyListener setKeyPressedAction(Runnable keyPressedAction) {
        this.keyPressedAction = keyPressedAction;
        return this; // Return the instance of MyKeyListener
    }

    public MyKeyListener setKeyReleasedAction(Runnable keyReleasedAction) {
        this.keyReleasedAction = keyReleasedAction;
        return this; // Return the instance of MyKeyListener
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (keyTypedAction != null) {
            keyTypedAction.run();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (keyPressedAction != null) {
            keyPressedAction.run();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (keyReleasedAction != null) {
            keyReleasedAction.run();
        }
    }
}

