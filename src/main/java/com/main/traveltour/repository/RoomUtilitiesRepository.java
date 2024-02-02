package com.main.traveltour.repository;

import com.main.traveltour.entity.RoomUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomUtilitiesRepository extends JpaRepository<RoomUtilities, Integer> {

    RoomUtilities findById(int id);

//    @Query("SELECT ru.roomUtilitiesName FROM RoomUtilities ru " +
//            "INNER JOIN ru.roomTypes rt " +
//            "WHERE ru.id = :roomTypeId ")
//    List<String> findRoomUtilitiesNameByRoomTypeId(@Param("roomTypeId") String roomTypeId);

    Page<RoomUtilities> findByRoomUtilitiesNameContainingIgnoreCase(String searchTerm, Pageable pageable);

    RoomUtilities findByRoomUtilitiesName(String name);

    void deleteById(int id);
}