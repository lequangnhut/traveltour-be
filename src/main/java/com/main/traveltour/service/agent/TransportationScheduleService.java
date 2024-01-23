package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransportationScheduleService {

    String findMaxCode();

    Page<TransportationSchedules> findAllTransportationSchedules(Pageable pageable);

    Page<TransportationSchedules> findTransportationSchedulesWithSearch(String searchTerm, Pageable pageable);

    TransportationSchedules findByTransportationScheduleId(String id);

    TransportationSchedules save(TransportationSchedules transportationSchedules);
}
