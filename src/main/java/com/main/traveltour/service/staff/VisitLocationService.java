package com.main.traveltour.service.staff;

import com.main.traveltour.dto.customer.visit.VisitLocationTrendDTO;
import com.main.traveltour.entity.VisitLocations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface VisitLocationService {

    VisitLocations findByIdAndIsActiveIsTrue(String id);

    Page<VisitLocations> getAllByIsActiveIsTrueAndIsAcceptedIsTrue(Pageable pageable);

    Page<VisitLocations> findBySearchTerm(String searchTerm, Pageable pageable);

    Page<VisitLocations> findVisitLocationsByProvince(String location, Pageable pageable);

    Page<VisitLocations> findByFilters(String searchTerm, BigDecimal price, List<String> tickerTypeList, List<Integer> locationTypeList, Pageable pageable);

    List<VisitLocationTrendDTO> findVisitLocationsTrend();

    List<VisitLocations> getAllVisitLocation();

    Long countVisit();
}
