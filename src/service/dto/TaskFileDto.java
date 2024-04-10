package service.dto;

public class TaskFileDto {
    private final String name;
    private final String type;
    private final Long taskId;

    public TaskFileDto(String name, String type, Long taskId) {
        this.name = name;
        this.type = type;
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Long getTaskId() {
        return taskId;
    }
}
