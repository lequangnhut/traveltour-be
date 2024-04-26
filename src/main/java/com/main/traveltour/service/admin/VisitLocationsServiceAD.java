package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.VisitLocations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisitLocationsServiceAD {

    List<VisitLocations> findByVisitLocationTypeId(int typeId);

    Page<VisitLocations> findAllVisitPost(Boolean isAccepted, Pageable pageable);

    Page<VisitLocations> findAllVisitPostByName(Boolean isAccepted, Pageable pageable, String searchTerm);

    VisitLocations findById(String id);

    VisitLocations save(VisitLocations visitLocations);

    Long countVisitLocationsChart(Integer year);

    List<VisitLocations> findThreeVisitLocation();
}
