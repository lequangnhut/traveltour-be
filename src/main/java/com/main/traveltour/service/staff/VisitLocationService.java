package com.main.traveltour.service.staff;

import com.main.traveltour.entity.VisitLocations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitLocationService {

    VisitLocations findByIdAndIsActiveIsTrue(String id);

    Page<VisitLocations> getAllByIsActiveIsTrueAndIsAcceptedIsTrue(Pageable pageable);

    Page<VisitLocations> findBySearchTerm(String searchTerm, Pageable pageable);

    Page<VisitLocations> findVisitLocationsByProvince(String location, Pageable pageable);
}
