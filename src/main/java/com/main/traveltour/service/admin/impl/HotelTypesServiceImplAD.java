package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.HotelTypes;
import com.main.traveltour.repository.HotelTypesRepository;
import com.main.traveltour.service.admin.HotelTypesServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HotelTypesServiceImplAD implements HotelTypesServiceAD {

    @Autowired
    private HotelTypesRepository hotelTypesRepository;

    @Override
    public Page<HotelTypes> findAll(Pageable pageable) {
        return hotelTypesRepository.findAll(pageable);
    }

    @Override
    public Page<HotelTypes> findAllWithSearch(String searchTerm, Pageable pageable) {
        return hotelTypesRepository.findByHotelTypeNameContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public HotelTypes findByHotelTypeName(String name) {
        return hotelTypesRepository.findByHotelTypeName(name);
    }

    @Override
    public HotelTypes findById(int id) {
        return hotelTypesRepository.findById(id);
    }

    @Override
    public HotelTypes save(HotelTypes hotelTypes) {
        return hotelTypesRepository.save(hotelTypes);
    }

    @Override
    public HotelTypes delete(int id) {
        hotelTypesRepository.deleteById(id);
        return null;
    }

}
