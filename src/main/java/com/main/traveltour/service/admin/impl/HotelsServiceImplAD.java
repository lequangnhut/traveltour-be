package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.service.admin.HotelsServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelsServiceImplAD implements HotelsServiceAD {

    @Autowired
    private HotelsRepository hotelsRepository;
    @Override
    public List<Hotels> findByHotelTypeId(int typeId) {return hotelsRepository.findByHotelTypeId(typeId);}

    @Override
    public List<Hotels> findByUtility(int typeId) {
        return hotelsRepository.findAllByPlaceUtilities(typeId);
    }
}
