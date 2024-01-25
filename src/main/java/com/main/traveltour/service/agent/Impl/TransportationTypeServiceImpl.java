package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationTypes;
import com.main.traveltour.repository.TransportationTypesRepository;
import com.main.traveltour.service.agent.TransportationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportationTypeServiceImpl implements TransportationTypeService {

    @Autowired
    private TransportationTypesRepository transportationTypesRepository;

    @Override
    public List<TransportationTypes> findAllTransportType() {
        return transportationTypesRepository.findAll();
    }

    @Override
    public TransportationTypes findByTransportTypeId(int transportTypeId) {
        return transportationTypesRepository.findById(transportTypeId);
    }
}
