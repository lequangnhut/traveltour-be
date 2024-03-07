package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationBrands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransportationBrandsService {

    String findMaxCode();

    List<TransportationBrands> findAllByAgencyId(int agencyId);

    Page<TransportationBrands> findAllCus(Pageable pageable);

    TransportationBrands findByAgencyId(int agencyId);

    TransportationBrands findByTransportBrandId(String transportBrandId);

    TransportationBrands save(TransportationBrands transportationBrands);
}
