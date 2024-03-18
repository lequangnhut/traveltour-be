package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransportationScheduleService {

    String findMaxCode();

    TransportationSchedules findBySchedulesId(String transportSchedulesId);

    TransportationSchedules save(TransportationSchedules schedules);

    List<TransportationSchedules> findByTransportId(String transportId);

    List<TransportationSchedules> findAllScheduleByBrandId(String transportBrandId);

    List<TransportationSchedules> getAllFromLocationAndToLocation();

    Page<TransportationSchedules> findAllSchedules(String transportBrandId, Pageable pageable);

    Page<TransportationSchedules> findAllSchedulesWitchSearch(String transportBrandId, String searchTerm, Pageable pageable);

    Page<TransportationSchedules> findAllTransportScheduleCus(Pageable pageable, String brandId);

    void updateStatusAndActive();
}
