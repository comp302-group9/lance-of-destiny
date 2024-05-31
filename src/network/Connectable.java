package network;

import ui.screens.RModeUI.GameStatusPanel;

public interface Connectable {
    Connectable getInstance();
    void sendScore(int i);
    void sendBarriers(int i);
    void sendLives(int i);
    void setGSP(GameStatusPanel g);
}
