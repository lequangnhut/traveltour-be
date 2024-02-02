package com.main.traveltour.repository;

import com.main.traveltour.entity.BedTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BedTypesRepository extends JpaRepository<BedTypes, Integer> {
    @Query("SELECT u FROM BedTypes u ORDER BY u.bedTypeName ASC")
    Page<BedTypes> findAllByBedTypeNameOrderByBedTypeNameASC(Pageable pageable);

    Page<BedTypes> findByBedTypeNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    BedTypes findByBedTypeName(String name);

    BedTypes findById(int id);

    @Query("SELECT bt.bedTypeName FROM BedTypes bt " +
            "INNER JOIN bt.roomBedsById rb " +
            "WHERE rb.roomTypeId = :roomTypeId")
    List<String> findByRoomTypeId(@Param("roomTypeId") String roomTypeId);

    void deleteById(int id);
}