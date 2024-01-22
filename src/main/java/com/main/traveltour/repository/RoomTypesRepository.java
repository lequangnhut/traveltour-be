package com.main.traveltour.repository;

import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomTypesRepository extends JpaRepository<RoomTypes, Integer> {

    @Query("SELECT MAX(rt.id) FROM RoomTypes rt")
    String findMaxId();
}