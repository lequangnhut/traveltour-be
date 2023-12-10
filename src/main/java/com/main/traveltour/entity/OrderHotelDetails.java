package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_hotel_details", schema = "travel_tour")
public class OrderHotelDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "order_hotel_id")
    private Integer orderHotelId;

    @Basic
    @Column(name = "room_type_id")
    private Integer roomTypeId;

    @Basic
    @Column(name = "amount")
    private Integer amount;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
