package com.main.traveltour.service.staff;

import com.main.traveltour.entity.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ToursService {

    String maxCodeTourId();

    List<Tours> findAllByIsActiveIsTrue();

    Page<Tours> findAllByIsActiveIsTrue(Pageable pageable);

    // Thêm phương thức tìm kiếm với từ khóa
    Page<Tours> findAllWithSearch(String searchTerm, Pageable pageable);

    Optional<Tours> findById(String tourId);

    String getToursNameByTourId(String tourId);

    Tours save(Tours tours);
    void saveAll(List<Tours> tours);
}
