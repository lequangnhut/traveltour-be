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
@Table(name = "tour_types", schema = "travel_tour")
public class TourTypes {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "tour_type_name")
    private String tourTypeName;

    @OneToMany(mappedBy = "tourTypesByTourTypeId")
    @JsonManagedReference
    private Collection<Tours> toursById;
}
