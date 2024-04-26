package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Hotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HotelsServiceAD {
    List<Hotels> findByHotelTypeId(int typeId);

    List<Hotels> findByUtility(int typeId);

    Page<Hotels> findAllHotelPost(Boolean isAccepted, Pageable pageable);

    Page<Hotels> findAllHotelPostByName(Boolean isAccepted, Pageable pageable, String searchterm);

    Hotels findById(String id);

    Hotels save (Hotels hotels);

    Long countHotelsChart(Integer year);

    List<Hotels> findThreeHotelMostOrder ();

}
