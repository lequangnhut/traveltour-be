package com.main.traveltour.service.staff.impl;

import com.main.traveltour.dto.staff.OrderHotelDetailsDto;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.repository.OrderHotelDetailsRepository;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderHotelDetailServiceImpl implements OrderHotelDetailService {
    @Autowired
    OrderHotelDetailsRepository repo;

    @Autowired
    RoomTypeService roomTypeService;

    @Override
    public void save(OrderHotelDetails orderHotelDetails) {
        repo.save(orderHotelDetails);
    }

    @Override
    public void saveOrderHotelDetailsCustomer(OrderHotelDetails orderHotelDetails) {
        BigDecimal price = roomTypeService.findRoomTypeById(orderHotelDetails.getRoomTypeId()).get().getPrice();
        orderHotelDetails.setUnitPrice(price);
        repo.save(orderHotelDetails);
    }

    @Override
    public List<OrderHotelDetails> findByOrderHotelId(String orderHotelsId) {
        return repo.findOrderHotelDetailByOrderHotelId(orderHotelsId);
    }
}
