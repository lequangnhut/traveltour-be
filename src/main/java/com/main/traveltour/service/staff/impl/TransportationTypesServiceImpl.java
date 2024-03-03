package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TransportationTypes;
import com.main.traveltour.repository.TransportationTypesRepository;
import com.main.traveltour.service.staff.TransportationTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportationTypesServiceImpl implements TransportationTypesService {
    @Autowired
    private TransportationTypesRepository repo;

    @Override
    public List<TransportationTypes> getAllTransportationTypes() {
        return repo.findAll();
    }
}
