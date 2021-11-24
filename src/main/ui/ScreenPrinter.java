package ui;

import model.Event;
import model.EventLog;
import model.MonthlyTracker;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Code in this class is retrieved from:
 * https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
 *
 * Window closing from:
 * https://stackoverflow.com/questions/7613577/java-how-do-i-prevent-windowclosing-from-actually-closing-the-window
 *
 * Represents a screen printer for printing event log to screen.
 */
public class ScreenPrinter extends JInternalFrame implements LogPrinter {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private JTextArea logArea;
	
	/**
	 * Constructor sets up window in which log will be printed on screen
	 */
    public ScreenPrinter() {

        super("Event log", false, true, false, false);

        JFrame frame = new JFrame("Event Log");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                frame.dispose();
            }
        });

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane);

        scrollPane.setVisible(true);

        frame.setContentPane(scrollPane);

        //Display the window.
        frame.setSize(400,375);
        frame.setVisible(true);
    }

    @Override
    public void printLog(EventLog el) {
        for (Event next : el) {
            logArea.setText(logArea.getText() + next.toString() + "\n\n");
        }

        repaint();
    }

    @Override
    public void consolePrint(EventLog el) {
        for (Event next : el) {
            System.out.println(next);
        }
        el.clear();

    }

    @Override
    public void clearLog(EventLog el) {
        el.clear();

    }



}
