package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.*;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.service.agent.HotelsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Hotels> findById(String id) {
        return hotelsRepository.findById(id);
    }

    @Override
    public void delete(Hotels hotels) {
        hotelsRepository.save(hotels);
    }

    @Override
    public List<Hotels> getAllHotels() {
        return hotelsRepository.findAll();
    }

}
