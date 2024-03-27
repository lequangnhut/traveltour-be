package com.main.traveltour.utils;

import com.main.traveltour.dto.agent.transport.OrderTransportationsDto;
import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.staff.OrderVisitsDto;

import java.util.List;

public class SessionAttr {

    /**
     * Thanh toán VNPay và Momo cho Tour
     */
    public static BookingDto BOOKING_DTO = null;

    /**
     * Thanh toán VNPay và Momo cho Transport
     */
    public static OrderTransportationsDto ORDER_TRANSPORTATIONS_DTO = null;
    public static List<Integer> SEAT_NUMBER = null;

    /**
     * Thanh toán VNPay location
     */
    public static OrderVisitsDto ORDER_LOCATIONS_DTO = null;
}
