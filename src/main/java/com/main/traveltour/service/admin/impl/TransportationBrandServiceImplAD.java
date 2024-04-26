package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.repository.TransportationBrandsRepository;
import com.main.traveltour.service.admin.TransportationBrandServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportationBrandServiceImplAD implements TransportationBrandServiceAD {
    @Autowired
    private TransportationBrandsRepository repo;

    @Override
    public List<TransportationBrands> findThreeVehicleMostOrder() {
        return repo.find3BrandMostOrder();
    }

    @Override
    public Long countTransportationBrandsChart(Integer year) {
        return repo.countTransportationBrandsChart(year);
    }
}
