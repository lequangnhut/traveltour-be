package com.main.traveltour.repository;

import com.main.traveltour.entity.Invoices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoicesRepository extends JpaRepository<Invoices, Integer> {

    @Query("SELECT MAX(i.id) FROM Invoices i")
    String findMaxCode();

    Invoices findById(String InvoiceId);

    @Query("SELECT i FROM Invoices i " +
            "JOIN i.bookingToursByBookingTourId bt " +
            "WHERE (:searchTerm IS NULL OR " +
            "(UPPER(i.id) LIKE %:searchTerm% OR " +
            "UPPER(i.bookingTourId) LIKE %:searchTerm% OR " +
            "CAST(i.bookingToursByBookingTourId.orderTotal AS STRING) LIKE %:searchTerm% OR " +
            "UPPER(i.bookingToursByBookingTourId.tourDetailId) LIKE %:searchTerm% OR " +
            "UPPER(CONCAT(i.bookingToursByBookingTourId.tourDetailId, ' - ', i.bookingToursByBookingTourId.tourDetailsByTourDetailId.tourId)) LIKE %:searchTerm% OR " +
            "UPPER(i.bookingToursByBookingTourId.tourDetailsByTourDetailId.tourId) LIKE %:searchTerm%))" +
            "AND bt.orderStatus = 1 " +
            "ORDER BY i.dateCreated DESC ")
    Page<Invoices> findAllBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

}