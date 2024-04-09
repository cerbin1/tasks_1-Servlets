package service.dto;

public class TaskReminderDto {
    private final Long id;
    private final Long taskId;
    private final String taskName;
    private final String assigneeEmail;

    public TaskReminderDto(Long id, Long taskId, String taskName, String assigneeEmail) {
        this.id = id;
        this.taskId = taskId;
        this.taskName = taskName;
        this.assigneeEmail = assigneeEmail;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public Long getId() {
        return id;
    }
}
