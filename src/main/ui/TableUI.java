package ui;
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import model.Expense;
import model.MonthlyTracker;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;


//Creates a new JTable that displays the expense data
public class TableUI extends JPanel {

    //EFFECTS: constructor that constructs the JTable with the desired data and adds a scroll
    // pane to the table
    public TableUI(MonthlyTracker tracker) {
        super(new GridLayout(1, 0));

        String[] columnNames = getCols();
        Object[][] data = getData(tracker);

        final JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 400));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);

        // https://stackoverflow.com/questions/2297991/how-to-set-header-for-jtable
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.black);
        header.setForeground(Color.WHITE);
    }

    //EFFECTS: returns the string array representation of the column header names
    private String[] getCols() {
        String[] columnNames = {"Expense Amount", "Description"};

        return columnNames;
    }

    //EFFECTS: Returns a multidimensional array representing the amount and
    // description of the expense
    private Object[][] getData(MonthlyTracker tracker) {
        List<Expense> expenses = tracker.getMonthlyExpenses();
        int row = 0;
        Object[][] data = new Object[100][100];

        for (Expense e : expenses) {
            data[row][0] = String.valueOf(e.getAmount());
            data[row][1] = e.getDescription();
            row++;

        }
        return data;
    }

    /**
     * EFFECTS: Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void makeTableGUI(MonthlyTracker month) {
        //Create and set up the window.
        JFrame frame = new JFrame(month.getMonth());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        TableUI newContentPane = new TableUI(month);
        newContentPane.setOpaque(true); //content panes must be opaque
        newContentPane.revalidate();


        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }


}