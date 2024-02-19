package com.main.traveltour.restcontroller.auth;

import com.main.traveltour.dto.UsersDto;
import com.main.traveltour.dto.auth.LoginDto;
import com.main.traveltour.dto.auth.RegisterDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Users;
import com.main.traveltour.security.jwt.JwtUtilities;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class AuthAPI {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UsersService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtilities jwtUtilities;

    @Autowired
    HttpSession session;

    /**
     * @return User
     * Register User
     */
    @PostMapping("auth/register")
    private ResponseEntity<?> registerAuth(@Validated @RequestBody RegisterDto registerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Data error !");
        }

        emailService.queueEmailRegister(registerDto);

        Users user = EntityDtoUtils.convertToEntity(registerDto, Users.class);
        user.setIsActive(Boolean.FALSE);

        userService.authenticateRegister(user);
        return ResponseEntity.ok(user);
    }

    /**
     * @return token
     * Login and get token
     */
    @PostMapping("auth/login")
    private ResponseEntity<?> loginAuth(@RequestBody LoginDto loginDto) {
        Map<String, String> response = new HashMap<>();

        try {
            String token = userService.authenticateLogin(loginDto);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            response.put("token", "false");
            return ResponseEntity.ok(response);
        }
    }

    /**
     * @return User
     * Check token and return User
     */
    @GetMapping("auth/request-client")
    private ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        Claims claims = jwtUtilities.extractAllClaims(token);

        String username = claims.getSubject();
        List<String> roles = (List<String>) claims.get("roles");

        response.put("username", username);
        response.put("roles", roles);

        return ResponseEntity.ok(response);
    }

    @GetMapping("auth/find-by-email/{email}")
    private UsersDto findByEmail(@PathVariable String email) {
        Users user = userService.findByEmail(email);
        return EntityDtoUtils.convertToDto(user, UsersDto.class);
    }

    @GetMapping("auth/find-by-token/{token}")
    @ResponseBody
    public ResponseObject getUserByToken(@PathVariable String token) {
        Users users = userService.findByToken(token);

        if (users != null) {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", users);
        } else {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    /**
     * @return check email exists
     * Check email exists in the database
     */
    @GetMapping("auth/check-duplicate-email/{email}")
    private Map<String, Boolean> checkDuplicateEmail(@PathVariable String email) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = userService.findByEmail(email) != null;

        response.put("exists", exists);
        return response;
    }

    /**
     * @return check phone exists
     * Check phone exists in the database
     */
    @GetMapping("auth/check-duplicate-phone/{phone}")
    private Map<String, Boolean> checkDuplicatePhone(@PathVariable String phone) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = userService.findByPhone(phone) != null;

        response.put("exists", exists);
        return response;
    }

    /**
     * @return check phone exists
     * Check phone exists in the database
     */
    @GetMapping("auth/check-duplicate-card/{cardId}")
    private Map<String, Boolean> checkDuplicateCardId(@PathVariable String cardId) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = userService.findByCardId(cardId) != null;

        response.put("exists", exists);
        return response;
    }
}
