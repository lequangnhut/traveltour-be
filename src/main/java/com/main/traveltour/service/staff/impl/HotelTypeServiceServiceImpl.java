package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.HotelTypes;
import com.main.traveltour.repository.HotelTypesRepository;
import com.main.traveltour.service.staff.HotelTypeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelTypeServiceServiceImpl implements HotelTypeServiceService {
    @Autowired
    private HotelTypesRepository repo;

    @Override
    public HotelTypes findById(int id) {
        return repo.findById(id);
    }
}
