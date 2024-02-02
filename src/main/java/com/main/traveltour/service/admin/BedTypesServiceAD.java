package com.main.traveltour.service.admin;

import com.main.traveltour.entity.BedTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BedTypesServiceAD {

    Page<BedTypes> findAll(Pageable pageable);

    Page<BedTypes> findAllWithSearch(String searchTerm, Pageable pageable);

    BedTypes findByBedTypeName(String name);

    BedTypes findById(int id);

    List<String> findByRoomTypeId(String roomTypeId);

    BedTypes save(BedTypes bedTypes);

    BedTypes delete(int id);
}
