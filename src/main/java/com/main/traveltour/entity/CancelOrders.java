package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cancel_orders", schema = "travel_tour")
public class CancelOrders {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "order_id", nullable = false, length = 30) //mã booking
    private String orderId;

    @Basic
    @Column(name = "categogy") //0: tour, 1: hotel, 2: vehicle, 3: visit
    private Integer categogy;

    @Basic
    @Column(name = "deposit_value") //giá trị cọc 50 = 50% = 0.5
    private Integer depositValue;

    @Basic
    @Column(name = "deposit_price") //quy giá trị cọc thành tiền
    private BigDecimal depositPrice;

    @Basic
    @Column(name = "date_created") //ngày hủy đơn
    private Timestamp dateCreated;
}
