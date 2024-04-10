package service.dto;

import java.time.LocalDateTime;

public class EditTaskDto {

    private final Long id;
    private final String name;
    private final LocalDateTime deadline;
    private final Long assigneeId;
    private final Long priorityId;
    private final String category;

    public EditTaskDto(Long id, String name, LocalDateTime deadline, Long assigneeId, Long priorityId, String category) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.assigneeId = assigneeId;
        this.priorityId = priorityId;
        this.category = category;
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

    public Long getAssigneeId() {
        return assigneeId;
    }

    public Long getPriorityId() {
        return priorityId;
    }

    public String getCategory() {
        return category;
    }
}
