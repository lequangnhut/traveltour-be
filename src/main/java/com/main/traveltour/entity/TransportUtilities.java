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
@Table(name = "transport_utilities", schema = "travel_tour")
public class TransportUtilities {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "icon")
    private String icon;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "transportUtilities")
    private List<Transportations> users;
}
