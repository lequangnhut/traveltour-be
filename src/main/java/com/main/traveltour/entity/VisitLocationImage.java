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
@Table(name = "visit_location_image", schema = "travel_tour")
public class VisitLocationImage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "visit_location_id", nullable = false, length = 30)
    private String visitLocationId;

    @Basic
    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "visit_location_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private VisitLocations visitLocationsByVisitLocationId;
}
