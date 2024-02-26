package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.BedTypes;
import com.main.traveltour.entity.RoomBeds;
import com.main.traveltour.repository.BedTypesRepository;
import com.main.traveltour.service.agent.BedTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedTypeServiceImpl implements BedTypeService {

    @Autowired
    private BedTypesRepository bedTypesRepository;

    @Override
    public List<BedTypes> findAllListBedTypes() {
        return bedTypesRepository.findAll();
    }

}
