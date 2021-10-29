package model;


import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a single expense having a value (in dollars) and a description for the expense
public class Expense implements Writable {
    private float expense;              // value of the expense
    private String entryDescription;    // description of expense


    // REQUIRES: amount > 0 with max 2 decimal places; description has a non-zero length
    // EFFECTS: Constructor that creates an expense with an amount and the description of the amount added
    public Expense(float amount, String description) {
        this.expense = amount;
        this.entryDescription = description;
    }

    // EFFECTS: returns the string representation of the expense amount and its description
    public List<String> showExpense() {
        List<String> expenseList = new ArrayList<>();
        expenseList.add(String.valueOf(this.expense));
        expenseList.add(this.entryDescription);
        return expenseList;
    }

    //Getters
    public float getAmount() {
        return this.expense;
    }
    public String getDescription() {
        return this.entryDescription;
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("expense", expense);
        json.put("description", entryDescription);
        return json;
    }

}

