package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.service.admin.HotelsServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelsServiceImplAD implements HotelsServiceAD {

    @Autowired
    private HotelsRepository hotelsRepository;

    @Override
    public List<Hotels> findByHotelTypeId(int typeId) {
        return hotelsRepository.findByHotelTypeId(typeId);
    }

    @Override
    public List<Hotels> findByUtility(int typeId) {
        return hotelsRepository.findAllByPlaceUtilities(typeId);
    }

    @Override
    public Page<Hotels> findAllHotelPost(Boolean isAccepted, Pageable pageable) {
        return hotelsRepository.findAllHotelByAcceptedAndTrueActive(isAccepted, pageable);
    }

    @Override
    public Page<Hotels> findAllHotelPostByName(Boolean isAccepted, Pageable pageable, String searchterm) {
        return hotelsRepository.findAllHotelByAcceptedAndTrueActiveByName(isAccepted, pageable, searchterm);
    }

    @Override
    public Hotels findById(String id) {
        return hotelsRepository.getHotelsById(id);
    }

    @Override
    public Hotels save(Hotels hotels) {
        return hotelsRepository.save(hotels);
    }

    @Override
    public Long countHotelsChart(Integer year) {
        return hotelsRepository.countHotelsChart(year);
    }

    @Override
    public List<Hotels> findThreeHotelMostOrder() {
        return hotelsRepository.find3HotelMostOrder();
    }

}