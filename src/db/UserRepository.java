package db;

import service.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

public class UserRepository {
    final String SQL_CREATE_USER = "INSERT INTO \"user\" (email, username, password, name, surname) VALUES (?, ?, ?, ?, ?)";
    final String SQL_SELECT_USERS = "SELECT id FROM \"user\" WHERE username = ? AND password = ?";
    final String SQL_SELECT_USER_LOGINS = "SELECT * FROM user_login WHERE username = ? AND session_id = ? AND active = TRUE";
    final String SQL_CREATE_LOGIN = "INSERT INTO user_login (username, session_id) VALUES (?, ?)";
    final String SQL_DEACTIVATE_LOGIN = "UPDATE user_login SET active = FALSE WHERE username = ?";
    final String SQL_ACTIVATE_USER = "UPDATE \"user\" SET active = TRUE WHERE username = ?";
    final String SQL_IS_USER_ACTIVE = "SELECT * FROM \"user\" WHERE username = ? AND active = TRUE";
    final String SQL_GET_BY_EMAIL = "SELECT * FROM \"user\" WHERE email = ?";
    final String SQL_GET_BY_USERNAME = "SELECT * FROM \"user\" WHERE username = ?";
    final String SQL_GET_ALL_USERS = "SELECT id, name, username FROM \"user\"";


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

    public String getUserIdByUsernameAndPassword(String username, String hashedPassword) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USERS)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return resultSet.getString("id");
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

    public void deactivateUserLogin(String username) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DEACTIVATE_LOGIN)) {
                preparedStatement.setString(1, username);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setUserActive(String username) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ACTIVATE_USER)) {
                preparedStatement.setString(1, username);
                return preparedStatement.executeUpdate() == 1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getActiveUserWith(String username) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_IS_USER_ACTIVE)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                return onlyOneRowIn(resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByEmail(String email) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BY_EMAIL)) {
                preparedStatement.setString(1, email);
                return preparedStatement.executeQuery().next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByUsername(String username) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BY_USERNAME)) {
                preparedStatement.setString(1, username);
                return preparedStatement.executeQuery().next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserDto> findAll() {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_USERS)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<UserDto> allUsers = new ArrayList<>();
                while (resultSet.next()) {
                    allUsers.add(new UserDto(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3)));
                }
                return allUsers;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
