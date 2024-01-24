package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Transportations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransportationService {

    String findMaxCode();

    Transportations findTransportById(String transportId);

    Page<Transportations> findAllTransports(Pageable pageable);

    Page<Transportations> findAllTransportWithSearch(String searchTerm, Pageable pageable);

    Transportations save(Transportations transportations);
}
