package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.BedTypes;
import com.main.traveltour.repository.BedTypesRepository;
import com.main.traveltour.service.admin.BedTypesServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedTypesServiceImplAD implements BedTypesServiceAD {

    @Autowired
    private BedTypesRepository bedTypesRepository;

    @Override
    public Page<BedTypes> findAll(Pageable pageable) {
        return bedTypesRepository.findAll(pageable);
    }

    @Override
    public Page<BedTypes> findAllWithSearch(String searchTerm, Pageable pageable) {
        return bedTypesRepository.findByBedTypeNameContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public BedTypes findByBedTypeName(String name) {
        return bedTypesRepository.findByBedTypeName(name);
    }

    @Override
    public BedTypes findById(int id) {
        return bedTypesRepository.findById(id);
    }

    @Override
    public List<String> findByRoomTypeId(String roomTypeId) {
        return bedTypesRepository.findByRoomTypeId(roomTypeId);
    }

    @Override
    public BedTypes save(BedTypes bedTypes) {
        return bedTypesRepository.save(bedTypes);
    }

    @Override
    public BedTypes delete(int id) {
        bedTypesRepository.deleteById(id);
        return null;
    }

}
