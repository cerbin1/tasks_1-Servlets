package db.dao;

import db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskFileDao {
    private static final String SQL_SAVE_FILE_INFO = "INSERT INTO task_file (name, type, task_id) VALUES (?, ?, ?)";

    public void create(String fileName, String contentType, Long taskId) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_FILE_INFO)) {
            preparedStatement.setString(1, fileName);
            preparedStatement.setString(2, contentType);
            preparedStatement.setLong(3, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
