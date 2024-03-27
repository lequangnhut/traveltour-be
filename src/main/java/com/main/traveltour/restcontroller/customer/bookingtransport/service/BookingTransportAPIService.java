package com.main.traveltour.restcontroller.customer.bookingtransport.service;

import com.main.traveltour.dto.agent.transport.OrderTransportationsDto;
import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.TransportationSchedules;

import java.util.List;

public interface BookingTransportAPIService {

    OrderTransportations createUserPayment(OrderTransportationsDto orderTransportationsDto, List<Integer> seatNumber, Integer orderStatus);

    void createOrderDetailScheduleSeat(TransportationSchedules schedules, String orderTransportId, List<Integer> seatNumber);
}
