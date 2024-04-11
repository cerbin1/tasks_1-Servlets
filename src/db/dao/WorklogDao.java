package db.dao;

import db.DbConnection;
import service.dto.WorklogDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorklogDao {
    private static final String SQL_GET_TASK_WORKLOGS = "SELECT id, date, minutes, comment FROM worklog WHERE task_id = ?";
    private static final String SQL_CREATE_WORKLOG = "INSERT INTO worklog (date, minutes, comment, modification_date, creator_id, task_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_WORKLOG = "DELETE FROM worklog WHERE id = ?";
    public static final String SQL_UPDATE_WORKLOG = "UPDATE worklog SET date = ?, minutes = ?, comment = ?, modification_date = ? WHERE id = ?";

    public void createWorklog(LocalDate date, Long minutes, String comment, Long creatorId, Long taskId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_WORKLOG)) {
                preparedStatement.setObject(1, date);
                preparedStatement.setLong(2, minutes);
                preparedStatement.setString(3, comment);
                preparedStatement.setObject(4, LocalDateTime.now());
                preparedStatement.setLong(5, creatorId);
                preparedStatement.setLong(6, taskId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<WorklogDto> findAllByTaskId(Long taskId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_TASK_WORKLOGS)) {
                preparedStatement.setLong(1, taskId);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<WorklogDto> worklogs = new ArrayList<>();
                while (resultSet.next()) {
                    worklogs.add(new WorklogDto(resultSet.getLong("id"),
                            resultSet.getObject("date", LocalDate.class),
                            resultSet.getLong("minutes"),
                            resultSet.getString("comment")));
                }
                return worklogs;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeWorklog(Long worklogId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_WORKLOG)) {
                preparedStatement.setLong(1, worklogId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateById(Long worklogId, LocalDate date, Long minutes, String comment) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_WORKLOG)) {
                preparedStatement.setObject(1, date);
                preparedStatement.setLong(2, minutes);
                preparedStatement.setString(3, comment);
                preparedStatement.setObject(4, LocalDateTime.now());
                preparedStatement.setLong(5, worklogId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
