package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_chat", schema = "travel_tour")
public class UserChat {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "full_name")
    private String fullName;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "avatar")
    private String avatar;

    @Basic
    @Column(name = "last_updated")
    private Timestamp lastUpdated;

    @Basic
    @Column(name = "role")
    private String role;
}
