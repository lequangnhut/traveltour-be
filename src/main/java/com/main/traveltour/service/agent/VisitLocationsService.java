package com.main.traveltour.service.agent;

import com.main.traveltour.entity.VisitLocations;

public interface VisitLocationsService {

    String findMaxCode();

    VisitLocations findByAgencyId(int userId);

    VisitLocations save(VisitLocations visitLocations);
}
