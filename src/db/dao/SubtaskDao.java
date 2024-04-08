package db.dao;

import db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubtaskDao {
    private static final String SQL_CREATE_SUBTASK = "INSERT INTO subtask (task_id, name, sequence) VALUES (?, ?, ?)";

    public void createSubtasks(Long taskId, String[] subtaskNames) {
        for (int i = 0; i < subtaskNames.length; i++) {
            DbConnection dbConnection = new DbConnection();
            Connection connection = dbConnection.createConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_SUBTASK)) {
                preparedStatement.setLong(1, taskId);
                preparedStatement.setString(2, subtaskNames[i]);
                preparedStatement.setLong(3, i);
                preparedStatement.executeUpdate();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
