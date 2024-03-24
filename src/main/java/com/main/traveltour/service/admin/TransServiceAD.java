package com.main.traveltour.service.admin;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.entity.Transportations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransServiceAD {

    List<Transportations> findbyTransTypeId(int typeId);

    Page<TransportationBrands> findAllBrandPost(Boolean isAccepted, Pageable pageable);

    Page<TransportationBrands> findAllBrandPostByName(Boolean isAccepted, Pageable pageable, String searchTerm);

    Page<Transportations> findAllTransPost(Boolean isActive, String brandId, Pageable pageable);

    Page<Transportations> findAllTransPostByName(Boolean isActive, String brandId, Pageable pageable, String searchTerm);

    Page<TransportationSchedules> findAllSchedulesPost(Boolean isActive, String transId, Pageable pageable);

    Page<TransportationSchedules> findAllSchedulesPostByName(Boolean isActive, String transId, Pageable pageable, String searchTerm);

    TransportationBrands findByBrandId (String id);

    Transportations findByTransId (String id);

    TransportationSchedules findByScheduleId (String id);

}
