package com.main.traveltour.repository;

import com.main.traveltour.entity.HotelTypes;
import com.main.traveltour.entity.Hotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelTypesRepository extends JpaRepository<HotelTypes, Integer> {

    /*List loại hotel*/
    @Query("SELECT u FROM HotelTypes u ORDER BY u.hotelTypeName ASC")
    Page<HotelTypes> findAllByHotelTypeNameOrderByHotelTypeNameASC(Pageable pageable);

    Page<HotelTypes> findByHotelTypeNameContainingIgnoreCase(String searchTerm, Pageable pageable);
    HotelTypes findByHotelTypeName(String name);
    HotelTypes findById(int id);
    void deleteById(int id);

}