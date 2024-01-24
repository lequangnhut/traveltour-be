package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.repository.TransportationBrandsRepository;
import com.main.traveltour.service.agent.TransportationBrandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportationBrandsServiceImpl implements TransportationBrandsService {

    @Autowired
    private TransportationBrandsRepository transportationBrandsRepository;

    @Override
    public String findMaxCode() {
        return transportationBrandsRepository.findMaxCode();
    }

    @Override
    public List<TransportationBrands> findAllByAgencyId(int agencyId) {
        return transportationBrandsRepository.findAllByAgenciesId(agencyId);
    }

    @Override
    public TransportationBrands findByAgencyId(int agencyId) {
        return transportationBrandsRepository.findByAgenciesId(agencyId);
    }

    @Override
    public TransportationBrands findByTransportBrandId(String transportBrandId) {
        return transportationBrandsRepository.findById(transportBrandId);
    }

    @Override
    public TransportationBrands save(TransportationBrands transportationBrands) {
        return transportationBrandsRepository.save(transportationBrands);
    }
}
