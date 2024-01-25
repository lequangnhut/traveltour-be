package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationBrands;

import java.util.List;

public interface TransportationBrandsService {

    String findMaxCode();

    List<TransportationBrands> findAllByAgencyId(int agencyId);

    TransportationBrands findByAgencyId(int agencyId);

    TransportationBrands findByTransportBrandId(String transportBrandId);

    TransportationBrands save(TransportationBrands transportationBrands);
}
