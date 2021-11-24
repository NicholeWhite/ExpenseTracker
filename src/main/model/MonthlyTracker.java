package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// Represents a list of expenses, with each expense having an associated value (in dollars) and description
public class MonthlyTracker implements Writable {
    private List<Expense> monthlyExpenses; // List of expenses
    private int size;                      // represents the size of list of expenses
    private String month;

    // EFFECTS: Constructor that creates an empty array list, sets the size to 0, and has a blank month;
    // clears event log
    public MonthlyTracker() {
        this.monthlyExpenses = new ArrayList<>();
        this.size = 0;
        this.month = "";
       // EventLog.getInstance().clear();
    }

    // EFFECTS: Constructor that creates an empty array list, sets the size to 0 and specifies a month;
    public MonthlyTracker(String month) {
        this.monthlyExpenses = new ArrayList<>();
        this.size = 0;
        this.month = month;
    }

    // MODIFIES: this
    // EFFECTS: Adds the expense to the back of the array list and increases size of list by 1, and logs action
    public void addExpense(Expense e) {
        this.monthlyExpenses.add(e);
        size++;
        EventLog.getInstance().logEvent(new Event("Expense Added: " + e.getAmount() + ", " + e.getType()));

    }

    //EFFECTS: Logs expenses in monthlyExpenses
    public void logExpenses() {
        for (Expense e: monthlyExpenses) {
            EventLog.getInstance().logEvent(new Event("Last Session Expenses: " + e.getAmount() + ", " + e.getType()));
        }
    }

    // MODIFIES: this
    // EFFECTS: removes the expense from the list and decreases size by 1 and logs action, does
    //          nothing if exact expense is not found in list
    public void removeExpense(Expense e) {
        for (Expense ex: monthlyExpenses) {
            if (ex.getAmount() == e.getAmount() && ex.getType().equals(e.getType())) {
                this.monthlyExpenses.remove(ex);
                size--;
                EventLog.getInstance().logEvent(new Event("Expense Removed: " + e.getAmount() + ", " + e.getType()));
                break;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Clears all expenses and logs the action
    public void clearAll() {
        this.monthlyExpenses = new ArrayList<>();
        this.size = 0;
        this.month = month;
        EventLog.getInstance().logEvent(new Event("Cleared All Expenses."));

    }


    // EFFECTS: returns the sum of the expenses in monthlyExpenses and logs it, 0 if
    //          the list is empty
    public float sumExpenses() {
        float expenseSum = 0;
        for (Expense e: monthlyExpenses) {
            expenseSum += e.getAmount();
        }
        EventLog.getInstance().logEvent(new Event("Summed Expenses: " + expenseSum));

        return expenseSum;
    }

    // REQUIRES: monthlyExpenses has > 0 expenses
    // EFFECTS: adds each expense in monthlyExpenses to monthlyExpenseList
    //          returns the list of expenses as a list of strings and logs it
    public List<String> viewExpenses() {
        List<String> monthlyExpenseList = new ArrayList<>();
        for (Expense e: monthlyExpenses) {
            monthlyExpenseList.add(String.valueOf(e.showExpense()));
        }

        EventLog.getInstance().logEvent(new Event("Listed Expenses: " + monthlyExpenseList));
        return monthlyExpenseList;
    }

    //EFFECTS: Creates an event log entry when a previous file is loaded
    public void load() {
        EventLog.getInstance().logEvent(new Event("Loaded Expenses."));
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

    public String getMonth() {
        return this.month;
    }

    //MODIFIES: this
    //EFFECTS: sets this month and logs it
    public void setMonth(String month) {
        this.month = month;
        EventLog.getInstance().logEvent(new Event("Month Set: " + month));
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
