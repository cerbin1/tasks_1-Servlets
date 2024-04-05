package service;

public class NotificationDto {
    private final String name;
    private final Long taskId;
    private final Long userId;

    public NotificationDto(String name, Long taskId, String userId) {
        this.name = name;
        this.taskId = taskId;
        this.userId = Long.parseLong(userId);
    }

    public String getName() {
        return name;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Long getUserId() {
        return userId;
    }
}
