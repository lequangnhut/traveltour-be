package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.VisitLocationTypes;
import com.main.traveltour.repository.VisitLocationTypesRepository;
import com.main.traveltour.service.admin.VisitLocationTypesServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitLocationTypesServiceImplAD implements VisitLocationTypesServiceAD {

    @Autowired
    VisitLocationTypesRepository visitLocationTypesRepository;

    @Override
    public List<VisitLocationTypes> findAll() {
        return visitLocationTypesRepository.findAll();
    }

    @Override
    public Page<VisitLocationTypes> findAll(Pageable pageable) {
        return visitLocationTypesRepository.findAll(pageable);
    }

    @Override
    public Page<VisitLocationTypes> findAllWithSearch(String searchTerm, Pageable pageable) {
        return visitLocationTypesRepository.findByVisitLocationTypeNameContainingIgnoreCase(searchTerm,pageable);
    }

    @Override
    public VisitLocationTypes findByVisitLocationTypeName(String name) {
        return visitLocationTypesRepository.findByVisitLocationTypeName(name);
    }

    @Override
    public VisitLocationTypes findById(int id) {
        return visitLocationTypesRepository.findById(id);
    }

    @Override
    public VisitLocationTypes save(VisitLocationTypes visitLocationTypes) {
        return visitLocationTypesRepository.save(visitLocationTypes);
    }

    @Override
    public VisitLocationTypes delete(int id) {
        visitLocationTypesRepository.deleteById(id);
        return null;
    }
}
