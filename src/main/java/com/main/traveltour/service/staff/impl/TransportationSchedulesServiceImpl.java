package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.repository.TransportationSchedulesRepository;
import com.main.traveltour.service.staff.staff.TransportationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransportationSchedulesServiceImpl implements TransportationScheduleService {

    @Autowired
    private TransportationSchedulesRepository repo;


    @Override
    public Page<TransportationSchedules> getAllTransportationSchedules(Pageable pageable) {
        return repo.getAllTransportationSchedules(pageable);
    }

    @Override
    public Page<TransportationSchedules> findBySearchTerm(String searchTerm, Pageable pageable) {
        return null;
    }

    @Override
    public Page<TransportationSchedules> findTransportationByProvince(String location, Pageable pageable) {
        return null;
    }

    @Override
    public Page<TransportationSchedules> findTransportationSchedulesWithFilter
            (String fromLocation, String toLocation,
             Timestamp departureTime, Timestamp arrivalTime, Integer amountSeat,
             Integer price, List<Integer> transportationTypeIdList, List<String> listOfVehicleManufacturers,
             Pageable pageable) {
        return repo.findTransportationSchedulesWithFilter(fromLocation, toLocation,
                departureTime, arrivalTime, amountSeat, price, transportationTypeIdList,
                listOfVehicleManufacturers, pageable);
    }
}
