package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.TransportationBrands;

import java.util.List;

public interface TransportationBrandServiceAD {

    List<TransportationBrands> findThreeVehicleMostOrder ();

    Long countTransportationBrandsChart(Integer year);
}
