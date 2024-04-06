package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_likes", schema = "travel_tour")
public class UserLikes {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "service_id", nullable = false, length = 30) // mã dịch vụ | vd: mã tour, mã hotel, mã tquan
    private String serviceId;

    @Basic
    @Column(name = "category") //0: tour, 1: hotel, 2: vehicle, 3: visit
    private Integer category;

    @Basic
    @Column(name = "user_id")
    private int usersId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Users usersByUserId;
}
