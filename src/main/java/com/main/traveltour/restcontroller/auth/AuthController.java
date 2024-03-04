package com.main.traveltour.restcontroller.auth;

import com.main.traveltour.entity.PassOTP;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.customer.PassOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("api/v1")
public class AuthController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PassOTPService passOTPService;

    @GetMapping("auth/verify-account/{token}")
    private String loginWithGoogle(@PathVariable String token) {
        Users users = usersService.findByToken(token);

        if (users != null) {
            users.setIsActive(Boolean.TRUE);
            usersService.save(users);
        }
        return "redirect:http://localhost:3000/verify-success/" + token;
    }

    @GetMapping("customer/forgot-pass/captcha/check-code-on-time/{verifyCode}&{email}")
    private String checkLinks(@PathVariable String verifyCode, @PathVariable String email) {

        Users users = usersService.findByEmail(email);

        PassOTP passOTP = passOTPService.findByUserIdAndToken(users.getId(), verifyCode);

        if (passOTP != null) {
            Date dateCreated = passOTP.getDateCreated();
            Date now = new Date();

            long diffInMillis = now.getTime() - dateCreated.getTime();
            long diffInMinutes = diffInMillis / (60 * 1000);

            if (diffInMinutes > 10) {
                passOTP.setIsActive(false);
                passOTPService.save(passOTP);
                return "redirect:http://localhost:3000/account/forgot-pass";
            } else {
                return "redirect:http://localhost:3000/account/forgot-pass/" + verifyCode;
            }
        }
        return "redirect:http://localhost:3000/account/forgot-pass";
    }




}
