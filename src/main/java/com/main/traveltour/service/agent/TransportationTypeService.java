package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationTypes;

import java.util.List;

public interface TransportationTypeService {

    List<TransportationTypes> findAllTransportType();

    TransportationTypes findByTransportTypeId(int transportTypeId);
}
