package db;

import service.PriorityDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PriorityRepository {
    final String SQL_GET_ALL_PRIORITIES = "SELECT id, value FROM priority";

    public List<PriorityDto> findAll() {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_PRIORITIES)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<PriorityDto> allPriorities = new ArrayList<>();
                while (resultSet.next()) {
                    allPriorities.add(new PriorityDto(resultSet.getLong(1), resultSet.getString(2)));
                }
                return allPriorities;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
