package com.main.traveltour.service.admin;

import com.main.traveltour.entity.TourTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TourTypesServiceAD {

    Page<TourTypes> findAll(Pageable pageable);

    Page<TourTypes> findAllWithSearch(String searchTerm, Pageable pageable);

    TourTypes findByTourTypeName(String name);

    TourTypes findById(int id);

    TourTypes save(TourTypes tourTypes);

    TourTypes delete(int id);

}
