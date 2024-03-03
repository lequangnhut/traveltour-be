package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.repository.TransportationBrandsRepository;
import com.main.traveltour.service.staff.TransportationBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportationBrandServiceImpl implements TransportationBrandService {

    @Autowired
    private TransportationBrandsRepository repo;

    @Override
    public List<TransportationBrands> findAll() {
        return repo.findAllByIsActiveIsTrueAndIsAcceptedIsTrue();
    }
}
