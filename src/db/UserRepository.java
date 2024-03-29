package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

public class UserRepository {
    final String SQL_CREATE_USER = "INSERT INTO \"user\" (email, username, password, name, surname) VALUES (?, ?, ?, ?, ?)";
    final String SQL_SELECT_USERS = "SELECT * FROM \"user\" WHERE username = ? AND password = ?";
    final String SQL_SELECT_USER_LOGINS = "SELECT * FROM user_login WHERE username = ? AND session_id = ? AND active = TRUE";
    final String SQL_CREATE_LOGIN = "INSERT INTO user_login (username, session_id) VALUES (?, ?)";


    public void createUser(String email, String username, String hashedPassword, String name, String surname) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_USER)) {
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

    public boolean userExists(String username, String hashedPassword) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USERS)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
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

    public boolean userLoginExists(String username, String sessionId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_LOGINS, TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, sessionId);
                ResultSet resultSet = preparedStatement.executeQuery();
                return onlyOneRowIn(resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createLogin(String username, String sessionId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_LOGIN)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, sessionId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
