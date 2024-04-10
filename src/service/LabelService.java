package service;

import db.dao.LabelDao;
import service.dto.LabelDto;

import java.util.List;

public class LabelService {
    private final LabelDao labelDao;

    public LabelService(LabelDao labelDao) {
        this.labelDao = labelDao;
    }

    public List<LabelDto> getTaskLabels(String taskId) {
        return labelDao.findLabelsByTaskId(Long.parseLong(taskId));
    }

    public void removeLabel(String labelId) {
        labelDao.remove(Long.parseLong(labelId));
    }

    public void updateTaskLabels(String taskId, String[] labels, String[] labelIds, String[] newLabels) {
        if (labelIds != null) {
            labelDao.updateLabels(labels, labelIds);
        }
        if (newLabels != null) {
            labelDao.createLabels(Long.parseLong(taskId), newLabels);
        }
    }
}
