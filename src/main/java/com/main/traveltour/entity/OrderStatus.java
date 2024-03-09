package com.main.traveltour.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum OrderStatus {
    PENDING(1), // Đơn hàng chưa được xử lí
    PROCESSING(2), // Đơn hàng đang được xử lí
    CONFIRMED(3), // Đơn hàng đã được xác nhận
    CANCELLED(4); // Đơn hàng đã bị hủy
    private int value;
}
