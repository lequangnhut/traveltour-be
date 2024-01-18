package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.repository.AgenciesRepository;
import com.main.traveltour.service.agent.AgenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgenciesServiceImpl implements AgenciesService {

    @Autowired
    private AgenciesRepository agenciesRepository;

    @Override
    public Agencies findByUserId(int userId) {
        return agenciesRepository.findByUserId(userId);
    }

    @Override
    public Agencies save(Agencies agencies) {
        return agenciesRepository.save(agencies);
    }
}
