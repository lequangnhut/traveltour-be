package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.service.staff.staff.HotelServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class HotelServiceServiceImpl implements HotelServiceService {

    @Autowired
    private HotelsRepository repo;

    @Override
    public Page<Hotels> findAllHotel(Pageable pageable) {
        return repo.findAllHotel(pageable);
    }

    @Override
    public Page<Hotels> findBySearchTerm(String searchTerm, Pageable pageable) {
        return repo.findBySearchTerm(searchTerm, pageable);
    }

    @Override
    public Page<Hotels> findAvailableHotelsWithFilters(String location, Timestamp departureDate, Timestamp arrivalDate, Integer numAdults, Integer numChildren, Integer numRooms, Pageable pageable) {
        return repo.findAvailableHotelsWithFilters(location, departureDate, arrivalDate, numAdults, numChildren, numRooms, pageable);
    }

    @Override
    public Hotels getHotelsById(String id) {
        return repo.getHotelsById(id);
    }

}
