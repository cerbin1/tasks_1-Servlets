package service.dto;

public class StatisticDto {
    private final Long numberOfUsers;
    private final Long numberOfCreatedTasks;
    private final Long numberOfCompletedTasks;
    private final Long numberOfSubtasks;
    private final Long numberOfNotifications;

    public StatisticDto(Long numberOfUsers, Long numberOfCreatedTasks, Long numberOfCompletedTasks, Long numberOfSubtasks, Long numberOfNotifications) {
        this.numberOfUsers = numberOfUsers;
        this.numberOfCreatedTasks = numberOfCreatedTasks;
        this.numberOfCompletedTasks = numberOfCompletedTasks;
        this.numberOfSubtasks = numberOfSubtasks;
        this.numberOfNotifications = numberOfNotifications;
    }

    public Long getNumberOfUsers() {
        return numberOfUsers;
    }

    public Long getNumberOfCreatedTasks() {
        return numberOfCreatedTasks;
    }

    public Long getNumberOfCompletedTasks() {
        return numberOfCompletedTasks;
    }

    public Long getNumberOfSubtasks() {
        return numberOfSubtasks;
    }

    public Long getNumberOfNotifications() {
        return numberOfNotifications;
    }
}
