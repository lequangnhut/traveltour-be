package com.main.traveltour.service.staff.staff;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.service.staff.HotelServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;

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
    public Page<Hotels> findHotelsWithFilters(String location, Date departureDate, Date arrivalDate, Integer numAdults, Integer numChildren, Pageable pageable) {
        return repo.findHotelsWithFilters(location, departureDate, arrivalDate, numAdults, numChildren, pageable);
    }

    @Override
    public Hotels getHotelsById(String id) {
        return repo.getHotelsById(id);
    }

}
