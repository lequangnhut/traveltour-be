//package com.main.traveltour.service.staff.impl;
//
//import com.main.traveltour.repository.BookingTourHotelsRepository;
//import com.main.traveltour.service.staff.BookingTourHotelService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//@Service
//public class BookingTourHotelServiceImpl implements BookingTourHotelService {
//
//    @Autowired
//    private BookingTourHotelsRepository repo;
//
//    @Override
//    public Page<BookingTourHotels> findByBookingTourId(String bookingTourId, String searchTerm, Pageable pageable) {
//        return repo.findByBookingTourId(bookingTourId, searchTerm, pageable);
//    }
//}
