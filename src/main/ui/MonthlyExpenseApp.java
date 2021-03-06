package ui;

import model.Expense;
import model.MonthlyTracker;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// This class references and uses code from the TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp

// Provides a console based interface for the user to interact with
public class MonthlyExpenseApp {
    private static final String JSON_STORE = "./data/expenseListData.json";
    private MonthlyTracker expenseList;
    private Expense expense;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the monthly expense application
    public MonthlyExpenseApp() {
        runExpenseTracker();

    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runExpenseTracker() {
        boolean keepGoing = true;
        String command = null;


        init();
        expenseList.setMonth(command);

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("m")) {
            doSetMonth();
        } else if (command.equals("a")) {
            doAddExpense();
        } else if (command.equals("r")) {
            doRemoveExpense();
        } else if (command.equals("t")) {
            doSumExpenses();
        } else if (command.equals("l")) {
            doViewExpenses();
        } else if (command.equals("s")) {
            saveExpenseList();
        } else if (command.equals("o")) {
            openExpenseList();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes expenses and a monthly tracker
    private void init() {
        expenseList = new MonthlyTracker();
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

    }

    // EFFECTS: displays menu of options to the user
    private void displayMenu() {
        System.out.println("\nPlease choose option:");
        System.out.println("\tm -> set month");
        System.out.println("\ta -> add expense");
        System.out.println("\tr -> remove expense");
        System.out.println("\tt -> view total expenses");
        System.out.println("\tl -> list all expenses");
        System.out.println("\ts -> save expense tracker to file");
        System.out.println("\to -> open expense tracker from file");
        System.out.println("\tq -> quit");
    }



    // MODIFIES: this
    // EFFECTS: performs the addition of an expense to the expense list
    private void doAddExpense() {
        System.out.print("Enter the expense: $");
        float amount = input.nextFloat();
        System.out.println(amount);

        System.out.print("Enter the description of expense: ");
        String detail = input.next();

        if (amount > 0.0 && detail.length() > 0) {
            expense = new Expense(amount, detail);
            expenseList.addExpense(expense);
        } else if (amount <= 0) {
            System.out.println("Cannot enter $0.00 or a negative amount... \n");
        } else {
            System.out.println("Please enter a valid description... \n");
        }
        expenseTotal(expenseList);
    }

    // MODIFIES: this
    // EFFECTS: performs the removal of an expense from the expense list
    private void doRemoveExpense() {
        System.out.print("Enter the amount of the expense to remove: $");
        float amount = input.nextFloat();

        System.out.print("Enter the description of the expense: ");
        String detail = input.next();

        if (amount <= 0.0) {
            System.out.println("Cannot remove $0.00 or a negative amount...\n");
        } else if (detail.length() == 0) {
            System.out.println("Description is not valid...\n");
        } else if (expenseList.sumExpenses() < amount) {
            System.out.println("Amount is greater than the sum of expenses...\n");
        } else {
            expense = new Expense(amount, detail);
            expenseList.removeExpense(expense);
        }
        expenseTotal(expenseList);
    }

    // EFFECTS: calls expenseTotal() to provide a print-out of the sum of
    //          expenses in the expenseList
    private void doSumExpenses() {
        expenseTotal(expenseList);
    }

    // EFFECTS: prints the list of expenses
    private void doViewExpenses() {
        if (expenseList.getSize() == 0) {
            System.out.println("No expenses to show..");
        } else {
            System.out.println(expenseList.viewExpenses());
        }
    }

    // EFFECTS: prints the sum of expenses to the screen
    private void expenseTotal(MonthlyTracker expenses) {
        System.out.print("Total Expenses: $");
        System.out.print(expenses.sumExpenses());
    }


    // EFFECTS: saves the expenseList to file
    private void saveExpenseList() {
        try {
            if (expenseList.getMonth() != null) {
                jsonWriter.open();
                jsonWriter.write(expenseList);
                jsonWriter.close();
                System.out.println("Saved " + expenseList.getName() + " to " + JSON_STORE);
            } else {
                System.out.println("Please specify a month before saving");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }



    // MODIFIES: this
    // EFFECTS: loads expenseList from file
    private void openExpenseList() {
        try {
            expenseList = jsonReader.read();
            System.out.println("Loaded " + expenseList.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: specifies a month for expenseList
    private void doSetMonth() {
        System.out.print("Enter the month: ");
        String month = input.next();
        expenseList.setMonth(month);
    }

}
