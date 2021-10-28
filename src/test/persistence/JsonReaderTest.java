package persistence;

import model.Expense;
import model.MonthlyTracker;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MonthlyTracker t = reader.read();
            fail("IOException expected");
        } catch (IOException e){
            //pass;
        }
    }

    @Test
    void testReaderEmptyMonthlyTracker() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyMonthlyTracker.json");

        try {
            MonthlyTracker t = reader.read();
            assertEquals("Monthly Expenses", t.getName());
            assertEquals(0, t.getSize());
        } catch (IOException e) {
            fail("Could not read from file");
        }

    }

    @Test
    void testReaderGeneralMonthlyTracker(){
        JsonReader reader = new JsonReader("./data/testReaderGeneralMonthlyTracker.json");
        try {
            MonthlyTracker t = reader.read();
            assertEquals("Monthly Expenses", t.getName());
            List<Expense> expenses = t.getMonthlyExpenses();
            assertEquals(2, expenses.size());
            checkExpense("hydro", 100F, expenses.get(0));
            checkExpense("rent", 1000F, expenses.get(1));
        } catch (IOException e) {
            fail("Could not read from file");
        }

    }
}
