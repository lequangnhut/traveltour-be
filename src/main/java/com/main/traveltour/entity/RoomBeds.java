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
@Table(name = "room_beds", schema = "travel_tour")
public class RoomBeds {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "room_type_id")
    private Integer roomTypeId;

    @Basic
    @Column(name = "bed_type_id")
    private Integer bedTypeId;

    @Basic
    @Column(name = "amount")
    private Integer amount;
}
