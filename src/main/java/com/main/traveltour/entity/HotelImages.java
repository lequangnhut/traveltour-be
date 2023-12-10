package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotel_images", schema = "travel_tour")
public class HotelImages {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "hotel_id")
    private int hotelId;

    @Basic
    @Column(name = "hotel_img")
    private String hotelImg;
}
