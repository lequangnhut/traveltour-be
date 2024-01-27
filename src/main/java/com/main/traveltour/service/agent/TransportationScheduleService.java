package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransportationScheduleService {

    String findMaxCode();

    TransportationSchedules findBySchedulesId(String transportSchedulesId);

    TransportationSchedules save(TransportationSchedules schedules);

    List<TransportationSchedules> findByTransportId(String transportId);

    Page<TransportationSchedules> findAllSchedules(String transportBrandId, Pageable pageable);

    Page<TransportationSchedules> findAllSchedulesWitchSearch(String transportBrandId, String searchTerm, Pageable pageable);

    void updateStatusAndActive();
}
