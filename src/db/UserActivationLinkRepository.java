package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserActivationLinkRepository {

    private static final String SQL_CREATE_USER = "INSERT INTO user_activation_link (link_id, username) VALUES (?, ?)";
    private static final String SQL_GET_ACTIVATION_LINK = "SELECT username FROM user_activation_link WHERE link_id = ? AND expired = FALSE LIMIT 1;";
    private static final String SQL_DEACTIVATE_LINK = "UPDATE user_activation_link SET expired = TRUE WHERE link_id = ?";

    public void createLink(String username, UUID linkId) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_USER)) {
            preparedStatement.setObject(1, linkId);
            preparedStatement.setString(2, username);
            boolean userNotCreated = preparedStatement.executeUpdate() == 0;
            if (userNotCreated) {
                throw new SQLException();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsernameForNonExpiredLink(String id) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ACTIVATION_LINK)) {
            preparedStatement.setObject(1, UUID.fromString(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            throw new RuntimeException("Username not found for non expired link");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setLinkExpired(String id) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DEACTIVATE_LINK)) {
            preparedStatement.setObject(1, UUID.fromString(id));
            boolean success = preparedStatement.executeUpdate() == 1;
            connection.close();
            return success;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
