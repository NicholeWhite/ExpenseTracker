package ui;

import exception.LogException;
import model.Event;
import model.EventLog;
import model.Expense;
import model.MonthlyTracker;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;


/**
 * This class uses the framework provided by:
 * https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
 * TextInputDemo.java uses these additional files:
 *   SpringUtilities.java
 *   ...
 *
 *   Code in this class also uses the JOptionPane for the error/message dialogs:
 *   https://docs.oracle.com/javase/8/docs/api/javax/swing/JOptionPane.html
 *
 *   Additionally, the dataToTable() function uses the JTable for the data table:
 *   https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
 *
 *   Icon use reference from:
 *   https://stackoverflow.com/questions/1614772/how-to-change-jframe-icon
 *   and for resizing:
 *   http://www.nullpointer.at/2011/08/21/java-code-snippets-howto-resize-an-imageicon/#comment-11870
 *
 *   Icon from:
 *   https://www.flaticon.com/free-icon/coin_217853
 *   and
 *   https://pixabay.com/vectors/money-cash-coin-dollars-usd-gold-5698019/
 *
 *   The timer component is referenced from:
 *   https://stackoverflow.com/questions/1006611/java-swing-timer
 *
 *   Button Panel can be attributed to:
 *   https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
 *
 *   Scroll panels based on:
 *   https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html
 *
 */

// Class represents the main interactive UI for this project, allows user add, clear, save and load expense.
public class MonthlyExpenseUI extends JPanel implements ActionListener, FocusListener {
    private static final String JSON_STORE = "./data/expenseListData.json";
    JTextField expenseField;
    JTextField descriptionField;
    JSpinner monthSpinner;
    boolean expenseSet = false;
    Font regularFont;
    Font italicFont;
    JLabel messageDisplay;
    static final int GAP = 10;

    private MonthlyTracker expenseList;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    LogPrinter lp;

    //EFFECTS: Class constructor that calls the initializer and sets the dimensions for the panel
    // adds the fields to the panel
    public MonthlyExpenseUI() {
        super(new GridLayout(1, 0));

        init();

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel leftHalf = new JPanel() {
            //Don't allow us to stretch vertically.
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE,
                        pref.height);
            }
        };
        leftHalf.setLayout(new BoxLayout(leftHalf,
                BoxLayout.PAGE_AXIS));
        leftHalf.add(createEntryFields());
        leftHalf.add(createButtons());

        add(leftHalf);
        add(createMessageDisplay());

    }


    //EFFECTS: Creates interactive buttons for the JPanel and returns the panel
    // with the buttons added
    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,2));

        JButton button = new JButton("Add Expense");
        button.addActionListener(this);
        button.setActionCommand("add");
        buttonPanel.add(button);

        button = new JButton("Clear All");
        button.addActionListener(this);
        button.setActionCommand("clear");
        buttonPanel.add(button);

        button = new JButton("Save Work");
        button.addActionListener(this);
        button.setActionCommand("save");
        buttonPanel.add(button);

        button = new JButton("Print Log");
        button.addActionListener(this);
        button.setActionCommand("print");
        buttonPanel.add(button);
        panel.add(buttonPanel);

        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, GAP - 5,  GAP - 5));
        return panel;
    }


    /**
     * MODIFIES: this, TableUI
     * EFFECTS: Called when the user clicks the button or presses enter in the text field
     * Options are to clear all expenses, save expenses to file, or adds the entry expense
     * and displays it on the table GUI
     */
    public void actionPerformed(ActionEvent e) {
        if ("clear".equals(e.getActionCommand())) {
            expenseSet = false;
            expenseField.setText("");
            descriptionField.setText("");
            expenseList.clearAll();
            TableUI.makeTableGUI(expenseList);

        } else if ("save".equals(e.getActionCommand())) {
            saveToFile();
        } else if ("print".equals(e.getActionCommand())) {

            doAction();
        } else if ("add".equals(e.getActionCommand())) {
            if (checkValid(expenseField, descriptionField) != true) {
                return;
            }
            Expense expense = new Expense(Float.valueOf(expenseField.getText()), descriptionField.getText());
            expenseList.addExpense(expense);

            expenseList.setMonth((String) monthSpinner.getValue());
            expenseSet = true;

            // Generates a new instance of the table GUI each time an expense is added
            TableUI.makeTableGUI(expenseList);
        }

        updateDisplays();
    }

    //Modifies: this, LogPrinter
    //Effects: Logs the entries into the LogPrinter
    public void doAction() {
        try {
            lp = new ScreenPrinter();
            lp.printLog(EventLog.getInstance());

        } catch (LogException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // EFFECTS: saves work to a file and opens a message dialog show it was successful
    public void saveToFile() {
        try {
            jsonWriter.open();
            jsonWriter.write(expenseList);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,
                    "Progress saved.", "Save Successful!", JOptionPane.PLAIN_MESSAGE);

        } catch (FileNotFoundException exception) {
            JOptionPane.showMessageDialog(null,
                    "Unable to write to file...", "Error! ", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: returns false if the expense or description is not valid, true otherwise
    public boolean checkValid(JTextField expense, JTextField description) {
        String validExpense = isValidExpense(expense);

        if (validExpense != "") {
            JOptionPane.showMessageDialog(null,
                    validExpense, "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String validDescription = isValidDescription(description);
        if (validDescription != "") {
            JOptionPane.showMessageDialog(null,
                    validDescription, "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // EFFECTS: returns a message if the expense is invalid, returns empty string otherwise
    // invalid if negative, non-number, or 0 in value
    public String isValidExpense(JTextField expense) {
        String errorMessage = "";

        try {
            Float.parseFloat(expense.getText());
            float val = Float.parseFloat(expense.getText());
            if (val < 0) {
                errorMessage = "Expense must not be negative";

            } else if (val == 0) {
                errorMessage = "Expense must not be zero";
            }
        } catch (NumberFormatException ex) {
            errorMessage = "Expense must be a valid number ";
        }

        return errorMessage;
    }

    // EFFECTS: returns a message if the description is invalid, returns empty string otherwise
    // invalid if negative, non-number, or 0 in value
    public String isValidDescription(JTextField description) {
        String errorMessage = "";
        String empty = "";

        if (description.getText().matches(".*\\d.*")) {
            errorMessage = "Description must not contain numbers";
        }

        if ((description.getText() == null) || empty.equals(description.getText()))  {
            errorMessage = "Description must not be empty";
        }
        return errorMessage;
    }


    //MODIFIES: this
    //EFFECTS: updates the message displayed on the UI
    protected void updateDisplays() {
        messageDisplay.setText(formatMessage());
        if (expenseSet) {
            messageDisplay.setFont(regularFont);
        } else {
            messageDisplay.setFont(italicFont);
        }
    }

    //MODIFIES: this
    //EFFECTS: arranges a panel layout for a message to be displayed on the UI
    // and updatesDisplays when called
    protected JComponent createMessageDisplay() {
        JPanel panel = new JPanel(new BorderLayout());
        messageDisplay = new JLabel();
        messageDisplay.setHorizontalAlignment(JLabel.CENTER);
        regularFont = messageDisplay.getFont().deriveFont(Font.PLAIN,
                16.0f);
        italicFont = regularFont.deriveFont(Font.ITALIC);
        updateDisplays();

        //Lay out the panel.
        panel.setBorder(BorderFactory.createEmptyBorder(GAP / 2, //top
                0,     //left
                GAP / 2, //bottom
                0));   //right
        panel.add(new JSeparator(JSeparator.VERTICAL),
                BorderLayout.LINE_START);
        panel.add(messageDisplay,
                BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 150));

        return panel;
    }

    //EFFECTS: Formats a message with the expense, description , and month printed for the user to
    // see. If expense is not set, then prints "No Expenses Added."
    protected String formatMessage() {
        if (!expenseSet) {
            return "No Expenses Added.";
        }

        String expense = expenseField.getText();
        String description = descriptionField.getText();
        String month = (String) monthSpinner.getValue();

        String empty = "";

        if ((expense == null) || empty.equals(expense)) {
            expense = "<em>(no expense specified)</em>";
        }
        if ((description == null) || empty.equals(description)) {
            description = "<em>(no description specified)</em>";
        }
        if ((month == null) || empty.equals(month)) {
            month = "<em>(no month specified)</em>";
        }
        String sb = stringBufferHelper(expense, description, month);

        return sb;
    }

    //EFFECTS: string buffer helper that adds the format for the string that
    // will be displayed on the UI when an expense is added
    protected String stringBufferHelper(String expense, String description, String month) {
        StringBuffer sb = new StringBuffer();

        sb.append("<html><p align=center>");
        sb.append("<em>Added Expense:");
        sb.append("<br>");
        sb.append("$");
        sb.append(expense);
        sb.append("<br>");
        sb.append(description);
        sb.append(" ");
        sb.append("<br> To the month:");
        sb.append(month);
        sb.append(" ");

        return sb.toString();
    }

     //EFFECTS: Called when one of the fields gets the focus so that
     // we can select the focused field.
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }

    //EFFECTS: Workaround for formatted text field focus side effects.
    protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField)c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

    //EFFECTS: Needed for FocusListener interface.
    public void focusLost(FocusEvent e) { } //ignore

    //MODIFIES: this
    //REQUIRES: Creates the UI fields that the user can enter the data
    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
                "Expense: ",
                "Description: ",
                "Month: ",
        };

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        expenseField = new JTextField();
        expenseField.setColumns(15);
        fields[fieldNum++] = expenseField;

        descriptionField = new JTextField();
        descriptionField.setColumns(15);
        fields[fieldNum++] = descriptionField;

        String[] stateStrings = getMonth();
        monthSpinner = new JSpinner(new SpinnerListModel(stateStrings));
        fields[fieldNum++] = monthSpinner;

        //Associate label/field pairs, add everything,
        //and lay it out.
        panelLayout(panel, labelStrings, labels, fields);
        SpringUtilities.makeCompactGrid(panel, labelStrings.length, 2,
                GAP, GAP, //init x,y
                GAP, GAP / 2);//xpad, ypad
        return panel;
    }

    //MODIFIES: this, panel, labels
    //EFFECTS: adds labels to the panel and adds listeners to each field
    //Associate label/field pairs, add everything, and lay it out.
    private void panelLayout(JPanel panel, String[] labelStrings, JLabel[] labels, JComponent[] fields) {
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i],
                    JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf = null;
            if (fields[i] instanceof JSpinner) {
                tf = getTextField((JSpinner) fields[i]);
            } else {
                tf = (JTextField) fields[i];
            }
            tf.addActionListener(this);
            tf.addFocusListener(this);
        }
    }

    // EFFECTS: Returns a string list of the months in the year
    public String[] getMonth() {
        String[] month = { "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };
        return month;
    }

    //EFFECTS: returns the text field that the spinner is associated to, in this case the month
    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: "
                    + spinner.getEditor().getClass()
                    + " isn't a descendant of DefaultEditor");
            return null;
        }
    }


    //EFFECTS: Used when application is exited to print the event log onto console
    public static void onExit() {
        LogPrinter l;
        l = new ScreenPrinter();
        l.consolePrint(EventLog.getInstance());

    }


    // EFFECTS: Create the GUI and sets it to be visible.
    // Loads image and sets it to be the app icon
    private static void createAndShowGUI() {
        //Create and set up the window.

        JFrame frame = new JFrame("Expense Tracker");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                onExit();

            }
        });

        //Resizes icon image
        ImageIcon img = new ImageIcon("data/coin.png");
        Image image = img.getImage(); // transform it
        Image newimg = image.getScaledInstance(110, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        img = new ImageIcon(newimg);

        //Sets icon image
        frame.setIconImage(img.getImage());

        frame.add(new MonthlyExpenseUI());

        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }

    //MODIFIES: this
    //EFFECTS: asks user if would like to load previous saved work if it exists
    // If yes, loads previous expense list
    // If no, does nothing
    public void loadPrevious() {
        //Checks if file exists before asking user
        try {
            jsonReader.read();
        } catch (IOException e) {
            return;
        }

        int n = JOptionPane.showConfirmDialog(null,
                "Would you to continue the last save?",
                "Load Previous Work ", JOptionPane.YES_NO_OPTION);

        if (n == 0) {
            try {
                expenseList = jsonReader.read();
                expenseList.load();
                EventLog.getInstance().clear();
                expenseList.logExpenses();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Error loading file...", "Error! ", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            EventLog.getInstance().clear();
        }
    }


    // MODIFIES: this
    // EFFECTS: initializes expenses and a monthly tracker
    // prompts user to load expense
    private void init() {
        expenseList = new MonthlyTracker();
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        loadPrevious();

    }


    //EFFECTS: main function that is called and generates the UI
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            //EFFECTS: runs program and generates UI, sets a timer between the two panel components
            public void run() {

                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);

                ActionListener taskPerformer = new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        SplashScreen splashScreen = new SplashScreen();
                    }
                };

                Timer splashTimer = new Timer(50, taskPerformer);
                splashTimer.setRepeats(false);
                splashTimer.start();

                ActionListener timerForGUI = new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        createAndShowGUI();
                    }
                };

                Timer guiTimer = new Timer(3000, timerForGUI);
                guiTimer.setRepeats(false);
                guiTimer.start();

            }
        });
    }
}
