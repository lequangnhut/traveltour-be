package com.main.traveltour.restcontroller.staff;


import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationTypes;
import com.main.traveltour.service.staff.TransportationTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/transportation-type-service/")
public class TransportationTypesAPI {

    @Autowired
    private TransportationTypesService transportationTypesService;

    @GetMapping("find-all-transportation-type")
    public ResponseObject searchTransportationTypes() {
        List<TransportationTypes> transportationTypes = transportationTypesService.getAllTransportationTypes();

        if (transportationTypes.isEmpty()) {
            return new ResponseObject("500", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationTypes);
        }
    }

}
