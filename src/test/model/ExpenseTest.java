package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseTest {
    private Expense testExpense;
    private List<String> testList;

    @BeforeEach
    void runBefore() {
        testExpense = new Expense(100.25F, "hydro");
    }

    @Test
    void testGetAmount() {
        assertEquals(100.25F, testExpense.getAmount());
    }

    @Test
    void testGetDescription() {
        assertEquals("hydro", testExpense.getDescription());

    }

    @Test
    void testShowExpense() {
        testList = new ArrayList<>();
        testList.add("100.25");
        testList.add("hydro");
        assertEquals(testList, testExpense.showExpense());

    }

}
