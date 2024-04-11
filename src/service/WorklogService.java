package service;

import db.dao.WorklogDao;
import service.dto.WorklogDto;

import java.time.LocalDate;
import java.util.List;

public class WorklogService {
    private final WorklogDao worklogDao;

    public WorklogService(WorklogDao worklogDao) {
        this.worklogDao = worklogDao;
    }

    public List<WorklogDto> getTaskWorklogs(String taskId) {
        return worklogDao.findAllByTaskId(Long.parseLong(taskId));
    }

    public void createWorklog(String date, String minutes, String comment, String creatorId, String taskId) {
        worklogDao.createWorklog(LocalDate.parse(date), Long.parseLong(minutes), comment, Long.parseLong(creatorId), Long.parseLong(taskId));
    }

    public void removeWorklog(String worklogId) {
        worklogDao.removeWorklog(Long.parseLong(worklogId));
    }

    public void updateWorklog(String worklogId, String date, String minutes, String comment) {
        worklogDao.updateById(Long.parseLong(worklogId), LocalDate.parse(date), Long.parseLong(minutes), comment);
    }
}
