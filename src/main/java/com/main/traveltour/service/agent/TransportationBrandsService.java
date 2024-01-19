package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationBrands;

public interface TransportationBrandsService {

    TransportationBrands findByUserId(int userId);

    TransportationBrands save(TransportationBrands transportationBrands);
}
