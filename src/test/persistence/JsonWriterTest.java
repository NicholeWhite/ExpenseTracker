package persistence;

import model.Expense;
import model.MonthlyTracker;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            MonthlyTracker t = new MonthlyTracker("January");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            //pass
        }

    }

    @Test
    void testWriterEmptyMonthlyTracker() {
        try {
            MonthlyTracker t = new MonthlyTracker("January");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMonthlyExpenses.json");
            writer.open();
            writer.write(t);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMonthlyExpenses.json");
            t = reader.read();
            assertEquals("January", t.getName());
            assertEquals(0, t.numExpenses());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralMonthlyTracker() {
        try {
            MonthlyTracker t = new MonthlyTracker("January");
            t.addExpense(new Expense(500F, "rent"));
            t.addExpense(new Expense(100F, "hydro"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMonthlyTracker.json");
            writer.open();
            writer.write(t);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMonthlyTracker.json");
            t = reader.read();
            assertEquals("January", t.getName());
            List<Expense> expenses = t.getExpenses();
            assertEquals(2, expenses.size());
            checkExpense("rent", 500F, expenses.get(0));
            checkExpense("hydro", 100F, expenses.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }


}
