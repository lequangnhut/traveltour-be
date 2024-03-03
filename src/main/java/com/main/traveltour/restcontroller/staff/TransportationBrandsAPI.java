package com.main.traveltour.restcontroller.staff;


import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.service.staff.TransportationBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/transportation-brand-service/")
public class TransportationBrandsAPI {

    @Autowired
    private TransportationBrandService transportationBrandsService;

    @GetMapping("find-all-transportation-brand")
    public ResponseObject searchTransportationBrand() {
        List<TransportationBrands> transportationBrands = transportationBrandsService.findAll();

        if (transportationBrands.isEmpty()) {
            return new ResponseObject("500", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationBrands);
        }
    }


}
