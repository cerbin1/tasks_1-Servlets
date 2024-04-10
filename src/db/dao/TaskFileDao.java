package db.dao;

import db.DbConnection;
import service.dto.TaskFileDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskFileDao {
    private static final String SQL_SAVE_FILE_INFO = "INSERT INTO task_file (name, type, task_id) VALUES (?, ?, ?)";
    private static final String SQL_GET_TASK_FILES = "SELECT name, type, task_id FROM task_file WHERE task_id = ?";
    private static final String SQL_GET_TASK_FILE_BY_FILENAME = "SELECT name FROM task_file WHERE name = ?";

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

    public List<TaskFileDto> findAllForTaskId(Long taskId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_TASK_FILES)) {
                preparedStatement.setLong(1, taskId);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<TaskFileDto> files = new ArrayList<>();
                while (resultSet.next()) {
                    files.add(new TaskFileDto(
                            resultSet.getString("name"),
                            resultSet.getString("type"),
                            resultSet.getLong("task_id")
                    ));
                }
                return files;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByName(String fileName) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_TASK_FILE_BY_FILENAME)) {
                preparedStatement.setString(1, fileName);
                ResultSet resultSet = preparedStatement.executeQuery();
                return onlyOneRowIn(resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean onlyOneRowIn(ResultSet resultSet) throws SQLException {
        return resultSet.next() && !resultSet.next();

    }
}
