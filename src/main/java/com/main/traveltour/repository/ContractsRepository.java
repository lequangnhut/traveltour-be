package com.main.traveltour.repository;

import com.main.traveltour.entity.Contracts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractsRepository extends JpaRepository<Contracts, Integer> {

    @Query("SELECT MAX(c.id) FROM Contracts c")
    String findMaxCode();
}