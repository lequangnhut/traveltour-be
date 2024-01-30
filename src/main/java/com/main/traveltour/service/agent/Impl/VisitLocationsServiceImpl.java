package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.repository.VisitLocationsRepository;
import com.main.traveltour.service.agent.VisitLocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitLocationsServiceImpl implements VisitLocationsService {

    @Autowired
    private VisitLocationsRepository visitLocationsRepository;

    @Override
    public String findMaxCode() {
        return visitLocationsRepository.findMaxCode();
    }

    @Override
    public List<VisitLocations> findAllByAgencyId(int agencyId) {
        return visitLocationsRepository.findAllByAgenciesIdAndIsActiveTrue(agencyId);
    }

    @Override
    public VisitLocations findByAgencyId(int agencyId) {
        return visitLocationsRepository.findByAgenciesId(agencyId);
    }

    @Override
    public VisitLocations findByVisitLocationId(String visitLocationId) {
        return visitLocationsRepository.findById( visitLocationId);
    }

    @Override
    public VisitLocations save(VisitLocations visitLocations) {
        return visitLocationsRepository.save(visitLocations);
    }
}
