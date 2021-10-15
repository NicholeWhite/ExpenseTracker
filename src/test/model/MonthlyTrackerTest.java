package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MonthlyTrackerTest {
    private MonthlyTracker testExpenseList;
    private Expense testExpense;
    private Expense testExpense2;
    private Expense testExpense3;
    private List<String> testList;


    @BeforeEach
    void runBefore() {
        testExpenseList = new MonthlyTracker();
    }

    @Test
    void testAddExpense() {
        assertTrue(testExpenseList.isEmpty());
        for(int i = 0; i < 5 ; i++) {
            testExpense = new Expense(i, "test");
            testExpenseList.addExpense(testExpense);
        }
        assertFalse(testExpenseList.isEmpty());
        assertEquals(5,testExpenseList.getSize());
    }

    @Test
    void testRemoveExpense() {
        assertTrue(testExpenseList.isEmpty());
        for(int i = 0; i < 5 ; i++) {
            testExpense = new Expense(i, "test");
            testExpenseList.addExpense(testExpense);
        }

        assertEquals(5,testExpenseList.getSize());

        for(int i = 0; i < 5 ; i++) {
            testExpense = new Expense(i, "test");
            testExpenseList.removeExpense(testExpense);
        }
        assertTrue(testExpenseList.isEmpty());

        testExpense = new Expense(100F, "inList");
        testExpense2 = new Expense(500F, "notInList");
        testExpense3 = new Expense(100F, "inList");
        testExpenseList.addExpense(testExpense);
        assertEquals(1,testExpenseList.getSize());
        testExpenseList.removeExpense(testExpense2);
        assertEquals(1, testExpenseList.getSize());
        testExpenseList.removeExpense(testExpense3);
        assertEquals(0, testExpenseList.getSize());

    }

    @Test
    void testSumExpenses() {
        assertTrue(testExpenseList.isEmpty());
        for(int i = 0; i < 4 ; i++) {
            testExpense = new Expense(i, "test");
            testExpenseList.addExpense(testExpense);
        }
        assertEquals(6F,testExpenseList.sumExpenses());

        testExpenseList.removeExpense(testExpense);
        assertEquals(3F, testExpenseList.sumExpenses());

    }

    @Test
    void testViewExpenses() {
        assertTrue(testExpenseList.isEmpty());
        testList = new ArrayList<>();
        for(int i = 0; i < 3 ; i++) {

            testExpense = new Expense(i, "test");
            testList.add(String.valueOf(testExpense.showExpense()));
            testExpenseList.addExpense(testExpense);
        }
       assertEquals(testList, testExpenseList.viewExpenses());
    }

}