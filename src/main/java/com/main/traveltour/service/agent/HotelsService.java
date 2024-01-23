package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Hotels;

import java.util.List;

public interface HotelsService {

    List<Hotels> findAllListHotel();

    List<Hotels> findAllByAgencyId(int agencyId);

    Hotels findByAgencyId(int agencyId);

    String findMaxCode();

    Hotels save(Hotels hotels);
}
