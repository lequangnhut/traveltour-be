package com.main.traveltour.repository;

import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomTypesRepository extends JpaRepository<RoomTypes, Integer> {

    @Query("SELECT MAX(rt.id) FROM RoomTypes rt")
    String findMaxId();

    @Query("SELECT r FROM RoomTypes r " +
            "JOIN r.roomUtilities ru " +
            "WHERE ru.id = :id")
    List<RoomTypes> findAllByRoomUtilities(@Param("id") int id);

}