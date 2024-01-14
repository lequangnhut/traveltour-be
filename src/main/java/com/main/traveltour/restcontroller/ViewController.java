package com.main.traveltour.restcontroller;

import com.main.traveltour.dto.DemoDTO;
import com.main.traveltour.service.cloudinary.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class ViewController {

    @Autowired
    private FileUpload fileUpload;

    @PostMapping("demo/demo-upload-file")
    public DemoDTO demo(@RequestPart("demoDTO") DemoDTO demoDTO,
                        @RequestPart("business_license") MultipartFile businessLicense,
                        @RequestPart("business_images") MultipartFile businessImages) throws IOException {
        String license = fileUpload.uploadFile(businessLicense);
        String images = fileUpload.uploadFile(businessImages);
        System.out.println(license);
        System.out.println(images);
        return demoDTO;
    }
}
