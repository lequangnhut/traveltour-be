package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tours", schema = "travel_tour")
public class Tours {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Basic
    @Column(name = "tour_type_id")
    private Integer tourTypeId;

    @Basic
    @Column(name = "tour_name")
    private String tourName;

    @Basic
    @Column(name = "tour_img")
        private String tourImg;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "date_deleted")
    private Timestamp dateDeleted;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "tour_description")
    private String tourDescription;

    @OneToMany(mappedBy = "toursByTourId")
    @JsonManagedReference
    private Collection<TourDetails> tourDetailsById;

    @ManyToOne
    @JoinColumn(name = "tour_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private TourTypes tourTypesByTourTypeId;
}
