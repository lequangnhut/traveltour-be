package com.main.traveltour.service.staff;

import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TransportationScheduleService {

    TransportationSchedules findById(String id);

    Page<TransportationSchedules> findBySearchTerm(String searchTerm, Pageable pageable);


    Page<TransportationSchedules> findTransportationSchedulesWithFilter(
            String fromLocation,
            String toLocation,
            Timestamp departureTime,
            Timestamp arrivalTime,
            Integer amountSeat,
            Integer price,
            List<Integer> transportationTypeIdList,
            List<String> listOfVehicleManufacturers,
            Pageable pageable);

    void update(TransportationSchedules transportationSchedules);

    Long countSchedules();
}
