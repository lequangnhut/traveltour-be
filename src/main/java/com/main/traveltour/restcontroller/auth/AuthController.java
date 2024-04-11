package com.main.traveltour.restcontroller.auth;

import com.main.traveltour.config.DomainURL;
import com.main.traveltour.dto.UsersDto;
import com.main.traveltour.entity.PassOTP;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.customer.PassOTPService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("api/v1")
public class AuthController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PassOTPService passOTPService;

    private Users userSession = null;

    @GetMapping("auth/verify-account/{token}")
    private String verifyTokenAccount(@PathVariable String token) {
        Users users = usersService.findByToken(token);

        if (users != null) {
            users.setIsActive(Boolean.TRUE);
            usersService.save(users);
        }
        return "redirect:" + DomainURL.FRONTEND_URL + "/verify-success/" + token;
    }

    @GetMapping("auth/login/google")
    private String loginWithGoogle() {
        return "redirect:/oauth2/authorization/google";
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
                return "redirect:" + DomainURL.FRONTEND_URL + "/account/forgot-pass";
            } else {
                return "redirect:" + DomainURL.FRONTEND_URL + "/account/forgot-pass/" + verifyCode;
            }
        }
        return "redirect:" + DomainURL.FRONTEND_URL + "/account/forgot-pass";
    }

    @GetMapping("/auth/login/google/success")
    private String redirectSuccess(Principal principal, OAuth2AuthenticationToken oauth) {
        if (principal != null && oauth != null) {
            OAuth2User oauth2User = oauth.getPrincipal();
            Users users = usersService.findByEmail(oauth2User.getAttribute("email"));

            if (users != null) {
                if (users.getIsActive()) {
                    userSession = users;
                    return "redirect:" + DomainURL.FRONTEND_URL + "/home";
                } else {
                    return "redirect:" + DomainURL.FRONTEND_URL + "/sign-in";
                }
            } else {
                Users user = new Users();
                user.setEmail(oauth2User.getAttribute("email"));
                user.setFullName(oauth2User.getAttribute("name"));
                user.setAvatar(oauth2User.getAttribute("picture"));
                user.setPassword(RandomUtils.RandomToken(10));
                user.setIsActive(Boolean.TRUE);

                usersService.authenticateRegister(user);
                userSession = user;
                return "redirect:" + DomainURL.FRONTEND_URL + "/home";
            }
        }

        return "redirect:" + DomainURL.FRONTEND_URL + "/sign-in";
    }

    @ResponseBody
    @GetMapping("/auth/login-google-get-user")
    private ResponseObject loginGoogleGetUser() {
        Users users = null;

        if (userSession != null) {
            String email = userSession.getEmail();
            users = usersService.findByEmail(email);
        }

        if (users != null) {
            UsersDto userDto = EntityDtoUtils.convertToDto(users, UsersDto.class);
            userSession = null;
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", userDto);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @GetMapping("admin-account/forgot-pass/captcha/check-code-on-time/{verifyCode}&{email}")
    private String checkAdminLinks(@PathVariable String verifyCode, @PathVariable String email) {
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
                return "redirect:" + DomainURL.FRONTEND_URL + "/admin-account/forgot-pass";
            } else {
                return "redirect:" + DomainURL.FRONTEND_URL + "/admin-account/forgot-pass/" + verifyCode;
            }
        }
        return "redirect:" + DomainURL.FRONTEND_URL + "/admin-account/forgot-pass";
    }
}
