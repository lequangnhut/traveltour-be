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
@Table(name = "transportation_types", schema = "travel_tour")
public class TransportationTypes {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "transportation_type_name")
    private String transportationTypeName;

    @OneToMany(mappedBy = "transportationTypesByTransportationTypeId")
    @JsonManagedReference
    private Collection<Transportations> transportationsById;

    @OneToMany(mappedBy = "transportationTypesByTransportationTypeId")
    @JsonManagedReference
    private Collection<TourTrips> tourTripsById;
}
