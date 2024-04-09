package db.dao;

import db.DbConnection;
import service.dto.TaskReminderDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskReminderDao {
    private static final String SQL_CREATE_TASK_REMINDER = "INSERT INTO task_reminder (task_id, planned_send_date) VALUES (?, ?)";
    private static final String SQL_GET_REMINDERS_TO_SEND = "SELECT task_reminder.id, task_id, task.\"name\" as taskName, \"user\".email as assigneeEmail " +
            "FROM task_reminder " +
            "JOIN task ON task_reminder.task_id = task.id " +
            "JOIN \"user\" ON task.assignee_id = \"user\".id " +
            "WHERE planned_send_date <= NOW() AND sent = FALSE";
    private static final String SQL_SET_REMINDER_AS_SENT = "UPDATE task_reminder SET sent = TRUE, sent_date = NOW() WHERE id = ?";

    public void create(Long taskId, LocalDateTime plannedSendDate) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_TASK_REMINDER)) {
            preparedStatement.setLong(1, taskId);
            preparedStatement.setObject(2, plannedSendDate);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TaskReminderDto> getNotSentTaskReminders() {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_REMINDERS_TO_SEND)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<TaskReminderDto> reminders = new ArrayList<>();
                while (resultSet.next()) {
                    reminders.add(new TaskReminderDto(
                            resultSet.getLong("id"),
                            resultSet.getLong("task_id"),
                            resultSet.getString("taskName"),
                            resultSet.getString("assigneeEmail")));
                }
                return reminders;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setReminderAsSent(Long reminderId) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SET_REMINDER_AS_SENT)) {
            preparedStatement.setLong(1, reminderId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
