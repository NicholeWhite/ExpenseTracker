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

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MonthlyTracker read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkRoom(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private MonthlyTracker parseWorkRoom(JSONObject jsonObject) {
        System.out.print(jsonObject);
        String name = jsonObject.getString("Month");
        MonthlyTracker t = new MonthlyTracker(name);
        addItems(t, jsonObject);
        return t;
    }

    // MODIFIES: wr
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addItems(MonthlyTracker t, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Expense");
        for (Object json : jsonArray) {
            JSONObject nextExpense = (JSONObject) json;
            addItem(t, nextExpense);
        }
    }

    // MODIFIES: t
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addItem(MonthlyTracker t, JSONObject jsonObject) {
        String description = jsonObject.getString("description");
        System.out.printf("descroption", description);
        String amount = String.valueOf(jsonObject.getString("Expense"));
        Expense expense = new Expense(Float.valueOf(amount), description);
        t.addExpense(expense);
    }
}
