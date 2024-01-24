package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Transportations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransportationService {

    String findMaxCode();

    Transportations findTransportById(String transportId);

    Transportations findTransportByLicensePlate(String licensePlate);

    Page<Transportations> findAllTransports(String brandId, Pageable pageable);

    Page<Transportations> findAllTransportWithSearch(String brandId, String searchTerm, Pageable pageable);

    Transportations save(Transportations transportations);
}
