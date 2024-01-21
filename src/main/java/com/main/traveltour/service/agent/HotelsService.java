package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Hotels;

import java.util.List;

public interface HotelsService {

    List<Hotels> findAllListHotel();

    String findMaxCode();

    Hotels findByAgencyId(int userId);

    Hotels save(Hotels hotels);
}
