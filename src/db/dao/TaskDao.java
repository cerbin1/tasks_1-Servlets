package db.dao;

import db.DbConnection;
import service.dto.EditTaskDto;
import service.dto.TaskDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    private static final String SQL_CREATE_TASK = "INSERT INTO task (\"name\", deadline, assignee_id, priority_id, creator_id, category) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String SQL_GET_ALL_TASKS = "SELECT task.id, task.category, task.name, task.deadline, task.completed, task.complete_date," +
            " \"user\".name as assigneeName, priority.value as priorityValue " +
            "FROM task " +
            "JOIN priority ON task.priority_id = priority.id " +
            "JOIN \"user\" ON task.assignee_id = \"user\".id " +
            "ORDER BY task.id";
    private static final String SQL_GET_USERNAME_TASKS = "SELECT task.id, task.category, task.name, task.deadline, task.completed, task.complete_date," +
            " \"user\".name as assigneeName, priority.value as priorityValue " +
            "FROM task " +
            "JOIN priority ON task.priority_id = priority.id " +
            "JOIN \"user\" ON task.assignee_id = \"user\".id " +
            "WHERE \"user\".username = ? " +
            "ORDER BY task.id";

    private static final String SQL_GET_TASK_FOR_EDIT_BY_ID = "SELECT task.id, task.category, task.name, task.deadline, task.completed, task.complete_date," +
            "task.assignee_id, task.priority_id " +
            "FROM task " +
            "WHERE task.id = ?";

    private static final String SQL_GET_TASK_BY_ID = "SELECT task.id, task.category, task.name, task.deadline, task.completed, task.complete_date," +
            " \"user\".name as assigneeName, priority.value as priorityValue " +
            "FROM task " +
            "JOIN priority ON task.priority_id = priority.id " +
            "JOIN \"user\" ON task.assignee_id = \"user\".id " +
            "WHERE task.id = ?";

    private static final String SQL_UPDATE_TASK = "UPDATE task SET name = ?, deadline = ?, assignee_id = ?, priority_id = ?, category = ? WHERE id = ?";

    private static final String SQL_DELETE_TASK_BY_ID = "DELETE FROM task WHERE id = ?";
    private static final String SQL_GET_TASKS_BY_NAME = "SELECT task.id, task.category, task.name, task.deadline, task.completed, task.complete_date," +
            " \"user\".name as assigneeName, priority.value as priorityValue " +
            "FROM task " +
            "JOIN priority ON task.priority_id = priority.id " +
            "JOIN \"user\" ON task.assignee_id = \"user\".id " +
            "WHERE task.\"name\" LIKE '%' || ? || '%' ORDER BY id";

    public Long createTask(String name, LocalDateTime deadline, Long userId, Long priorityId, Long creatorId, String category) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_TASK)) {
            preparedStatement.setString(1, name);
            preparedStatement.setObject(2, deadline);
            preparedStatement.setLong(3, userId);
            preparedStatement.setLong(4, priorityId);
            preparedStatement.setLong(5, creatorId);
            preparedStatement.setString(6, category);

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

    public List<TaskDto> findAll() {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_TASKS)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<TaskDto> tasks = new ArrayList<>();
                while (resultSet.next()) {
                    tasks.add(new TaskDto(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getObject("deadline", LocalDateTime.class),
                            resultSet.getString("assigneeName"),
                            resultSet.getString("priorityValue"),
                            resultSet.getBoolean("completed"),
                            resultSet.getObject("complete_date", LocalDateTime.class),
                            resultSet.getString("category")
                    ));
                }
                return tasks;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TaskDto> findAllByAssigneeUsername(String username) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_USERNAME_TASKS)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<TaskDto> tasks = new ArrayList<>();
                while (resultSet.next()) {
                    tasks.add(new TaskDto(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getObject("deadline", LocalDateTime.class),
                            resultSet.getString("assigneeName"),
                            resultSet.getString("priorityValue"),
                            resultSet.getBoolean("completed"),
                            resultSet.getObject("complete_date", LocalDateTime.class),
                            resultSet.getString("category")
                    ));
                }
                return tasks;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public EditTaskDto findByIdForEdit(Long id) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_TASK_FOR_EDIT_BY_ID)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return new EditTaskDto(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getObject("deadline", LocalDateTime.class),
                        resultSet.getLong("assignee_id"),
                        resultSet.getLong("priority_id"),
                        resultSet.getString("category"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateById(Long taskId, String name, LocalDateTime deadline, Long assigneeId, Long priorityId, String category) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_TASK)) {
            preparedStatement.setString(1, name);
            preparedStatement.setObject(2, deadline);
            preparedStatement.setLong(3, assigneeId);
            preparedStatement.setLong(4, priorityId);
            preparedStatement.setString(5, category);
            preparedStatement.setLong(6, taskId);
            boolean taskUpdated = preparedStatement.executeUpdate() == 1;
            if (!taskUpdated) {
                throw new SQLException();
            }
            connection.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TaskDto findById(Long taskId) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_TASK_BY_ID)) {
                preparedStatement.setLong(1, taskId);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return new TaskDto(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getObject("deadline", LocalDateTime.class),
                        resultSet.getString("assigneeName"),
                        resultSet.getString("priorityValue"),
                        resultSet.getBoolean("completed"),
                        resultSet.getObject("complete_date", LocalDateTime.class),
                        resultSet.getString("category")
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeById(Long taskId) {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.createConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_TASK_BY_ID)) {
            preparedStatement.setLong(1, taskId);
            boolean taskDeleted = preparedStatement.executeUpdate() == 1;
            if (!taskDeleted) {
                throw new SQLException();
            }
            connection.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TaskDto> findAllByName(String name) {
        DbConnection dbConnection = new DbConnection();
        try (Connection connection = dbConnection.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_TASKS_BY_NAME)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<TaskDto> tasks = new ArrayList<>();
                while (resultSet.next()) {
                    tasks.add(new TaskDto(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getObject("deadline", LocalDateTime.class),
                            resultSet.getString("assigneeName"),
                            resultSet.getString("priorityValue"),
                            resultSet.getBoolean("completed"),
                            resultSet.getObject("complete_date", LocalDateTime.class),
                            resultSet.getString("category")
                    ));
                }
                return tasks;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
