package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agencies", schema = "travel_tour")
public class Agencies {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "name_agency")
    private String nameAgency;

    @Basic
    @Column(name = "representative_name")
    private String representativeName;

    @Basic
    @Column(name = "tax_id")
    private String taxId;

    @Basic
    @Column(name = "url_website")
    private String urlWebsite;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "province")
    private String province;

    @Basic
    @Column(name = "district")
    private String district;

    @Basic
    @Column(name = "ward")
    private String ward;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "user_id")
    private int userId;

    @Basic
    @Column(name = "is_accepted")
    private Integer isAccepted;

    @Basic
    @Column(name = "img_document")
    private String imgDocument;
}
