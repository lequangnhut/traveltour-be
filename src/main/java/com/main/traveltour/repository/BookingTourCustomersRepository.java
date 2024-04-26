package com.main.traveltour.repository;

import com.main.traveltour.entity.BookingTourCustomers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingTourCustomersRepository extends JpaRepository<BookingTourCustomers, Integer> {

    @Query("SELECT btc FROM BookingTourCustomers btc " +
            "JOIN btc.bookingToursByBookingTourId bt " +
            "WHERE (bt.tourDetailId = :tourDetailId) AND " +
            "(:searchTerm IS NULL OR " +
            "(CAST(btc.id AS STRING) LIKE %:searchTerm% OR " +
            "UPPER(btc.bookingTourId) LIKE %:searchTerm% OR " +
            "UPPER(btc.customerName) LIKE %:searchTerm% OR " +
            "UPPER(btc.customerPhone) LIKE %:searchTerm%))")
    Page<BookingTourCustomers> findBySearchTermAndTourDetailId
            (@Param("tourDetailId") String tourDetailId,
             @Param("searchTerm") String searchTerm,
             Pageable pageable);

    BookingTourCustomers findByCustomerPhone(String customerPhone);

    @Query("SELECT btc FROM BookingTourCustomers btc " +
            "JOIN btc.bookingToursByBookingTourId bt " +
            "WHERE (bt.tourDetailId = :tourDetailId) " +
            "ORDER BY CASE WHEN LOCATE(' ', btc.customerName) > 0 THEN SUBSTRING(btc.customerName, LENGTH(btc.customerName) - LOCATE(' ', REVERSE(btc.customerName)) + 2) ELSE btc.customerName END ASC")
    List<BookingTourCustomers> findByTourDetailId(@Param("tourDetailId") String tourDetailId);

    @Query("SELECT COUNT(btc) FROM BookingTourCustomers btc")
    Long countBookingTourCustomers();
}