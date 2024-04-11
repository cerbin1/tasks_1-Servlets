package service.dto;

import java.time.LocalDateTime;

public class TaskDto {

    private final Long id;
    private final String name;
    private final LocalDateTime deadline;
    private final String assignee;
    private final String priority;
    private final Long subtasksCount;
    private final Long worklogsCount;
    private final boolean completed;
    private final LocalDateTime completeDate;
    private final String category;

    public TaskDto(Long id, String name, LocalDateTime deadline, String assignee, String priority, Long subtasksCount, Long worklogsCount, boolean completed, LocalDateTime completeDate, String category) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.assignee = assignee;
        this.priority = priority;
        this.subtasksCount = subtasksCount;
        this.worklogsCount = worklogsCount;
        this.completed = completed;
        this.completeDate = completeDate;
        this.category = category;
    }

    public TaskDto(Long id, String name, LocalDateTime deadline, String assignee, String priority, boolean completed, LocalDateTime completeDate, String category) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.assignee = assignee;
        this.priority = priority;
        this.completed = completed;
        this.completeDate = completeDate;
        this.category = category;
        this.subtasksCount = null;
        this.worklogsCount = null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getPriority() {
        return priority;
    }

    public Long getSubtasksCount() {
        return subtasksCount;
    }

    public Long getWorklogsCount() {
        return worklogsCount;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public String getCategory() {
        return category;
    }
}
