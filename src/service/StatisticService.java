package service;

import db.dao.*;
import service.dto.*;

import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.*;

public class StatisticService {

    private final UserDao userDao;
    private final TaskDao taskDao;
    private final SubtaskDao subtaskDao;
    private final NotificationDao notificationDao;
    private final WorklogDao worklogDao;

    public StatisticService(UserDao userDao, TaskDao taskDao, SubtaskDao subtaskDao, NotificationDao notificationDao, WorklogDao worklogDao) {
        this.userDao = userDao;
        this.taskDao = taskDao;
        this.subtaskDao = subtaskDao;
        this.notificationDao = notificationDao;
        this.worklogDao = worklogDao;
    }

    public StatisticDto getGeneralStatistics() {
        return new StatisticDto(userDao.getNumberOfUsers(),
                taskDao.getNumberOfCreatedTasks(),
                taskDao.getNumberOfCompletedTasks(),
                subtaskDao.getNumberOfSubtasks(),
                notificationDao.getNumberOfNotifications()
        );
    }

    public List<TasksCountForDateDto> getNumberOfTasks() {
        Map<LocalDate, Long> groupedByDate =
                taskDao.findAll().stream()
                        .map(task -> task.getDeadline().toLocalDate())
                        .sorted()
                        .collect(toList())
                        .stream()
                        .collect(groupingBy(date -> date, counting()))
                        .entrySet().stream()
                        .sorted(comparingByKey())
                        .collect(toMap(Entry::getKey, Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));

        Map<LocalDate, Long> counts = new TreeMap<>();
        groupedByDate.keySet().forEach(localDate -> counts.put(localDate, groupedByDate.get(localDate)));

        Map<LocalDate, Long> sortedByDate = counts.entrySet().stream()
                .sorted(comparingByKey())
                .collect(toMap(Entry::getKey, Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));

        List<TasksCountForDateDto> result = new ArrayList<>();
        sortedByDate.forEach((date, count) -> result.add(new TasksCountForDateDto(date, count)));

        return result;
    }

    public List<TimeLoggedDto> getTimeLoggedByUsers() {
        List<UserDto> users = userDao.findAll();
        List<TimeLoggedDto> timeLoggedByUsersList = new ArrayList<>();
        users.forEach(user -> {
            List<TaskDto> userTasks = taskDao.findAllByAssigneeId(user.getId());
            AtomicReference<Long> sum = new AtomicReference<>(0L);
            userTasks.forEach(userTask -> {
                List<WorklogDto> taskWorklogs = worklogDao.findAllByTaskId(userTask.getId());
                taskWorklogs.forEach(worklog -> {
                    sum.updateAndGet(v -> v + worklog.getMinutes());
                });
            });
            timeLoggedByUsersList.add(new TimeLoggedDto(user.getUsername(), sum.get()));
        });
        return timeLoggedByUsersList;
    }
}
