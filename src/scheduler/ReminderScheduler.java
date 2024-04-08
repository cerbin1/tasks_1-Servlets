package scheduler;

import db.dao.TaskReminderDao;
import service.EmailSendingService;
import service.TaskReminderDto;
import service.TaskReminderService;

import java.util.List;

import static conf.ApplicationProperties.APP_BASE_PATH;

public class ReminderScheduler {
    private final EmailSendingService emailSendingService;

    private final TaskReminderService taskReminderService;

    public ReminderScheduler() {
        emailSendingService = new EmailSendingService();
        taskReminderService = new TaskReminderService(new TaskReminderDao());
    }

    public void sendEmailReminders() {
        List<TaskReminderDto> remindersToSend = taskReminderService.getRemindersToSend();
        remindersToSend.forEach(taskReminder -> {
            String linkToTaskDetails = APP_BASE_PATH + "/details/taskId=" + +taskReminder.getTaskId();
            String emailContent = String.format("I would like to remind you that you have a task to accomplish. Task name: %s Link: %s",
                    taskReminder.getTaskName(), linkToTaskDetails);
            emailSendingService.sendEmail("Task reminder", emailContent, taskReminder.getAssigneeEmail());
            taskReminderService.markReminderAsSent(taskReminder.getId());
        });
    }
}
