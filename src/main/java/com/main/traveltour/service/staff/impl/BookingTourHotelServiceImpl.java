package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.repository.OrderHotelDetailsRepository;
import com.main.traveltour.repository.OrderHotelsRepository;
import com.main.traveltour.service.staff.BookingTourHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingTourHotelServiceImpl implements BookingTourHotelService {

    @Autowired
    private HotelsRepository hotelsRepository;

    @Autowired
    private OrderHotelDetailsRepository orderHotelDetailsRepository;

    @Autowired
    private OrderHotelsRepository orderHotelsRepository;

    @Override
    public Page<Hotels> findHotelByTourDetailId(String tourDetailId, Integer orderHotelStatus, String searchTerm, Pageable pageable) {
        return hotelsRepository.findHotelByTourDetailId(tourDetailId, orderHotelStatus, searchTerm, pageable);
    }

    @Override
    public List<OrderHotelDetails> findOrderHotelDetailByTourDetailIdAndHotelId(String tourDetailId, String hotelId) {
        return orderHotelDetailsRepository.findOrderHotelDetailByTourDetailIdAndHotelId(tourDetailId, hotelId);
    }

    @Override
    public List<OrderHotels> findOrderHotelByTourDetailIdAndHotelId(String tourDetailId, String hotelId) {
        return orderHotelsRepository.findOrderHotelByTourDetailIdAndHotelId(tourDetailId, hotelId);
    }

    @Override
    public void update(OrderHotels orderHotels) {
        orderHotelsRepository.save(orderHotels);
    }

    @Override
    public Page<Hotels> findHotelByUserId(Integer userId, Integer orderHotelStatus, Pageable pageable) {
        return hotelsRepository.findHotelByUserId(userId, orderHotelStatus, pageable);
    }

    @Override
    public Page<Hotels> findHotelByTourDetailIdForGuide(String tourDetailId, Pageable pageable, String searchTerm) {
        return hotelsRepository.findHotelByTourDetailIdForGuide(tourDetailId, pageable, searchTerm);
    }
}
