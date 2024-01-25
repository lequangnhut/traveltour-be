package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.repository.TransportationSchedulesRepository;
import com.main.traveltour.service.agent.TransportationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransportationScheduleServiceImpl implements TransportationScheduleService {

    @Autowired
    private TransportationSchedulesRepository transportationSchedulesRepository;

    @Override
    public String findMaxCode() {
        return transportationSchedulesRepository.fixMaxCode();
    }

    @Override
    public TransportationSchedules findBySchedulesId(String transportSchedulesId) {
        return transportationSchedulesRepository.findById(transportSchedulesId);
    }

    @Override
    public TransportationSchedules save(TransportationSchedules schedules) {
        return transportationSchedulesRepository.save(schedules);
    }

    @Override
    public Page<TransportationSchedules> findAllSchedules(String transportBrandId, Pageable pageable) {
        return transportationSchedulesRepository.findAllSchedules(transportBrandId, pageable);
    }

    @Override
    public Page<TransportationSchedules> findAllSchedulesWitchSearch(String transportBrandId, String searchTerm, Pageable pageable) {
        return transportationSchedulesRepository.findAllSchedulesWithSearch(transportBrandId, searchTerm, pageable);
    }
}
