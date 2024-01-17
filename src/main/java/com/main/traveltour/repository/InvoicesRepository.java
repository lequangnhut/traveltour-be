package com.main.traveltour.repository;

import com.main.traveltour.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoicesRepository extends JpaRepository<Invoices, Integer> {
}