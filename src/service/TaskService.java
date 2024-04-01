package service;

import db.TaskRepository;

import java.time.LocalDateTime;

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean create(String name, String deadline, String userId, String priorityId, String creatorId) {
        return taskRepository.createTask(name, LocalDateTime.parse(deadline), Long.parseLong(userId), Long.parseLong(priorityId), Long.parseLong(creatorId));
    }
}
