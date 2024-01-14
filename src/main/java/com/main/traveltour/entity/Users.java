package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "travel_tour")
public class Users {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "avatar")
    private String avatar;

    @Basic
    @Column(name = "full_name")
    private String fullName;

    @Basic
    @Column(name = "birth")
    private Date birth;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "citizen_card")
    private String citizenCard;

    @Basic
    @Column(name = "gender")
    private Integer gender;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "token")
    private String token;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "roles_users", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @JsonIgnoreProperties("users")
    private List<Roles> roles = new ArrayList<>();
}
