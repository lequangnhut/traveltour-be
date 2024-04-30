package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.RequestCar;
import com.main.traveltour.repository.RequestCarRepository;
import com.main.traveltour.service.staff.RequestCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequestCarServiceImpl implements RequestCarService {

    @Autowired
    private RequestCarRepository repo;

    @Override
    public Page<RequestCar> findAllRequestCarPage(Pageable pageable) {
        return repo.findAllByOrderByIsAcceptedAsc(pageable);
    }

    @Override
    public List<RequestCar> checkExitsTourDetail(String tourDetailId) {
        return repo.findAllByTourDetailId(tourDetailId);
    }

    @Override
    public Optional<RequestCar> findRequestCarById(Integer requestCarId) {
        return repo.findById(requestCarId);
    }

    @Override
    public RequestCar findTransportBrandSubmitted(Integer requestCarId, String transportBrandId) {
        return repo.findTransportBrandSubmitted(requestCarId, transportBrandId);
    }

    @Override
    public RequestCar save(RequestCar requestCar) {
        return repo.save(requestCar);
    }

    @Override
    public Page<RequestCar> findAllRequestCarsFilters(String fromLocation, String toLocation, List<Integer> mediaTypeList, List<String> listOfVehicleManufacturers, List<Boolean> seatTypeList, Pageable pageable) {
        return repo.findAllRequestCarsFilters(fromLocation, toLocation, mediaTypeList, listOfVehicleManufacturers, seatTypeList, pageable);
    }
}
