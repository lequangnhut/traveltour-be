package com.main.traveltour.service.staff;

import com.main.traveltour.entity.TourTypes;

import java.util.Optional;

public interface TourTypesService {
    Optional<TourTypes> findById(int id);
}
