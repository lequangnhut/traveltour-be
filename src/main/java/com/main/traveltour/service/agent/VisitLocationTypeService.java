package com.main.traveltour.service.agent;

import com.main.traveltour.entity.VisitLocationTypes;

import java.util.List;

public interface VisitLocationTypeService {

    List<VisitLocationTypes> findAllForRegisterAgency();
}
