package db.dao;

import db.DbConnection;
import service.dto.SubtaskDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubtaskDao {
    private static final String SQL_CREATE_SUBTASK = "INSERT INTO subtask (task_id, name, sequence) VALUES (?, ?, ?)";
    private static final String SQL_GET_ALL_SUBTASKS_BY_TASK_ID = "SELECT id, name, sequence FROM subtask WHERE task_id = ? ORDER BY sequence";
    private static final String SQL_DELETE_SUBTASK = "DELETE FROM subtask WHERE id = ?";
    private static final String SQL_UPDATE_SUBTASK = "UPDATE subtask SET name = ? WHERE id = ?";
    private static final String SQL_GET_NUMBER_OF_SUBTASKS = "SELECT COUNT(*) FROM subtask";

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
                    subtasks.add(new SubtaskDto(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getLong("sequence")));
                }
                return subtasks;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(Long subtaskId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_SUBTASK)) {
                preparedStatement.setLong(1, subtaskId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateSubtasks(String[] subtasksNames, String[] subtasksIds) {
        for (int i = 0; i < subtasksIds.length; i++) {
            DbConnection dbConnection = new DbConnection();
            Connection connection = dbConnection.createConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_SUBTASK)) {
                preparedStatement.setString(1, subtasksNames[i]);
                preparedStatement.setLong(2, Long.parseLong(subtasksIds[i]));
                preparedStatement.executeUpdate();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Long getNumberOfSubtasks() {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_NUMBER_OF_SUBTASKS)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return resultSet.getLong(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
