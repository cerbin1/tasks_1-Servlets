package service;

import db.dao.SubtaskDao;
import db.dao.TaskDao;

import java.time.LocalDateTime;
import java.util.List;

public class TaskService {
    private final TaskDao taskDao;
    private final SubtaskDao subtaskDao;

    public TaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
        this.subtaskDao = new SubtaskDao();
    }

    public Long create(String name, String deadline, String userId, String priorityId, String creatorId, String[] subtasks) {
        Long taskId = taskDao.createTask(name, LocalDateTime.parse(deadline), Long.parseLong(userId), Long.parseLong(priorityId), Long.parseLong(creatorId));
        subtaskDao.createSubtasks(taskId, subtasks);
        return taskId;
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

    public boolean updateTaskAndSubtasks(String taskId, String name, String deadline, String userId, String priorityId,
                                         String[] subtasksNames,
                                         String[] subtasksIds,
                                         String[] newSubtasks) {
        boolean success = taskDao.updateById(
                Long.parseLong(taskId),
                name,
                LocalDateTime.parse(deadline),
                Long.parseLong(userId),
                Long.parseLong(priorityId));
        if (subtasksIds != null) {
            subtaskDao.updateSubtasks(subtasksNames, subtasksIds);
        }
        if (newSubtasks != null) {
            subtaskDao.createSubtasks(Long.parseLong(taskId), newSubtasks);
        }
        return success;
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
