package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.repository.TransportationSchedulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransportationScheduleServiceImpl implements TransportationScheduleService {

    @Autowired
    TransportationSchedulesRepository transportationSchedulesRepository;

    @Override
    public String findMaxCode() {
        return transportationSchedulesRepository.fixMaxCode();
    }

    @Override
    public Page<TransportationSchedules> findAllTransportationSchedules(Pageable pageable) {
        return transportationSchedulesRepository.findAll(pageable);
    }

    @Override
    public Page<TransportationSchedules> findTransportationSchedulesWithSearch(String searchTerm, Pageable pageable) {
        return transportationSchedulesRepository.findByTransportationSchedulesWithSearch(searchTerm, pageable);
    }

    @Override
    public TransportationSchedules findByTransportationScheduleId(String id) {
        return transportationSchedulesRepository.findById(id);
    }

    @Override
    public TransportationSchedules save(TransportationSchedules transportationSchedules) {
        return transportationSchedulesRepository.save(transportationSchedules);
    }
}
