package db;

import service.TaskDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final String SQL_CREATE_TASK = "INSERT INTO task (\"name\", deadline, assignee_id, priority_id, creator_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_GET_ALL_TASKS = "SELECT task.id, task.name, task.deadline, task.completed, task.complete_date," +
            " \"user\".name as assigneeName, priority.value as priorityValue " +
            "FROM task " +
            "JOIN priority ON task.priority_id = priority.id " +
            "JOIN \"user\" ON task.assignee_id = \"user\".id ORDER BY task.id";

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

    public List<TaskDto> findAll() {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_TASKS)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<TaskDto> tasks = new ArrayList<>();
                while (resultSet.next()) {
                    tasks.add(new TaskDto(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getObject("deadline", LocalDateTime.class),
                            resultSet.getString("assigneeName"),
                            resultSet.getString("priorityValue"),
                            resultSet.getBoolean("completed"),
                            resultSet.getObject("complete_date", LocalDateTime.class)
                    ));
                }
                return tasks;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
