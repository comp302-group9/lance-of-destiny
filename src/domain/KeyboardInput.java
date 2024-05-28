package domain;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import domain.interfaces.Observer;

public class KeyboardInput implements KeyListener{
    private boolean[] keys;
    private List<Observer> observers = new ArrayList<>();
    
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keys[keyCode] = true; // Mark the key as pressed
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    
    public boolean[] getKeys(){
        return keys;
    }
    
    public void attach(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers(int keyCode, boolean pressed) {
        for (Observer observer : observers) {
            observer.update(keyCode, pressed);
        }
    }
}
