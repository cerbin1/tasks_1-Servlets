package service;

import db.dao.TaskReminderDao;
import service.dto.TaskReminderDto;

import java.time.LocalDateTime;
import java.util.List;

public class TaskReminderService {
    private final TaskReminderDao taskReminderDao;

    public TaskReminderService(TaskReminderDao taskReminderDao) {
        this.taskReminderDao = taskReminderDao;
    }

    public void createTaskReminder(Long taskId, LocalDateTime localDateTime) {
        taskReminderDao.create(taskId, localDateTime);
    }

    public List<TaskReminderDto> getRemindersToSend() {
        return taskReminderDao.getNotSentTaskReminders();
    }

    public void markReminderAsSent(Long reminderId) {
        taskReminderDao.setReminderAsSent(reminderId);
    }
}
