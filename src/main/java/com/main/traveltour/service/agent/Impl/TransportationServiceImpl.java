package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.Transportations;
import com.main.traveltour.repository.TransportationsRepository;
import com.main.traveltour.service.agent.TransportationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransportationServiceImpl implements TransportationService {

    @Autowired
    private TransportationsRepository transportationsRepository;

    @Override
    public String findMaxCode() {
        return transportationsRepository.fixMaxCode();
    }

    @Override
    public Optional<Transportations> findTransportById(String transportId) {
        return transportationsRepository.findById(transportId);
    }

    @Override
    public Transportations findTransportByLicensePlate(String licensePlate) {
        return transportationsRepository.findByLicensePlate(licensePlate);
    }

    @Override
    public List<Transportations> findAllByTransportBrandId(String transportBrandId) {
        return transportationsRepository.findAllByTransportationBrandId(transportBrandId);
    }

    @Override
    public Page<Transportations> findAllTransports(String brandId, Pageable pageable) {
        return transportationsRepository.findAllTransport(brandId, pageable);
    }

    @Override
    public Page<Transportations> findAllTransportWithSearch(String brandId, String searchTerm, Pageable pageable) {
        return transportationsRepository.findByTransportWithSearch(brandId, searchTerm, pageable);
    }

    @Override
    public Transportations save(Transportations transportations) {
        return transportationsRepository.save(transportations);
    }
}
