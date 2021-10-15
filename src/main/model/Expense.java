package model;


import java.util.ArrayList;
import java.util.List;

// Represents a single expense having a value and a description for the expense
public class Expense {
    private float expense;
    private String entryDescription;


    // REQUIRES: amount > 0; description has a non-zero length
    // EFFECTS: creates an expense with an amount and the description of the amount
    public Expense(float amount, String description) {
        this.expense = amount;
        this.entryDescription = description;
    }

    // EFFECTS: returns the string representation of the expense
    public List<String> showExpense() {
        List<String> expenseList = new ArrayList<>();
        expenseList.add(String.valueOf(this.expense));
        expenseList.add(this.entryDescription);
        return expenseList;
    }

    public float getAmount() {
        return this.expense;
    }

    public String getDescription() {
        return this.entryDescription;
    }


}

