package org.example.indiecure_aplication.Model.Utils;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class controls the thread attach to the homeScreen's clock
 */
public class IndicureThread extends Thread {

    public Boolean stop = false;

    private Label label;

    public IndicureThread() {

    }
    public IndicureThread(Label labelP) {
        this.label = labelP;
    }

    @Override
    public void run() {

        while (!stop) {
            Platform.runLater(() -> {
                Date date = new Date();
                SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
                label.setText(formatTime.format(date));
            });
            try {
                this.sleep(1000);
            } catch (InterruptedException ie) {
                Logger.getLogger(IndicureThread.class.getName()).log(Level.SEVERE, null, ie);
            }
        }
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }
}
