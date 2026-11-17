package ocean.service;

import ocean.database.DatabaseConnection;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Builds a collection-based report and safely exports it as CSV. */
public class ReportService {
    public Map<String, Integer> categoryReport() throws Exception {
        Map<String, Integer> categories = new TreeMap<>();
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT category, COUNT(*) AS total FROM messages GROUP BY category")) {
            while (r.next()) categories.put(r.getString("category"), r.getInt("total"));
        }
        // Stream pipeline demonstrates sorting, filtering, mapping, and collection.
        return categories.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (first, second) -> first, TreeMap::new));
    }

    public void exportCategories(Path file) throws Exception {
        Map<String, Integer> report = categoryReport();
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write("Category,Message Count");
            writer.newLine();
            report.forEach((category, count) -> {
                try {
                    writer.write(csv(category) + "," + count);
                    writer.newLine();
                } catch (IOException e) {
                    throw new java.io.UncheckedIOException(e);
                }
            });
        } catch (java.io.UncheckedIOException e) {
            throw e.getCause();
        }
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
