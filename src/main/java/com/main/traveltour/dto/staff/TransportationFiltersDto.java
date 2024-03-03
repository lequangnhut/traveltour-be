package com.main.traveltour.dto.staff;

import lombok.Data;

import java.util.List;

@Data
public class TransportationFiltersDto {
    Integer price;
    List<Integer> mediaTypeList;
    List<String> listOfVehicleManufacturers;
}
