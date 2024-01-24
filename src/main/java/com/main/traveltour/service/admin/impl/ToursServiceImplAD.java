package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.Tours;
import com.main.traveltour.repository.ToursRepository;
import com.main.traveltour.service.admin.ToursServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToursServiceImplAD implements ToursServiceAD {

    @Autowired
    ToursRepository toursRepository;

    @Override
    public List<Tours> findByTourTypeId(int typeId) {
        return toursRepository.findAllByTourTypeId(typeId);
    }
}
