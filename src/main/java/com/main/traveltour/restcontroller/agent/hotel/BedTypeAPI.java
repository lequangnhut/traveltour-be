package com.main.traveltour.restcontroller.agent.hotel;

import com.main.traveltour.entity.BedTypes;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.agent.BedTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class BedTypeAPI {

    @Autowired
    private BedTypeService bedTypeService;

    @GetMapping("agent/bed-type/getAllBedTypes")
    public ResponseObject getAllBedTypes() {
        List<BedTypes> bedTypesList = bedTypeService.findAllListBedTypes();

        if(bedTypesList.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }else{
            return new ResponseObject("200", "OK", bedTypesList);
        }
    }
}
