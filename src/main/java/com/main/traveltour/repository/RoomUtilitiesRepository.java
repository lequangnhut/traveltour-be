package com.main.traveltour.repository;

import com.main.traveltour.entity.RoomUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUtilitiesRepository extends JpaRepository<RoomUtilities, Integer> {

    RoomUtilities findById(int id);

    Page<RoomUtilities> findByRoomUtilitiesNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    RoomUtilities findByRoomUtilitiesName(String name);

    void deleteById(int id);
}