package service;

import db.dao.PriorityDao;
import service.dto.PriorityDto;

import java.util.List;

public class PriorityService {
    private final PriorityDao priorityDao;

    public PriorityService(PriorityDao priorityDao) {
        this.priorityDao = priorityDao;
    }

    public List<PriorityDto> getPrioritiesData() {
        return priorityDao.findAll();
    }

}
