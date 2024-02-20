package com.main.traveltour.service.staff.staff;

import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.service.staff.RoomTypeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RoomTypeServiceServiceImpl implements RoomTypeServiceService {

    @Autowired
    private RoomTypesRepository repo;

    @Override
    public Page<RoomTypes> findByHotelIdAndIsDeletedIsFalse(String hotelId, Pageable pageable) {
        return repo.findByHotelIdAndIsDeletedIsFalse(hotelId, pageable);
    }

    @Override
    public Page<RoomTypes> findByHotelIdWithUtilitiesAndSearchTerm(String searchTerm, String hotelId, Pageable pageable) {
        return repo.findByHotelIdWithUtilitiesAndSearchTerm(searchTerm, hotelId, pageable);
    }

}
