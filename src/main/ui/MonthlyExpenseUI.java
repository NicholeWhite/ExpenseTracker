package ui;

import model.MonthlyTracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MonthlyExpenseUI extends JFrame implements ActionListener {
    private JLabel label;
    private JLabel label2;
    private JTextField field;
    private JTextField field2;

    private MonthlyTracker tracker;




    public MonthlyExpenseUI() {
        super("Monthly Expense Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13) );
        setLayout(new FlowLayout());
        JButton btn = new JButton("Change");
        btn.setActionCommand("myButton");
        btn.addActionListener(this); // Sets "this" object as an action listener for btn
        // so that when the btn is clicked,
        // this.actionPerformed(ActionEvent e) will be called.
        // You could also set a different object, if you wanted
        // a different object to respond to the button click
        label = new JLabel("flag");
        field = new JTextField(15);
        field2 = new JTextField(15);
        add(field);
        add(field2);
        add(btn);
        add(label);

        

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    //This is the method that is called when the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("myButton")) {
            label.setText(field.getText());
        }
    }


    
    public static void main(String[] args) {
        new MonthlyExpenseUI();
    }
}
