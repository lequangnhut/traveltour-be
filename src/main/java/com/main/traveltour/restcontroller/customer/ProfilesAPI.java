package com.main.traveltour.restcontroller.customer;

import com.main.traveltour.dto.admin.BedTypesDtoAD;
import com.main.traveltour.dto.customer.CustomerInfoDto;
import com.main.traveltour.entity.BedTypes;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.customer.UserServiceCT;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class ProfilesAPI {

    @Autowired
    UserServiceCT userServiceCT;

    @GetMapping("customer/info/find-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        Users users = userServiceCT.findByID(id);
        if (users != null) {
            CustomerInfoDto dto = EntityDtoUtils.convertToDto(users, CustomerInfoDto.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

}
