package com.main.traveltour.service.agent;

import com.main.traveltour.entity.Hotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface HotelsService {

    List<Hotels> findAllListHotel();

    List<Hotels> findAllByAgencyId(int agencyId);

    Hotels findByAgencyId(int agencyId);

    String findMaxCode();

    Hotels save(Hotels hotels);

    Optional<Hotels> findById(String id);

    void delete(Hotels hotels);

    List<Hotels> getAllHotels();

}
