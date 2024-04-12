package db.dao;

import db.DbConnection;
import service.dto.NotificationDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {
    private static final String SQL_CREATE_TASK = "INSERT INTO notification (\"name\", task_id, user_id, create_date) VALUES (?, ?, ?, ?)";
    private static final String SQL_GET_ALL = "SELECT notification.id as id, notification.\"name\" as name, " +
            "task.\"name\" as taskName, notification.create_date as createDate, notification.read as read, " +
            "notification.read_date as readDate, \"user\".\"name\" as userNameAssigned, task.id as taskId " +
            "FROM notification " +
            "JOIN task ON notification.task_id = task.id " +
            "JOIN \"user\" ON notification.user_id = \"user\".id " +
            "ORDER BY notification.id";
    public static final String SQL_MARK_AS_READ = "UPDATE notification SET read = true, read_date = ? WHERE id = ?";
    public static final String SQL_GET_NUMBER_OF_NOTIFICATIONS = "SELECT COUNT(*) FROM notification";

    public void create(String name, Long taskId, Long userId) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_TASK)) {
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, taskId);
            preparedStatement.setLong(3, userId);
            preparedStatement.setObject(4, LocalDateTime.now());
            boolean taskCreated = preparedStatement.executeUpdate() == 1;
            if (!taskCreated) {
                throw new SQLException();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<NotificationDto> findAll() {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<NotificationDto> notifications = new ArrayList<>();
                while (resultSet.next()) {
                    notifications.add(new NotificationDto(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("taskName"),
                            resultSet.getString("createDate"),
                            resultSet.getBoolean("read"),
                            resultSet.getString("readDate"),
                            resultSet.getString("userNameAssigned"),
                            resultSet.getLong("taskId")));
                }
                return notifications;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean remove(Long notificationId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM notification WHERE id = ?")) {
                preparedStatement.setLong(1, notificationId);
                return preparedStatement.executeUpdate() == 1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean markAsRead(Long notificationId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_MARK_AS_READ)) {
                preparedStatement.setObject(1, LocalDateTime.now());
                preparedStatement.setLong(2, notificationId);
                return preparedStatement.executeUpdate() == 1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getNumberOfNotifications() {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_NUMBER_OF_NOTIFICATIONS)) {
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
