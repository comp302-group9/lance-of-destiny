package domain.objects.Ymir;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class YmirController {
    private YmirModel model;
    private YmirView view;
    private YmirContext context;
    private Timer timer;

    public YmirController(YmirModel model, YmirView view) {
        this.model = model;
        this.view = view;
        this.context = new YmirContext(model, view);

        // Initialize the timer
        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.request();
                timer.setDelay(context.getCurrentStateDelay());
            }
        });

        // Perform the first state transition immediately
        context.request();
        // Set the timer delay for the initial state
        timer.setDelay(context.getCurrentStateDelay());
    }

    public void start() {
        timer.start();
    }
}
