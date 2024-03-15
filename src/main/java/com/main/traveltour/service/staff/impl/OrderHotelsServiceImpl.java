package com.main.traveltour.service.staff.impl;

import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.dto.customer.hotel.OrderHotelCustomerDto;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.OrderStatus;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.OrderHotelsRepository;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.staff.OrderHotelsService;
import com.main.traveltour.utils.GenerateOrderCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderHotelsServiceImpl implements OrderHotelsService {
    @Autowired
    OrderHotelsRepository repo;

    @Autowired
    RoomTypeService roomTypeService;

    @Override
    public String maxCodeTourId() {
        return repo.maxCodeTourId();
    }

    @Override
    public OrderHotels save(OrderHotels orderHotels) {
        return repo.save(orderHotels);
    }

    @Override
    public void saveOrderHotelCustomer(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel) {
        BigDecimal orderTotal = orderDetailsHotel.stream()
                .map(orderDetails -> {
                    Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(orderDetails.getRoomTypeId());
                    if (roomTypes.isPresent()) {
                        BigDecimal roomPrice = roomTypes.get().getPrice();
                        BigDecimal amount = BigDecimal.valueOf(orderDetails.getAmount());
                        return roomPrice.multiply(amount);
                    } else {
                        return BigDecimal.ZERO;
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderHotels.setOrderTotal(orderTotal);
        orderHotels.setId(GenerateOrderCode.generateCodePayment(orderHotels.getPaymentMethod()));
        orderHotels.setOrderCode(orderHotels.getId());
        orderHotels.setOrderStatus(OrderStatus.PENDING.getValue());
        orderHotels.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        repo.save(orderHotels);
    }

    @Override
    public Page<OrderHotels> getAllByUserId(Integer orderStatus, Integer userId, Pageable pageable) {
        return repo.findAllBookingHotelsByUserId(orderStatus, userId, pageable);
    }


}
