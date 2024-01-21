package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationBrands;

public interface TransportationBrandsService {

    String findMaxCode();

    TransportationBrands findByAgencyId(int userId);

    TransportationBrands save(TransportationBrands transportationBrands);
}
