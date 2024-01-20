package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.VisitLocationTypes;
import com.main.traveltour.repository.VisitLocationTypesRepository;
import com.main.traveltour.service.agent.VisitLocationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitLocationTypeServiceImpl implements VisitLocationTypeService {

    @Autowired
    private VisitLocationTypesRepository visitLocationTypesRepository;

    @Override
    public List<VisitLocationTypes> findAllForRegisterAgency() {
        return visitLocationTypesRepository.findAll();
    }
}
