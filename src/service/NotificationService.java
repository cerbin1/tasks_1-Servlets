package service;

import db.dao.NotificationDao;

public class NotificationService {

    private final NotificationDao notificationDao;

    public NotificationService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void createNotification(NotificationDto notificationDto) {
        notificationDao.create(notificationDto);
    }
}
