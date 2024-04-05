package service;

import db.dao.NotificationDao;

import java.util.List;

public class NotificationService {

    private final NotificationDao notificationDao;

    public NotificationService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void createNotification(String name, Long taskId, String userId) {
        notificationDao.create(name, taskId, Long.parseLong(userId));
    }

    public List<NotificationDto> getAllNotifications() {
        return notificationDao.findAll();
    }
}
