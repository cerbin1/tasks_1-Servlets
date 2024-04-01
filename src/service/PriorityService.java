package service;

import db.PriorityRepository;

import java.util.List;

public class PriorityService {
    private final PriorityRepository priorityRepository;

    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public List<PriorityDto> getPrioritiesData() {
        return priorityRepository.findAll();
    }

}
