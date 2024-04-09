package db.dao;

import db.DbConnection;
import service.dto.ChatMessageDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageDao {
    private static final String SQL_CREATE_CHAT_MESSAGE = "INSERT INTO chat_message (sender_id, task_id, content, sequence, sent_at) VALUES (?, ?, ?, ?, NOW())";
    private static final String SQL_GET_NEXT_MESSAGE_SEQUENCE = "SELECT COUNT(*) FROM chat_message WHERE task_id = ?";
    private static final String SQL_GET_TASK_CHAT_MESSAGES = "SELECT content, \"user\".name as senderName, \"user\".id as senderId FROM chat_message " +
            "JOIN \"user\" ON chat_message.sender_id = \"user\".id " +
            "WHERE task_id = ? ORDER BY sequence";

    public void create(Long userId, Long taskId, String messageContent, Long nextMessageSequence) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_CHAT_MESSAGE)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, taskId);
            preparedStatement.setString(3, messageContent);
            preparedStatement.setLong(4, nextMessageSequence);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getNextMessageSequenceForTask(Long taskId) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_NEXT_MESSAGE_SEQUENCE)) {
            preparedStatement.setLong(1, taskId);
            if (preparedStatement.execute() && preparedStatement.getResultSet().next()) {
                connection.close();
                return preparedStatement.getResultSet().getLong(1);
            } else {
                connection.close();
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ChatMessageDto> findAllForTaskId(Long taskId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_TASK_CHAT_MESSAGES)) {
                preparedStatement.setLong(1, taskId);

                ResultSet resultSet = preparedStatement.executeQuery();
                List<ChatMessageDto> messages = new ArrayList<>();
                while (resultSet.next()) {
                    messages.add(new ChatMessageDto(resultSet.getString("senderName"),
                            resultSet.getString("senderId"),
                            resultSet.getString("content")
                    ));
                }
                return messages;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
