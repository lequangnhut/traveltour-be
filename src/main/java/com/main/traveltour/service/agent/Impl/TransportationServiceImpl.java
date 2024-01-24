package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.Transportations;
import com.main.traveltour.repository.TransportationsRepository;
import com.main.traveltour.service.agent.TransportationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransportationServiceImpl implements TransportationService {

    @Autowired
    private TransportationsRepository transportationsRepository;

    @Override
    public String findMaxCode() {
        return transportationsRepository.fixMaxCode();
    }

    @Override
    public Transportations findTransportById(String transportId) {
        return transportationsRepository.findById(transportId);
    }

    @Override
    public Page<Transportations> findAllTransports(Pageable pageable) {
        return transportationsRepository.findAll(pageable);
    }

    @Override
    public Page<Transportations> findAllTransportWithSearch(String searchTerm, Pageable pageable) {
        return transportationsRepository.findByTransportWithSearch(searchTerm, pageable);
    }

    @Override
    public Transportations save(Transportations transportations) {
        return transportationsRepository.save(transportations);
    }
}
