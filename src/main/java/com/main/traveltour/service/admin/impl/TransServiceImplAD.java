package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.entity.Transportations;
import com.main.traveltour.repository.TransportationBrandsRepository;
import com.main.traveltour.repository.TransportationSchedulesRepository;
import com.main.traveltour.repository.TransportationsRepository;
import com.main.traveltour.service.admin.TransServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransServiceImplAD implements TransServiceAD {

    @Autowired
    private TransportationsRepository transportationsRepository;

    @Autowired
    private TransportationBrandsRepository transportationBrandsRepository;

    @Autowired
    private TransportationSchedulesRepository transportationSchedulesRepository;

    @Override
    public List<Transportations> findbyTransTypeId(int typeId) {
        return transportationsRepository.findAllByTransportationTypeId(typeId);
    }

    @Override
    public Page<TransportationBrands> findAllBrandPost(Boolean isAccepted, Pageable pageable) {
        return transportationBrandsRepository.findAllBrandPost(isAccepted, pageable);
    }

    @Override
    public Page<TransportationBrands> findAllBrandPostByName(Boolean isAccepted, Pageable pageable, String searchTerm) {
        return transportationBrandsRepository.findAllBrandPostByName(isAccepted, pageable, searchTerm);
    }

    @Override
    public Page<Transportations> findAllTransPost(Boolean isActive, String brandId, Pageable pageable) {
        return transportationsRepository.findAllPostByBrand(isActive, brandId, pageable);
    }

    @Override
    public Page<Transportations> findAllTransPostByName(Boolean isActive, String brandId, Pageable pageable, String searchTerm) {
        return transportationsRepository.findAllPostByBrandAndName(isActive, brandId, pageable, searchTerm);
    }

    @Override
    public Page<TransportationSchedules> findAllSchedulesPost(Boolean isActive, String transId, Pageable pageable) {
        return transportationSchedulesRepository.findScheduleByTransId(isActive, transId, pageable);
    }

    @Override
    public Page<TransportationSchedules> findAllSchedulesPostByName(Boolean isActive, String transId, Pageable pageable, String searchTerm) {
        return transportationSchedulesRepository.findScheduleByTransIdByName(isActive, transId, pageable, searchTerm);
    }

    @Override
    public TransportationBrands findByBrandId(String id) {
        return transportationBrandsRepository.findById(id);
    }

    @Override
    public Transportations findByTransId(String id) {
        return transportationsRepository.findByTransId(id);
    }

    @Override
    public TransportationSchedules findByScheduleId(String id) {
        return transportationSchedulesRepository.findById(id);
    }
}
