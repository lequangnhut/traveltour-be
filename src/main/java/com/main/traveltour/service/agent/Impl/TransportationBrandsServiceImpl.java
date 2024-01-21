package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.repository.TransportationBrandsRepository;
import com.main.traveltour.service.agent.TransportationBrandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransportationBrandsServiceImpl implements TransportationBrandsService {

    @Autowired
    private TransportationBrandsRepository transportationBrandsRepository;

    @Override
    public String findMaxCode() {
        return transportationBrandsRepository.findMaxCode();
    }

    @Override
    public TransportationBrands findByAgencyId(int userId) {
        return transportationBrandsRepository.findByAgenciesId(userId);
    }

    @Override
    public TransportationBrands save(TransportationBrands transportationBrands) {
        return transportationBrandsRepository.save(transportationBrands);
    }
}
