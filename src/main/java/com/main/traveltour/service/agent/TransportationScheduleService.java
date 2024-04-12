package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransportationScheduleService {

    String findMaxCode();

    TransportationSchedules findBySchedulesId(String transportSchedulesId);

    TransportationSchedules findCarIsSubmittedAgent(String transportSchedulesId);

    TransportationSchedules save(TransportationSchedules schedules);

    List<TransportationSchedules> findByTransportId(String transportId);

    List<TransportationSchedules> findAllScheduleByBrandIdRequestCar(String transportBrandId);

    List<TransportationSchedules> findAllScheduleByBrandId(String transportBrandId);

    List<TransportationSchedules> getAllFromLocationAndToLocation();

    Page<TransportationSchedules> findAllScheduleAgent(String transportBrandId, Boolean tripType, Pageable pageable);

    Page<TransportationSchedules> findAllScheduleAgentWitchSearch(String transportBrandId, Boolean tripType, String searchTerm, Pageable pageable);

    Page<TransportationSchedules> findAllTransportScheduleCusFilters(
            String brandId,
            BigDecimal price,
            String fromLocation,
            String toLocation,
            Date checkInDateFiller,
            List<Integer> mediaTypeList,
            List<String> listOfVehicleManufacturers,
            Pageable pageable);

    void updateStatusAndActive();
}
