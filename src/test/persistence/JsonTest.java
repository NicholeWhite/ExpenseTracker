package persistence;


import model.Expense;

import static org.junit.jupiter.api.Assertions.*;

// This class references and uses code from the JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonTest {
    protected void checkExpense(String description, Float amount, Expense expense){
        assertEquals(description, expense.getType());
        assertEquals(amount, expense.getAmount());
    }

}
