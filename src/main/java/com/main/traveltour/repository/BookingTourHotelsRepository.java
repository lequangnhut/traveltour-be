//package com.main.traveltour.repository;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface BookingTourHotelsRepository extends JpaRepository<BookingTourHotels, String> {
//
//    @Query("SELECT bth FROM BookingTourHotels bth " +
//            "JOIN bth.bookingToursByBookingTourId bt " +
//            "JOIN bth.orderHotelsByOrderHotelId oh " +
//            "WHERE (:bookingTourId IS NULL OR bth.bookingTourId = :bookingTourId) " +
//            "AND UPPER(bt.tourDetailsByTourDetailId.toursByTourId.tourName) LIKE %:searchTerm% OR " +
//            "UPPER(bt.customerName) LIKE %:searchTerm% OR " +
//            "UPPER(bt.customerPhone) LIKE %:searchTerm% OR " +
//            "UPPER(bt.customerEmail) LIKE %:searchTerm% OR " +
//            "CAST(bt.orderTotal AS string) LIKE %:searchTerm% OR " +
//            "UPPER(oh.usersByUserId.fullName) LIKE %:searchTerm% OR " +
//            "CAST(oh.orderTotal AS string) LIKE %:searchTerm% " +
//            "ORDER BY oh.orderStatus")
//    Page<BookingTourHotels> findByBookingTourId(@Param("bookingTourId") String bookingTourId, @Param("searchTerm") String searchTerm, Pageable pageable);
//
//}