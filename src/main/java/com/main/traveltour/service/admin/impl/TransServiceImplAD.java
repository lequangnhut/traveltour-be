package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.Transportations;
import com.main.traveltour.repository.TransportationsRepository;
import com.main.traveltour.service.admin.TransServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransServiceImplAD implements TransServiceAD {

    @Autowired
    private TransportationsRepository transportationsRepository;

    @Override
    public List<Transportations> findbyTransTypeId(int typeId) {
        return transportationsRepository.findAllByTransportationTypeId(typeId);
    }
}
