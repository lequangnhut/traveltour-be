package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.repository.TransportationSchedulesRepository;
import com.main.traveltour.service.agent.TransportationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

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
    public List<TransportationSchedules> findByTransportId(String transportId) {
        return transportationSchedulesRepository.findByTransportationId(transportId);
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

    @Override
    public void updateStatusAndActive() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime.truncatedTo(java.time.temporal.ChronoUnit.SECONDS));

        List<TransportationSchedules> transportationSchedules1 = transportationSchedulesRepository.findByDepartureTimeAndIsActiveTrue(timestamp);
        List<TransportationSchedules> transportationSchedule2 = transportationSchedulesRepository.findByArrivalTimeAndIsActiveTrue(timestamp);

        for (TransportationSchedules schedules : transportationSchedules1) {
            schedules.setIsStatus(1);
            transportationSchedulesRepository.save(schedules);
        }
        for (TransportationSchedules schedules : transportationSchedule2) {
            schedules.setIsStatus(2);
            transportationSchedulesRepository.save(schedules);
        }
    }
}
