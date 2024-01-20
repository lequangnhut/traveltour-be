package com.main.traveltour.service.agent;

import com.main.traveltour.entity.VisitLocations;

public interface VisitLocationsService {

    VisitLocations findByAgencyId(int userId);

    VisitLocations save(VisitLocations visitLocations);
}
