package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotel_types", schema = "travel_tour")
public class HotelTypes {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "hotel_type_name")
    private String hotelTypeName;

    @OneToMany(mappedBy = "hotelTypesByHotelTypeId")
    @JsonManagedReference
    private Collection<Hotels> hotelsById;
}
