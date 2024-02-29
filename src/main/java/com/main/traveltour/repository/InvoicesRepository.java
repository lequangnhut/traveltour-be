package com.main.traveltour.repository;

import com.main.traveltour.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoicesRepository extends JpaRepository<Invoices, Integer> {

    @Query("SELECT MAX(i.id) FROM Invoices i")
    String findMaxCode();
}