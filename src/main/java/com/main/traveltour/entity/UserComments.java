package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_comments", schema = "travel_tour")
public class UserComments {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "service_id", nullable = false, length = 30) // mã dịch vụ | vd: mã tour, mã hotel, mã tquan
    private String serviceId;

    @Basic
    @Column(name = "category") // 0: tour, 1: hotel, 2: vehicle, 3: visit
    private Integer category;

    @Basic
    @Column(name = "star")
    private Integer star; // 1 sao 2 sao..... 5 sao

    @Basic
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "user_id")
    private int usersId;

    @Basic
    @Column(name = "order_id")
    private String orderId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Users usersByUserId;
}
