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
@Table(name = "bed_types", schema = "travel_tour")
public class BedTypes {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "bed_type_name")
    private String bedTypeName;

    @OneToMany(mappedBy = "bedTypesByBedTypeId")
    @JsonManagedReference
    private Collection<RoomBeds> roomBedsById;
}
