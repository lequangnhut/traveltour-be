package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Hotels;

import java.util.List;

public interface HotelsServiceAD {
    List<Hotels> findByHotelTypeId(int typeId);

    List<Hotels> findByUtility(int typeId);

}
