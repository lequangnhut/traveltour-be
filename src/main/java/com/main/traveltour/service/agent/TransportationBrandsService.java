package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationBrands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransportationBrandsService {

    String findMaxCode();

    List<TransportationBrands> findAllByAgencyId(int agencyId);

    Page<TransportationBrands> findAllCus(Pageable pageable);

    TransportationBrands findByAgencyId(int agencyId);

    TransportationBrands findByTransportBrandId(String transportBrandId);

    Page<TransportationBrands> findAllCustomerWithFilter(
            String searchTerm,
            BigDecimal price,
            String fromLocation,
            String toLocation,
            Date checkInDateFiller,
            List<Integer> mediaTypeList,
            List<String> listOfVehicleManufacturers,
            Pageable pageable);

    List<TransportationBrands> findAllCustomerDataList();

    TransportationBrands save(TransportationBrands transportationBrands);

    Optional<TransportationBrands> findById (String id);
}
