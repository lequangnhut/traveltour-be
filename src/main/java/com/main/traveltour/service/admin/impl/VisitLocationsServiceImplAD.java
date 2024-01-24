package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.repository.VisitLocationsRepository;
import com.main.traveltour.service.admin.VisitLocationsServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
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
}
