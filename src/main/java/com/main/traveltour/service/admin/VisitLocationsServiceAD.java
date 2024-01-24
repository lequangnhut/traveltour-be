package com.main.traveltour.service.admin;

import com.main.traveltour.entity.VisitLocations;

import java.util.List;

public interface VisitLocationsServiceAD {

    List<VisitLocations> findByVisitLocationTypeId(int typeId);

}
