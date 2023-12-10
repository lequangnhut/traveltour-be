package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "place_utilities", schema = "travel_tour")
public class PlaceUtilities {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "place_utilities_name")
    private String placeUtilitiesName;

    @ManyToMany(mappedBy = "placeUtilities")
    private List<Hotels> hotels;
}
