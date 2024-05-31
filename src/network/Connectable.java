package network;

import domain.models.RunningModeModel;
import ui.screens.RModeUI.GameStatusPanel;

public interface Connectable {
    Connectable getInstance();
    void sendScore(int i);
    void sendBarriersLeft(int i);
    void sendLives(int i);
    void sendSpell1();
    void sendSpell2();
    void sendSpell3();
    void setGSP(GameStatusPanel g);
    void setModel(RunningModeModel m);
}
