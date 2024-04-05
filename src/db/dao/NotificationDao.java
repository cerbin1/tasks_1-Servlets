package db.dao;

import db.DbConnection;
import service.NotificationDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NotificationDao {
    private static final String SQL_CREATE_TASK = "INSERT INTO notification (\"name\", task_id, user_id, create_date) VALUES (?, ?, ?, ?)";

    public void create(NotificationDto notificationDto) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_TASK)) {
            preparedStatement.setString(1, notificationDto.getName());
            preparedStatement.setLong(2, notificationDto.getTaskId());
            preparedStatement.setLong(3, notificationDto.getUserId());
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
}
