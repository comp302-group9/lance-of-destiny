package domain.objects.Spells;

import java.awt.image.BufferedImage;

import javax.swing.Timer;

public abstract class Spell {
    protected String name;
    protected BufferedImage Img;
    protected Boolean isStorable;
    private Timer timer;
    private int secondsElapsed;
    private int duration = 30; // Duration of the spell in seconds
    protected Boolean active=false;
    private int num=2;
    
    public Spell(){
        this.secondsElapsed = 0;
        
        // Create a timer to track the spell duration
        this.timer = new Timer(1000, e -> {
            if (secondsElapsed < duration) {
                secondsElapsed++;
                // Do something here if needed, like updating UI
            } else {
                // Stop the timer when spell duration is reached
                num--;
                resetTimer();
                deActivate();
                if(num<0){resetTimer();}
                else {
                    stopTimer();
                    active = false;
                }
            }
        });

    }

    public abstract void Activate();
    public abstract void deActivate();
    
    public void startTimer() {
        setActive(true);
        timer.start();
    }

    public void stopTimer() {
        setActive(false);
        timer.stop();
    }

    public void resetTimer() {
        secondsElapsed = 0;
    }

    public BufferedImage getImg(){
        return Img;
    }

    public double getSecondsElapsed() {
        // TODO Auto-generated method stub
        return secondsElapsed;
    }

    public double getDuration() {
        // TODO Auto-generated method stub
        return duration;
    }

    public int getNum() {
        // TODO Auto-generated method stub
        return num;
    }

    public Timer getTimer() {
        // TODO Auto-generated method stub
        return timer;
    }

    public boolean getActive() {
        // TODO Auto-generated method stub
        return active;
    }

    public void setActive(boolean b) {
        // TODO Auto-generated method stub
        this.active=b;
    }
}
