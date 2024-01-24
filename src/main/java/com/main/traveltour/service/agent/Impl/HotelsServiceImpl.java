package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.service.agent.HotelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelsServiceImpl implements HotelsService {

    @Autowired
    private HotelsRepository hotelsRepository;

    @Override
    public List<Hotels> findAllListHotel() {
        return hotelsRepository.findAll();
    }

    @Override
    public List<Hotels> findAllByAgencyId(int agencyId) {
        return hotelsRepository.findAllByAgenciesId(agencyId);
    }

    @Override
    public Hotels findByAgencyId(int agencyId) {
        return hotelsRepository.findByAgenciesId(agencyId);
    }

    @Override
    public String findMaxCode() {
        return hotelsRepository.findMaxCode();
    }

    @Override
    public Hotels save(Hotels hotels) {
        return hotelsRepository.save(hotels);
    }

}
