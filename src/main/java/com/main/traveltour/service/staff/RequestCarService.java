package com.main.traveltour.service.staff;

import com.main.traveltour.entity.RequestCar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RequestCarService {

    Page<RequestCar> findAllRequestCarPage(Pageable pageable);

    Optional<RequestCar> findRequestCarById(Integer requestCarId);

    RequestCar findTransportBrandSubmitted(Integer requestCarId, String transportBrandId);

    RequestCar save(RequestCar requestCar);
}
