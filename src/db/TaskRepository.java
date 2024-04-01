package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TaskRepository {
    final String SQL_CREATE_TASK = "INSERT INTO task (\"name\", deadline, assignee_id, priority_id, creator_id) VALUES (?, ?, ?, ?, ?)";

    public boolean createTask(String name, LocalDateTime deadline, Long userId, Long priorityId, Long creatorId) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_TASK)) {
            preparedStatement.setString(1, name);
            preparedStatement.setObject(2, deadline);
            preparedStatement.setLong(3, userId);
            preparedStatement.setLong(4, priorityId);
            preparedStatement.setLong(5, creatorId);
            boolean taskCreated = preparedStatement.executeUpdate() == 1;
            if (!taskCreated) {
                throw new SQLException();
            }
            connection.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
