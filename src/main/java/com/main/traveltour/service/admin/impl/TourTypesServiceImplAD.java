package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.TourTypes;
import com.main.traveltour.repository.TourTypesRepository;
import com.main.traveltour.service.admin.TourTypesServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TourTypesServiceImplAD implements TourTypesServiceAD {

    @Autowired
    private TourTypesRepository tourTypesRepository;

    @Override
    public Page<TourTypes> findAll(Pageable pageable) {
        return tourTypesRepository.findAll(pageable);
    }

    @Override
    public Page<TourTypes> findAllWithSearch(String searchTerm, Pageable pageable) {
        return tourTypesRepository.findByTourTypeNameContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public TourTypes findByTourTypeName(String name) {
        return tourTypesRepository.findByTourTypeName(name);
    }

    @Override
    public TourTypes findById(int id) {
        return tourTypesRepository.findAllById(id);
    }

    @Override
    public TourTypes save(TourTypes tourTypes) {
        return tourTypesRepository.save(tourTypes);
    }

    @Override
    public TourTypes delete(int id) {
        tourTypesRepository.deleteById(id);
        return null;
    }
}
