package db.dao;

import db.DbConnection;
import service.SubtaskDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubtaskDao {
    private static final String SQL_CREATE_SUBTASK = "INSERT INTO subtask (task_id, name, sequence) VALUES (?, ?, ?)";
    private static final String SQL_GET_ALL_SUBTASKS_BY_TASK_ID = "SELECT name, sequence FROM subtask WHERE task_id = ? ORDER BY sequence";

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

    public List<SubtaskDto> findAllByTaskId(Long taskId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_SUBTASKS_BY_TASK_ID)) {
                preparedStatement.setLong(1, taskId);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<SubtaskDto> subtasks = new ArrayList<>();
                while (resultSet.next()) {
                    subtasks.add(new SubtaskDto(resultSet.getString("name"), resultSet.getLong("sequence")));
                }
                return subtasks;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
