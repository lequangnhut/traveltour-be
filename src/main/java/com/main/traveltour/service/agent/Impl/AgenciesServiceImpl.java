package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.repository.AgenciesRepository;
import com.main.traveltour.service.agent.AgenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AgenciesServiceImpl implements AgenciesService {

    @Autowired
    private AgenciesRepository agenciesRepository;

    @Override
    public Agencies findByUserId(int userId) {
        return agenciesRepository.findByUserId(userId);
    }

    @Override
    public Agencies findByAgencyId(int agencyId) {
        return agenciesRepository.findById(agencyId);
    }

    @Override
    public Agencies findByPhone(String phone) {
        return agenciesRepository.findByPhone(phone);
    }

    @Override
    public Agencies findByTaxId(String taxId) {
        return agenciesRepository.findByTaxId(taxId);
    }

    @Override
    public Agencies save(Agencies agencies) {
        return agenciesRepository.save(agencies);
    }

    @Override
    public Optional<Agencies> findById(Integer id) {
        return agenciesRepository.findById(id);
    }
}
