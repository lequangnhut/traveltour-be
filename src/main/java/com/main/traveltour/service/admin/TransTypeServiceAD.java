package com.main.traveltour.service.admin;

import com.main.traveltour.entity.TransportationTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransTypeServiceAD {

    Page<TransportationTypes> findAll(Pageable pageable);

    Page<TransportationTypes> findAllWithSearch(String searchTerm, Pageable pageable);

    TransportationTypes findByTransportationTypeName(String name);

    TransportationTypes findById(int id);

    TransportationTypes save(TransportationTypes transportationTypes);

    TransportationTypes delete(int id);

}
