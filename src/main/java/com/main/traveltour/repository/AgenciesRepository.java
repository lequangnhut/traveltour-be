package com.main.traveltour.repository;

import com.main.traveltour.entity.Agencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenciesRepository extends JpaRepository<Agencies, Integer> {

    Agencies findByUserId(int userId);
}