package ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

// Code for this class retrieved from:
// http://www.java2s.com/Tutorials/Java/Swing_How_to/JWindow/Create_Swing_Splash_screen_with_progress_bar.htm

//Class represents a simple loading splash screen
class SplashScreen extends JWindow {
    static JProgressBar progressBar = new JProgressBar();
    static int count = 1;
    static int TIMER_PAUSE = 3;
    static int PROGBAR_MAX = 100;
    static Timer progressBarTimer;
    ActionListener al = new ActionListener() {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            progressBar.setValue(count);
            if (PROGBAR_MAX == count) {
                progressBarTimer.stop();
                SplashScreen.this.setVisible(false);
                createAndShowFrame();
            }
            count++;
        }
    };

    //Constructor that creates a splash screen
    public SplashScreen() {
        Container container = getContentPane();

        JPanel panel = new JPanel();
        container.add(panel, BorderLayout.CENTER);

        panel.add(new JLabel(new ImageIcon("data/money.png")));

        progressBar.setMaximum(PROGBAR_MAX);
        container.add(progressBar, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        startProgressBar();
    }

    //MODIFIES: this
    // EFFECTS: sets a time for progress bar
    private void startProgressBar() {
        progressBarTimer = new Timer(TIMER_PAUSE, al);
        progressBarTimer.start();
    }

    //EFFECTS: creates and shows a JFrame
    private void createAndShowFrame() {
        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false);
    }
}