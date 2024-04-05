package service;

public class NotificationDto {
    private final Long id;
    private final String name;
    private final String taskName;
    private final String createDate;
    private final Boolean read;
    private final String readDate;
    private final String userNameAssigned;
    private final Long taskId;

    public NotificationDto(Long id, String name, String taskName, String createDate, Boolean read, String readDate, String userNameAssigned, Long taskId) {
        this.id = id;
        this.name = name;
        this.taskName = taskName;
        this.createDate = createDate;
        this.read = read;
        this.readDate = readDate;
        this.userNameAssigned = userNameAssigned;
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public Boolean isRead() {
        return read;
    }

    public String getReadDate() {
        return readDate;
    }

    public String getUserNameAssigned() {
        return userNameAssigned;
    }

    public Long getTaskId() {
        return taskId;
    }
}
