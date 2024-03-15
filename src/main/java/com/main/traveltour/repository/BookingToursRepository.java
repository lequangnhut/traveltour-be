package com.main.traveltour.repository;

import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.TourDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingToursRepository extends JpaRepository<BookingTours, Integer> {

    BookingTours findById(String bookingTourId);

    @Query("SELECT bt FROM BookingTours bt WHERE bt.orderStatus = :orderStatus")
    Page<BookingTours> findAllBookingTours(@Param("orderStatus") Integer orderStatus, Pageable pageable);

    @Query("SELECT bt FROM BookingTours bt " +
            "WHERE bt.orderStatus = :orderStatus AND " +
            "(UPPER(bt.customerName) LIKE %:searchTerm% OR " +
            "UPPER(bt.customerCitizenCard) LIKE %:searchTerm% OR " +
            "UPPER(bt.customerPhone) LIKE %:searchTerm% OR " +
            "UPPER(bt.customerEmail) LIKE %:searchTerm% OR " +
            "CAST(bt.orderTotal AS string) LIKE %:searchTerm%)")
    Page<BookingTours> findBySearchTerm(@Param("orderStatus") Integer orderStatus, @Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT bt FROM BookingTours bt WHERE bt.orderStatus = :orderStatus AND bt.userId = :userId")
    Page<BookingTours> findAllBookingToursByUserId(@Param("orderStatus") Integer orderStatus, @Param("userId") Integer userId, Pageable pageable);
}