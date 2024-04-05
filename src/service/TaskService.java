package service;

import db.dao.TaskDao;

import java.time.LocalDateTime;
import java.util.List;

public class TaskService {
    private final TaskDao taskDao;

    public TaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public Long create(String name, String deadline, String userId, String priorityId, String creatorId) {
        return taskDao.createTask(name, LocalDateTime.parse(deadline), Long.parseLong(userId), Long.parseLong(priorityId), Long.parseLong(creatorId));
    }

    public List<TaskDto> getAllTasks() {
        return taskDao.findAll();
    }

    public List<TaskDto> getTasksForUser(String username) {
        return taskDao.findAllByAssigneeUsername(username);
    }

    public EditTaskDto getTaskForEdit(String taskId) {
        return taskDao.findByIdForEdit(Long.parseLong(taskId));
    }

    public boolean updateTask(String taskId, String name, String deadline, String userId, String priorityId) {
        return taskDao.updateById(
                Long.parseLong(taskId),
                name,
                LocalDateTime.parse(deadline),
                Long.parseLong(userId),
                Long.parseLong(priorityId));
    }

    public TaskDto getTask(String taskId) {
        return taskDao.findById(Long.parseLong(taskId));
    }

    public boolean removeTask(String taskId) {
        return taskDao.removeById(Long.parseLong(taskId));
    }

    public List<TaskDto> getTasksByName(String name) {
        return taskDao.findAllByName(name);
    }
}
