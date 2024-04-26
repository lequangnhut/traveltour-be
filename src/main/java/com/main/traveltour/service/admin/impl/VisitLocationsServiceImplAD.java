package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.repository.VisitLocationsRepository;
import com.main.traveltour.service.admin.VisitLocationsServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitLocationsServiceImplAD implements VisitLocationsServiceAD {

    @Autowired
    private VisitLocationsRepository visitLocationsRepository;

    @Override
    public List<VisitLocations> findByVisitLocationTypeId(int typeId) {
        return visitLocationsRepository.findAllByVisitLocationTypeId(typeId);
    }

    @Override
    public Page<VisitLocations> findAllVisitPost(Boolean isAccepted, Pageable pageable) {
        return visitLocationsRepository.findAllVisitPostByAcceptedAndTrueActive(isAccepted, pageable);
    }

    @Override
    public Page<VisitLocations> findAllVisitPostByName(Boolean isAccepted, Pageable pageable, String searchTerm) {
        return visitLocationsRepository.findAllVisitPostByAcceptedAndTrueActiveByName(isAccepted, pageable, searchTerm);
    }

    @Override
    public VisitLocations findById(String id) {
        return visitLocationsRepository.findById(id);
    }

    @Override
    public VisitLocations save(VisitLocations visitLocations) {
        return visitLocationsRepository.save(visitLocations);
    }

    @Override
    public Long countVisitLocationsChart(Integer year) {
        return visitLocationsRepository.countVisitLocationsChart(year);
    }

    @Override
    public List<VisitLocations> findThreeVisitLocation() {
        return visitLocationsRepository.find3PlaceMostOrder();
    }
}
