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
    private Boolean active=false;
    private int num=2;
    
    public Spell(){
        this.secondsElapsed = 0;
        this.duration = 30; // Set the duration of the spell

        // Create a timer to track the spell duration
        this.timer = new Timer(1000, e -> {
            if (secondsElapsed < duration) {
                secondsElapsed++;
                // Do something here if needed, like updating UI
            } else {
                // Stop the timer when spell duration is reached
                num--;
                resetTimer();
                if(num<0){resetTimer();}
                else {
                    stopTimer();
                    active = false;
                }
            }
        });

    }
    
    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
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

    public boolean getActive() {
        // TODO Auto-generated method stub
        return active;
    }

    public void setActive(boolean b) {
        // TODO Auto-generated method stub
        this.active=b;
    }
}
