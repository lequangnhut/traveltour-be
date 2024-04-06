package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.RequestCar;
import com.main.traveltour.repository.RequestCarRepository;
import com.main.traveltour.service.staff.RequestCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class RequestCarServiceImpl implements RequestCarService {

    @Autowired
    private RequestCarRepository repo;

    @Override
    public Page<RequestCar> findAllRequestCarPage(Pageable pageable) {
        return repo.findAll(pageable);
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
}
