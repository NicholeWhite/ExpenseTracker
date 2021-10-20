package persistence;


import model.Expense;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {
    protected void checkExpense(String description, Float amount, Expense expense){
        assertEquals(description, expense.getDescription());
        assertEquals(amount, expense.getAmount());
    }

}
