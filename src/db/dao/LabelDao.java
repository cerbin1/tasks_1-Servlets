package db.dao;

import db.DbConnection;
import service.dto.LabelDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LabelDao {
    private static final String SQL_CREATE_LABELS = "INSERT INTO task_label (task_id, name) VALUES (?, ?)";
    private static final String SQL_GET_TASK_LABELS = "SELECT id, \"name\" FROM task_label WHERE task_id = ?";
    private static final String SQL_DELETE_LABEL = "DELETE FROM task_label WHERE id = ?";
    private static final String SQL_UPDATE_LABEL = "UPDATE task_label SET name = ? WHERE id = ?";

    public void createLabels(Long taskId, String[] labels) {
        for (String label : labels) {
            DbConnection dbConnection = new DbConnection();
            Connection connection = dbConnection.createConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_LABELS)) {
                preparedStatement.setLong(1, taskId);
                preparedStatement.setString(2, label);
                preparedStatement.executeUpdate();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<LabelDto> findLabelsByTaskId(Long taskId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_TASK_LABELS)) {
                preparedStatement.setLong(1, taskId);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<LabelDto> labels = new ArrayList<>();
                while (resultSet.next()) {
                    labels.add(new LabelDto(resultSet.getLong("id"), resultSet.getString("name")));
                }
                return labels;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(Long labelId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_LABEL)) {
                preparedStatement.setLong(1, labelId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLabels(String[] labels, String[] labelIds) {
        for (int i = 0; i < labelIds.length; i++) {
            DbConnection dbConnection = new DbConnection();
            Connection connection = dbConnection.createConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_LABEL)) {
                preparedStatement.setString(1, labels[i]);
                preparedStatement.setLong(2, Long.parseLong(labelIds[i]));
                preparedStatement.executeUpdate();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
