package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder
@Table(name = "room_beds", schema = "travel_tour")
public class RoomBeds {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "room_type_id", nullable = false, length = 30)
    private String roomTypeId;

    @Basic
    @Column(name = "bed_type_id")
    private Integer bedTypeId;

    @ManyToOne
    @JoinColumn(name = "room_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private RoomTypes roomTypesByRoomTypeId;

    @ManyToOne
    @JoinColumn(name = "bed_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private BedTypes bedTypesByBedTypeId;
}
