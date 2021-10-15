package ui;

import model.Expense;
import model.MonthlyTracker;

import java.util.Scanner;


// Note: most code for this part of the application is heavily based on the TellerApp
public class MonthlyExpenseApp {
    private MonthlyTracker expenseList;
    private Expense expense;
    private Scanner input;


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
        if (command.equals("a")) {
            doAddExpense();
        } else if (command.equals("r")) {
            doRemoveExpense();
        } else if (command.equals("t")) {
            doSumExpenses();
        } else if (command.equals("l")) {
            doViewExpenses();
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
    }


    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nPlease choose option:");
        System.out.println("\ta -> add expense");
        System.out.println("\tr -> remove expense");
        System.out.println("\tt -> view total expenses");
        System.out.println("\tl -> list all expenses");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: performs the addition of an expense to the expense list
    private void doAddExpense() {

        System.out.print("Enter the expense: $");
        float amount = input.nextFloat();
        System.out.println(amount);

        System.out.print("Enter the decription of expense: $");
        String detail = input.next();

        if (amount >= 0.0) {
            expense = new Expense(amount, detail);
            expenseList.addExpense(expense);

        } else {
            System.out.println("Cannot deposit negative amount...\n");
        }

        expenseTotal(expenseList);
    }

    // MODIFIES: this
    // EFFECTS: performs the removal of an expense from the expense list
    private void doRemoveExpense() {
        //Account selected = selectAccount();
        System.out.print("Enter the amount of the expense to remove: $");
        float amount = input.nextFloat();

        System.out.print("Enter its description: ");
        String detail = input.next();

        if (amount < 0.0) {
            System.out.println("Cannot remove a negative amount...\n");
        } else if (expenseList.sumExpenses() < amount) {
            System.out.println("Expense is not present...\n");
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

}
