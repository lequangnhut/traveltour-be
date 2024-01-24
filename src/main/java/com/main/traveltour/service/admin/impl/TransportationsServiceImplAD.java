package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.Transportations;
import com.main.traveltour.repository.TransportationsRepository;
import com.main.traveltour.service.admin.TransportationsServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportationsServiceImplAD implements TransportationsServiceAD {

    @Autowired
    TransportationsRepository transportationsRepository;

    @Override
    public List<Transportations> findByTransportationTypeId(int id) {
        return transportationsRepository.findAllByTransportationTypeId(id);
    }
}
