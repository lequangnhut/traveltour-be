package com.main.traveltour.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum OrderStatus {
    PENDING(0), // Đơn hàng chưa thanh toán
    PROCESSING(1), // Đơn hàng đang chờ xác nhận (đã thanh toán)
    CONFIRMED(2), // Đơn hàng đã được xác nhận (Được xác nhận từ phía đối tác)
    SUCCESSFUL(3), // Đơn hàng hoàn thành
    CANCELLED(4), // Đơn hàng đã bị hủy
    CANCELLED_BY_ME(5), // Đơn hàng bị hủy bởi tôi
    BOOKING_BY_HOTEL(6); // Đơn hàng được đặt bởi khách của khách sạn
    private int value;
}
