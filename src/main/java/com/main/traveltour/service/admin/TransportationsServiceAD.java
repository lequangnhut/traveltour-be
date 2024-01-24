package com.main.traveltour.service.admin;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.Transportations;

import java.util.List;

public interface TransportationsServiceAD {

    List<Transportations> findByTransportationTypeId(int id);

}
