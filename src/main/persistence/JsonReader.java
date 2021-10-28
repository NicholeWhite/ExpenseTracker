package persistence;

import model.Expense;
import model.MonthlyTracker;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads monthlyExpenses from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MonthlyTracker read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMonthlyTracker(jsonObject);
    }


    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses monthlyExpenses from JSON object and returns it
    private MonthlyTracker parseMonthlyTracker(JSONObject jsonObject) {
        String name = jsonObject.getString("Month");
        MonthlyTracker t = new MonthlyTracker(name);
        addItems(t, jsonObject);
        return t;
    }

    // MODIFIES: t
    // EFFECTS: parses Expenses from JSON object and adds them to monthlyExpenses
    private void addItems(MonthlyTracker t, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Expense");
        for (Object json : jsonArray) {
            JSONObject nextExpense = (JSONObject) json;
            addItem(t, nextExpense);
        }
    }

    // MODIFIES: t
    // EFFECTS: parses expense from JSON object and adds it to monthlyExpenses
    private void addItem(MonthlyTracker t, JSONObject jsonObject) {
        String description = jsonObject.getString("description");
        Float amount = Float.valueOf(jsonObject.getFloat("expense"));
        Expense expense = new Expense(Float.valueOf(amount), description);
        t.addExpense(expense);
    }
}
