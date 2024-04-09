package service.dto;

import java.time.LocalDateTime;

public class TaskDto {

    private final Long id;
    private final String name;
    private final LocalDateTime deadline;
    private final String assignee;
    private final String priority;
    private final boolean completed;
    private final LocalDateTime completeDate;

    public TaskDto(Long id, String name, LocalDateTime deadline, String assignee, String priority, boolean completed, LocalDateTime completeDate) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.assignee = assignee;
        this.priority = priority;
        this.completed = completed;
        this.completeDate = completeDate;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public String getPriority() {
        return priority;
    }

    public String getAssignee() {
        return assignee;
    }
}
