package com.main.traveltour.service.agent;

import com.main.traveltour.entity.VisitLocations;

import java.util.List;

public interface VisitLocationsService {

    String findMaxCode();

    List<VisitLocations> findAllByAgencyId(int agencyId);

    VisitLocations findByAgencyId(int agencyId);

    VisitLocations findByVisitLocationId(String visitLocationId);

    VisitLocations save(VisitLocations visitLocations);
}
