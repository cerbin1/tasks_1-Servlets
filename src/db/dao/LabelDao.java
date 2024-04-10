package db.dao;

import db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LabelDao {
    private static final String SQL_CREATE_LABELS = "INSERT INTO task_label (task_id, name) VALUES (?, ?)";

    public void createLabels(Long taskId, String[] labels) {
        for (String label : labels) {
            DbConnection dbConnection = new DbConnection();
            Connection connection = dbConnection.createConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_LABELS)) {
                preparedStatement.setLong(1, taskId);
                preparedStatement.setString(2, label);
                preparedStatement.executeUpdate();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
