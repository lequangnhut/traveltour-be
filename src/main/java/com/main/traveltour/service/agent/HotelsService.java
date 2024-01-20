package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Hotels;

public interface HotelsService {

    Hotels findByAgencyId(int userId);

    Hotels save(Hotels hotels);
}
