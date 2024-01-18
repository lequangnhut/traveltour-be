package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.repository.TransportationBrandsRepository;
import com.main.traveltour.service.agent.TransportationBrandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransportationBrandsServiceImpl implements TransportationBrandsService {

    @Autowired
    TransportationBrandsRepository transportationBrandsRepository;

    @Override
    public TransportationBrands save(TransportationBrands transportationBrands) {
        return transportationBrandsRepository.save(transportationBrands);
    }
}
