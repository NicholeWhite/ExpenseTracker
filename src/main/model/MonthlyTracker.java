package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a list of expenses, with each expense having an associated value (in dollars) and description
public class MonthlyTracker implements Writable {
    private List<Expense> monthlyExpenses; // List of expenses
    private int size;                      // represents the size of list of expenses
    private String month;

    // EFFECTS: Constructor that creates an empty array list and sets the size to 0
    public MonthlyTracker() {
        this.monthlyExpenses = new ArrayList<>();
        this.size = 0;
        this.month = "";
    }

    public MonthlyTracker(String month) {
        this.monthlyExpenses = new ArrayList<>();
        this.size = 0;
        this.month = month;
    }

    // MODIFIES: this
    // EFFECTS: Adds the expense to the back of the array list and increases size of list by 1
    public void addExpense(Expense e) {
        this.monthlyExpenses.add(e);
        size++;
    }

    // MODIFIES: this
    // EFFECTS: removes the expense from the list and decreases size by 1, does
    //          nothing if exact expense is not found in list
    public void removeExpense(Expense e) {
        for (Expense ex: monthlyExpenses) {
            if (ex.getAmount() == e.getAmount() && ex.getDescription().equals(e.getDescription())) {
                this.monthlyExpenses.remove(ex);
                size--;
                break;
            }
        }
    }


    // EFFECTS: returns the sum of the expenses in monthlyExpenses, 0 if
    //          the list is empty
    public float sumExpenses() {
        float expenseSum = 0;
        for (Expense e: monthlyExpenses) {
            expenseSum += e.getAmount();
        }
        return expenseSum;
    }

    // REQUIRES: monthlyExpenses has > 0 expenses
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

    public String getName() {
        return this.month;
    }

    public List<Expense> getMonthlyExpenses() {
        return monthlyExpenses;
    }



    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Month", month);
        json.put("Expense", expensesToJson());
        return json;
    }

    // EFFECTS: returns expenses in this monthlyExpenses as a JSON array
    private JSONArray expensesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Expense e : monthlyExpenses) {
            jsonArray.put(e.toJson());
        }
        return jsonArray;
    }

}
