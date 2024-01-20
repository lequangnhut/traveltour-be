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
@Table(name = "visit_location_types", schema = "travel_tour")
public class VisitLocationTypes {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "visit_location_type_name")
    private String visitLocationTypeName;

    @OneToMany(mappedBy = "visitLocationTypesByVisitLocationTypeId")
    @JsonManagedReference
    private Collection<VisitLocations> visitLocationsById;
}
