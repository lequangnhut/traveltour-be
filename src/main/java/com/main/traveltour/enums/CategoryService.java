package com.main.traveltour.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CategoryService {
    SERVICE_TOUR(0),
    SERVICE_HOTEL(1),
    SERVICE_TRANSPORT(2),
    SERVICE_LOCATION(3);
    private int value;
}
