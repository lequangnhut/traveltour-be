package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationBrands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransportationBrandsService {

    String findMaxCode();

    Page<TransportationBrands> findAllTransportBrand(Pageable pageable);

    Page<TransportationBrands> findAllTransportBrandWithSearch(String searchTerm, Pageable pageable);

    TransportationBrands findByAgencyId(int userId);

    TransportationBrands save(TransportationBrands transportationBrands);
}
