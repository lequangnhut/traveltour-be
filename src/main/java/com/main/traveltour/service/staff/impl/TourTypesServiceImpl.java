package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.TourTypes;
import com.main.traveltour.repository.TourTypesRepository;
import com.main.traveltour.service.staff.TourTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourTypesServiceImpl implements TourTypesService {

    @Autowired
    private TourTypesRepository repo;

    @Override
    public Optional<TourTypes> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public List<TourTypes> findAll() {
        return repo.findAll();
    }
}
