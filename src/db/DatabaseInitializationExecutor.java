package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializationExecutor {

    public void run() {
        Connection connection = (new DbConnection()).createConnection();
        String scriptsFilePath = System.getenv("SERVLET_FILES_PATH") + "db/sql/scripts.sql";
        try (Statement statement = connection.createStatement()) {
            statement.execute(readScriptFile(scriptsFilePath));
            System.out.println("SQL script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing SQL script: " + e.getMessage());
        }
    }

    private String readScriptFile(String filePath) {
        StringBuilder scriptContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scriptContent.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading SQL script file: " + e.getMessage());
        }
        return scriptContent.toString();
    }
}
