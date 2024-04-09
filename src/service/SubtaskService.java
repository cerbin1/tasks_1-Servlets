package service;

import db.dao.SubtaskDao;

import java.util.List;

public class SubtaskService {
    private final SubtaskDao subtaskDao;

    public SubtaskService(SubtaskDao subtaskDao) {
        this.subtaskDao = subtaskDao;
    }

    public List<SubtaskDto> getTaskSubtasks(String taskId) {
        return subtaskDao.findAllByTaskId(Long.parseLong(taskId));
    }

    public void removeSubtask(String subtaskId) {
        subtaskDao.remove(Long.parseLong(subtaskId));
    }
}
