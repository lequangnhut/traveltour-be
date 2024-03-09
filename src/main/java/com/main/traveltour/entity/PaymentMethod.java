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
@Table(name = "payment_method", schema = "travel_tour")

public class PaymentMethod {
    @Id
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Basic
    @Column(name = "description")
    private String description;
}
