package com.main.traveltour.service.agent;

import com.main.traveltour.entity.VisitLocations;

public interface VisitLocationsService {

    VisitLocations findByUserId(int userId);

    VisitLocations save(VisitLocations visitLocations);
}
