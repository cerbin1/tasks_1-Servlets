package service;

import db.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean create(String name, String deadline, String userId, String priorityId, String creatorId) {
        return taskRepository.createTask(name, LocalDateTime.parse(deadline), Long.parseLong(userId), Long.parseLong(priorityId), Long.parseLong(creatorId));
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<TaskDto> getTasksForUser(String username) {
        return taskRepository.findAllByAssigneeUsername(username);
    }
}
