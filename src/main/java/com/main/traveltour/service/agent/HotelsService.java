package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Hotels;

public interface HotelsService {

    Hotels findByUserId(int userId);

    Hotels save(Hotels hotels);
}
