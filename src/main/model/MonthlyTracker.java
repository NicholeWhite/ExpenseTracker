package model;

import java.util.ArrayList;
import java.util.List;

// Represents a list of expenses, each expense has an associated value and description
public class MonthlyTracker {
    private List<Expense> monthlyExpenses;
    private int size;

    // Constructor that creates a new array list and sets the size to 0 and expenseSum to 0
    public MonthlyTracker() {
        this.monthlyExpenses = new ArrayList<>();
        this.size = 0;
    }

    // MODIFIES: this
    // EFFECTS: Adds the expense to the back of the array list
    public void addExpense(Expense e) {
        monthlyExpenses.add(e);
        size++;
    }

    // REQUIRES: monthlyExpenses must not be empty
    // MODIFIES: this
    // EFFECTS: removes the expense from the list.
    public void removeExpense(Expense e) {
        for (Expense ex: monthlyExpenses) {
            if (ex.getAmount() == e.getAmount() && ex.getDescription().equals(e.getDescription())) {
                this.monthlyExpenses.remove(ex);
                size--;
                break;
            }
        }
    }


    // EFFECTS: returns the sum of the expenses in monthlyExpenses
    public float sumExpenses() {
        float expenseSum = 0;
        for (Expense e: monthlyExpenses) {
            expenseSum += e.getAmount();
        }
        return expenseSum;
    }

    // REQUIRES: monthlyExpenses has 1 or more expenses
    // EFFECTS: adds each expense in monthlyExpenses to monthlyExpenseList
    //          returns the list of expenses as a list of strings
    public List<String> viewExpenses() {
        List<String> monthlyExpenseList = new ArrayList<>();
        for (Expense e: monthlyExpenses) {
            monthlyExpenseList.add(String.valueOf(e.showExpense()));
        }
        return monthlyExpenseList;
    }

    // EFFECTS: returns true if the monthlyExpenses list is empty, false otherwise
    public boolean isEmpty() {
        return this.size == 0;
    }

    public int getSize() {
        return this.size;
    }




}
