package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
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

    @OneToMany(mappedBy = "usersByUserId")
    @JsonManagedReference
    private Collection<Agencies> agenciesById;

    @OneToMany(mappedBy = "usersByUserId")
    @JsonManagedReference
    private Collection<BookingTours> bookingToursById;

    @OneToMany(mappedBy = "usersByUserId")
    @JsonManagedReference
    private Collection<OrderHotels> orderHotelsById;

    @OneToMany(mappedBy = "usersByUserId")
    @JsonManagedReference
    private Collection<OrderTransportations> orderTransportationsById;

    @OneToMany(mappedBy = "usersByUserId")
    @JsonManagedReference
    private Collection<OrderVisits> orderVisitsById;

    @OneToMany(mappedBy = "usersByGuideId")
    @JsonManagedReference
    private Collection<TourDetails> tourDetailsById;

    @OneToMany(mappedBy = "usersByUserId")
    private Collection<Settings> settingsById;

    @OneToMany(mappedBy = "usersByUserId")
    @JsonManagedReference
    private Collection<PassOTP> passOtpById;

    @OneToMany(mappedBy = "usersByUserId")
    @JsonManagedReference
    private Collection<UserLikes> userLikesById;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "roles_users", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @JsonIgnoreProperties("users")
    private List<Roles> roles = new ArrayList<>();
}
