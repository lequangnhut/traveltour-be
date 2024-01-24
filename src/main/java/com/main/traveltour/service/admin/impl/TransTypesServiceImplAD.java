package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.TransportationTypes;
import com.main.traveltour.repository.TransportationTypesRepository;
import com.main.traveltour.service.admin.TourTypesServiceAD;
import com.main.traveltour.service.admin.TransTypeServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransTypesServiceImplAD implements TransTypeServiceAD {

    @Autowired
    private TransportationTypesRepository transportationTypesRepository;

    @Override
    public Page<TransportationTypes> findAll(Pageable pageable) {
        return transportationTypesRepository.findAll(pageable);
    }

    @Override
    public Page<TransportationTypes> findAllWithSearch(String searchTerm, Pageable pageable) {
        return transportationTypesRepository.findByTransportationTypeNameContainingIgnoreCase(searchTerm,pageable);
    }

    @Override
    public TransportationTypes findByTransportationTypeName(String name) {
        return transportationTypesRepository.findByTransportationTypeName(name);
    }

    @Override
    public TransportationTypes findById(int id) {
        return transportationTypesRepository.findById(id);
    }

    @Override
    public TransportationTypes save(TransportationTypes transportationTypes) {
        return transportationTypesRepository.save(transportationTypes);
    }

    @Override
    public TransportationTypes delete(int id) {
        transportationTypesRepository.deleteById(id);
        return null;
    }
}
