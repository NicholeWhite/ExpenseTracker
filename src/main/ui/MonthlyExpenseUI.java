package ui;

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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.*;


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
 */
public class MonthlyExpenseUI extends JPanel implements ActionListener, FocusListener {
    private static final String JSON_STORE = "./data/expenseListData.json";
    JTextField expenseField;
    JTextField descriptionField;
    //JFormattedTextField zipField;
    JSpinner monthSpinner;
    boolean expenseSet = false;
    boolean isCleared = false;
    Font regularFont;
    Font italicFont;
    JLabel addressDisplay;
    static final int GAP = 10;
    private MonthlyTracker expenseList;
    private Expense expense;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


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
        add(createAddressDisplay());


        //creates JPANEL
        TableUI.makeTableGUI(expenseList);

    }



    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Add Expense");
        button.addActionListener(this);
        button.setActionCommand("add");
        panel.add(button);

        button = new JButton("Clear");
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);

        button = new JButton("Save Work");
        button.addActionListener(this);
        button.setActionCommand("save");
        panel.add(button);



        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, GAP - 5,  GAP - 5));
        return panel;
    }

    /**
     * Called when the user clicks the button or presses
     * Enter in a text field.
     */
    public void actionPerformed(ActionEvent e) {
        if ("clear".equals(e.getActionCommand())) {
            expenseSet = false;
            isCleared = true;
            expenseField.setText("");
            descriptionField.setText("");

            //We can't just setText on the formatted text
            //field, since its value will remain set.
            //zipField.setValue(null);

        } else if ("save".equals(e.getActionCommand())) {
            saveToFile();
        } else {
            if (checkValid(expenseField, descriptionField) != true) {
                return;
            }
            Expense expense = new Expense(Float.valueOf(expenseField.getText()), descriptionField.getText());
            expenseList.addExpense(expense);
            System.out.println(expenseList.viewExpenses());
            expenseSet = true;
            isCleared = false;
            System.out.println("here");
            dataToTable();
            System.out.println("here2");
        }

        updateDisplays();
        TableUI.makeTableGUI(expenseList);

    }


    public void dataToTable() {
//        String[] columnNames = {"Expense",
//                "Description",
//                "Total Expenses"};
//
//        Object[][] data = {
//                {"Kathy", "Smith",},
//                {"John", "Doe",},
//                {"Sue", "Black",},
//                {"Jane", "White",},
//                {"Joe", "Brown",}
//        };
//
//        JTable table = new JTable(data, columnNames);
//
//        table.setVisible(true);
//        System.out.println(table);
        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() {
                return 10;
            }

            public int getRowCount() {
                return 10;
            }

            public Object getValueAt(int row, int col) {
                return new Integer(row * col);
            }
        };
        JTable table = new JTable(dataModel);
        JScrollPane scrollpane = new JScrollPane(table);
    }

    // MODIFIES: this
    // EFFECTS: saves work to a file and opens a mesasage dialog show it was successful
    //
    public void saveToFile() {
        try {
            jsonWriter.open();
            jsonWriter.write(expenseList);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,
                    "Progress saved.", "Save Successful!", JOptionPane.PLAIN_MESSAGE);

        } catch (FileNotFoundException exception) {
            JOptionPane.showMessageDialog(null,
                    "Uh oh! Unable to write to file...", "Error! ", JOptionPane.ERROR_MESSAGE);
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

    protected void updateDisplays() {
        addressDisplay.setText(formatAddress());
        if (expenseSet) {
            addressDisplay.setFont(regularFont);
        } else {
            addressDisplay.setFont(italicFont);
        }
    }

    protected JComponent createAddressDisplay() {
        JPanel panel = new JPanel(new BorderLayout());
        addressDisplay = new JLabel();
        addressDisplay.setHorizontalAlignment(JLabel.CENTER);
        regularFont = addressDisplay.getFont().deriveFont(Font.PLAIN,
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
        panel.add(addressDisplay,
                BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 150));

        return panel;
    }

    protected String formatAddress() {
        if (!expenseSet) {
            if (isCleared) {
                return " ";
            }
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
        sb.append(month); //should format
        sb.append(" ");

        return sb.toString();
    }

    //A convenience method for creating a MaskFormatter.
    protected MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }

    /**
     * Called when one of the fields gets the focus so that
     * we can select the focused field.
     */
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }

    //Workaround for formatted text field focus side effects.
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

    //Needed for FocusListener interface.
    public void focusLost(FocusEvent e) { } //ignore


    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
                "Expense: ",
                "Description: ",
                "Month: ",
              //  "Zip code: "
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

        //Field = new JFormattedTextField(
               // createFormatter("#####"));
        //fields[fieldNum++] = zipField;

        //Associate label/field pairs, add everything,
        //and lay it out.
        panelLayout(panel, labelStrings, labels, fields);
        SpringUtilities.makeCompactGrid(panel, labelStrings.length, 2,
                GAP, GAP, //init x,y
                GAP, GAP / 2);//xpad, ypad
        return panel;
    }


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

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("TextInputDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        JFrame table = new JFrame("tableDemo");
//        table.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("");
        //Add contents to the window.
        frame.add(new MonthlyExpenseUI());
//        table.add(new TableUI());


        //Display the window.
        frame.pack();
        frame.setVisible(true);

//        table.pack();
//        table.setVisible(true);


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
                isCleared = true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Error loading file...", "Error! ", JOptionPane.ERROR_MESSAGE);
            }
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
        System.out.println(expenseList.viewExpenses());

    }



    public static void main(String[] args) {

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();

            }
        });
    }
}
