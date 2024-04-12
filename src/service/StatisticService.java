package service;

import db.dao.NotificationDao;
import db.dao.SubtaskDao;
import db.dao.TaskDao;
import db.dao.UserDao;
import service.dto.StatisticDto;

public class StatisticService {

    private final UserDao userDao;
    private final TaskDao taskDao;
    private final SubtaskDao subtaskDao;
    private final NotificationDao notificationDao;

    public StatisticService(UserDao userDao, TaskDao taskDao, SubtaskDao subtaskDao, NotificationDao notificationDao) {
        this.userDao = userDao;
        this.taskDao = taskDao;
        this.subtaskDao = subtaskDao;
        this.notificationDao = notificationDao;
    }

    public StatisticDto getStatistics() {
        return new StatisticDto(userDao.getNumberOfUsers(),
                taskDao.getNumberOfCreatedTasks(),
                taskDao.getNumberOfCompletedTasks(),
                subtaskDao.getNumberOfSubtasks(),
                notificationDao.getNumberOfNotifications()
        );
    }
}
