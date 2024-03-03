package com.main.traveltour.repository;

import com.main.traveltour.entity.VisitLocationTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface    VisitLocationTypesRepository extends JpaRepository<VisitLocationTypes, Integer> {
    @Query("SELECT u FROM VisitLocationTypes u ORDER BY u.visitLocationTypeName ASC")
    Page<VisitLocationTypes> findAllByVisitLocationTypeNameOrderByVisitLocationTypeNameVisitLocationTypeNameAsc(Pageable pageable);

    Page<VisitLocationTypes> findByVisitLocationTypeNameContainingIgnoreCase(String searchTerm, Pageable pageable);
    VisitLocationTypes findByVisitLocationTypeName(String name);
    VisitLocationTypes findById(int id);
    void deleteById(int id);

}