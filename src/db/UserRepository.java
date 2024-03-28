package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRepository {
    final String SQL_INSERT = "INSERT INTO users (email, username, password, name, surname) VALUES (?, ?, ?, ?, ?)";

    public void createUser(String email, String username, String hashedPassword, String name, String surname) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, surname);
            boolean userNotCreated = preparedStatement.executeUpdate() == 0;
            if (userNotCreated) {
                throw new SQLException();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
