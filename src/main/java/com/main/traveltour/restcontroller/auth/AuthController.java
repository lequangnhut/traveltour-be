package com.main.traveltour.restcontroller.auth;

import com.main.traveltour.entity.Users;
import com.main.traveltour.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1")
public class AuthController {

    @Autowired
    private UsersService usersService;

    @GetMapping("auth/verify-account/{token}")
    private String loginWithGoogle(@PathVariable String token) {
        Users users = usersService.findByToken(token);

        if (users != null) {
            users.setIsActive(Boolean.TRUE);
            usersService.save(users);
        }
        return "redirect:http://localhost:3000/verify-success/" + token;
    }
}
