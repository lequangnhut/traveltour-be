package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.repository.TransportationSchedulesRepository;
import com.main.traveltour.service.staff.TransportationScheduleService;
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
    public TransportationSchedules findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Page<TransportationSchedules> findBySearchTerm(String searchTerm, Pageable pageable) {
        return null;
    }

    @Override
    public Page<TransportationSchedules> findTransportationSchedulesWithFilter
            (String fromLocation, String toLocation,
             Timestamp departureTime, Timestamp arrivalTime, Integer amountSeat,
             Integer price, List<Integer> transportationTypeIdList, List<String> listOfVehicleManufacturers,
             Pageable pageable) {
        return repo.findTransportationSchedulesWithFilter(
                fromLocation,
                toLocation,
                departureTime,
                arrivalTime,
                amountSeat,
                price,
                transportationTypeIdList,
                listOfVehicleManufacturers,
                pageable
        );
    }

    @Override
    public void update(TransportationSchedules transportationSchedules) {
        repo.save(transportationSchedules);
    }

    @Override
    public Long countSchedules() {
        return repo.countTransportationSchedules();
    }
}
