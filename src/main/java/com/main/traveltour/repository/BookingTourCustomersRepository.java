package com.main.traveltour.repository;

import com.main.traveltour.entity.BookingTourCustomers;
import com.main.traveltour.entity.BookingTours;
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
            "(UPPER(bt.customerName) LIKE %:searchTerm% OR " +
            "UPPER(bt.customerCitizenCard) LIKE %:searchTerm% OR " +
            "UPPER(bt.customerPhone) LIKE %:searchTerm% OR " +
            "UPPER(bt.customerEmail) LIKE %:searchTerm% OR " +
            "CAST(bt.orderTotal AS string) LIKE %:searchTerm%))")
    Page<BookingTourCustomers> findBySearchTermAndTourDetailId
            (@Param("tourDetailId") String tourDetailId,
             @Param("searchTerm") String searchTerm,
             Pageable pageable);

    BookingTourCustomers findByCustomerPhone(String customerPhone);

    @Query("SELECT btc FROM BookingTourCustomers btc " +
            "JOIN btc.bookingToursByBookingTourId bt " +
            "WHERE (bt.tourDetailId = :tourDetailId)")
    List<BookingTourCustomers> findByTourDetailId(@Param("tourDetailId") String tourDetailId);


}