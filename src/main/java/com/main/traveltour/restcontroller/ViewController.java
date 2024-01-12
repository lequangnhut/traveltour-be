package com.main.traveltour.restcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @GetMapping("/login-google-success")
    public String loginGoogleSuccess() {
        System.out.println("cc");
        return "redirect:/http://localhost:3000/home";
    }
}
