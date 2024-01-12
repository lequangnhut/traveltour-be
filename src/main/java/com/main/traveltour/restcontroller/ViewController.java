package com.main.traveltour.restcontroller;

import com.main.traveltour.service.cloudinary.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final FileUpload fileUpload;

    @RequestMapping("/home")
    public String loginGoogleSuccess() {
        return "home";
    }

    @PostMapping("/upload_file")
    public String uploadFile(@RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        String imageURL = fileUpload.uploadFile(multipartFile);
        System.out.println(imageURL);
        model.addAttribute("imageURL", imageURL);
        return "gallery";
    }
}
