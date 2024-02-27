package com.main.traveltour.restcontroller.customer;

import com.main.traveltour.dto.customer.CustomerInfoDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class ProfilesAPI {

    @Autowired
    UsersService usersService;

    private static final Logger logger = LoggerFactory.getLogger(ProfilesAPI.class);

    @Autowired
    private FileUpload fileUpload;


    @GetMapping("customer/info/find-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        Users users = usersService.findById(id);

        if (users != null) {
            CustomerInfoDto dto = EntityDtoUtils.convertToDto(users, CustomerInfoDto.class);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", dto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

//    update profile
    @PutMapping("customer/info/update-info/{id}")
    public ResponseEntity<Users> updateTourById(
            @RequestPart("usersDto") CustomerInfoDto usersDto,
            @RequestParam("avatarUpdate") MultipartFile avatar) throws IOException {

        // Tìm user
        Users users = usersService.findById(usersDto.getId());

        String imagesPath = null;
        if (avatar != null && !avatar.isEmpty()) {
            imagesPath = fileUpload.uploadFile(avatar);
            users.setAvatar(imagesPath);
            users.setFullName(usersDto.getFullName());
            users.setAddress(usersDto.getAddress());
            users.setBirth(usersDto.getBirth());
            users.setGender(usersDto.getGender());
            users.setPhone(usersDto.getPhone());
            usersService.save(users);
            return ResponseEntity.ok().build();
        }else{
            users.setFullName(usersDto.getFullName());
            users.setAddress(usersDto.getAddress());
            users.setBirth(usersDto.getBirth());
            users.setGender(usersDto.getGender());
            users.setPhone(usersDto.getPhone());
            usersService.save(users);
            return ResponseEntity.ok().build();
        }

    }
}
