package com.main.traveltour.service.staff;

import com.main.traveltour.entity.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ToursService {

    Page<Tours> findAll(Pageable pageable);

    // Thêm phương thức tìm kiếm với từ khóa
    Page<Tours> findAllWithSearch(String searchTerm, Pageable pageable);

    Optional<Tours> findById(int tourId);

    Tours save(Tours Tours);
}
