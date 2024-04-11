package com.main.traveltour.restcontroller.customer.infomation;

import com.github.cage.YCage;
import com.main.traveltour.dto.customer.infomation.ChangePassDto;
import com.main.traveltour.dto.customer.infomation.ForgotPasswordDto;
import com.main.traveltour.entity.PassOTP;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.customer.PassOTPService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.RandomUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class ForgotPasswordAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PassOTPService passOTPService;

    @Autowired
    private PasswordEncoder encoder;

    private String sessionCaptcha = null;

    @GetMapping("/customer/forgot-pass/captcha")
    public ResponseEntity<byte[]> generateCaptcha() {
        YCage gCage = new YCage();
        String checkCap = gCage.getTokenGenerator().next();
        sessionCaptcha = checkCap;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setExpires(0);
        headers.setPragma("no-cache");

        byte[] captchaImage = gCage.draw(checkCap);

        return ResponseEntity.ok().headers(headers).contentType(MediaType.IMAGE_JPEG).body(captchaImage);
    }

    @GetMapping("/customer/forgot-pass/captcha/check-captcha/{capCode}")
    private Map<String, Boolean> checkDuplicateCaptcha(@PathVariable String capCode) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = capCode.equals(sessionCaptcha);
        response.put("exists", exists);
        return response;
    }

    @GetMapping("/customer/forgot-pass/captcha/check-email/{email}")
    private Map<String, Boolean> checkDuplicateEmail(@PathVariable String email) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = usersService.checkMailForgot(email) != null;
        response.put("exists", exists);
        return response;
    }

    @PostMapping("/customer/forgot-pass/captcha/{email}")
    public void submitEmail(@PathVariable String email, @Valid ForgotPasswordDto passwordsDto) {
        String verifyCode = RandomUtils.RandomToken(15);
        Users users = usersService.findByEmail(email);

        passOTPService.deactivateOldPassOTP(users.getId());

        PassOTP passOTP = new PassOTP();
        passOTP.setUsersId(users.getId());
        passOTP.setCodeToken(verifyCode);
        passOTP.setIsActive(true);
        passOTP.setEmail(users.getEmail());
        passOTP.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        passOTPService.save(passOTP);

        passwordsDto.setFull_name(users.getFullName());
        passwordsDto.setVerifyCode(verifyCode);
        passwordsDto.setEmail(users.getEmail());

        emailService.queueEmailForgot(passwordsDto);
    }

    @PutMapping("/customer/forgot-pass/captcha/new-password/{token}")
    public ResponseObject changePasswordAPI(@PathVariable String token, @RequestBody ChangePassDto changePassDto) {
        try {
            PassOTP passOTP = passOTPService.findByToken(token);
            Users users = usersService.findById(passOTP.getUsersId());
            //get link hình
            String imagePath = users.getAvatar();
            //Lưu mật khẩu mới vào db
            String passwordEncore = encoder.encode(changePassDto.getNewPass());
            users.setPassword(passwordEncore);
            users.setAvatar(imagePath);
            Users updatePassUser = usersService.save(users);
            //Thành công thì loại luôn token đó
            passOTP.setIsActive(false);
            passOTPService.save(passOTP);

            return new ResponseObject("200", "Cập nhật thành công", updatePassUser);
        } catch (Exception e) {
            return new ResponseObject("404", "Cập nhật thất bại", null);
        }
    }

    @GetMapping("/account/forgot-pass/{token}")
    public ResponseObject oldCode(@PathVariable String token) {
        try {
            PassOTP passOTP = passOTPService.findByToken(token);
            Date dateCreated = passOTP.getDateCreated();
            Date now = new Date();

            long diffInMillis = now.getTime() - dateCreated.getTime();
            long diffInMinutes = diffInMillis / (60 * 1000);

            if (passOTP.getIsActive() && diffInMinutes < 10) {
                return new ResponseObject("200", "Hợp lệ", passOTP);
            } else {
                passOTP.setIsActive(false);
                passOTPService.save(passOTP);
                return new ResponseObject("404", "Không hợp lệ", null);
            }
        } catch (Exception e) {
            return new ResponseObject("404", "Không hợp lệ", null);
        }
    }

    //ADMIN

    @GetMapping("/admin-account/forgot-pass/captcha")
    public ResponseEntity<byte[]> generateCaptchaAD() {
        YCage gCage = new YCage();
        String checkCap = gCage.getTokenGenerator().next();
        sessionCaptcha = checkCap;

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setExpires(0);
        headers.setPragma("no-cache");

        byte[] captchaImage = gCage.draw(checkCap);

        return ResponseEntity.ok().headers(headers).contentType(MediaType.IMAGE_JPEG).body(captchaImage);
    }

    @GetMapping("/admin-account/forgot-pass/captcha/check-captcha/{capCode}")
    private Map<String, Boolean> checkDuplicateCaptchaAD(@PathVariable String capCode) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = capCode.equals(sessionCaptcha);
        response.put("exists", exists);
        return response;
    }

    @GetMapping("/admin-account/forgot-pass/captcha/check-email/{email}")
    private Map<String, Boolean> checkDuplicateEmailAD(@PathVariable String email) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = usersService.checkMailAdminForgot(email) != null;
        response.put("exists", exists);
        return response;
    }

    @PostMapping("/admin-account/forgot-pass/captcha/{email}")
    public void submitEmailAD(@PathVariable String email, @Valid ForgotPasswordDto passwordsDto) {
        String verifyCode = RandomUtils.RandomToken(15);
        Users users = usersService.findByEmail(email);

        passOTPService.deactivateOldPassOTP(users.getId());

        PassOTP passOTP = new PassOTP();
        passOTP.setUsersId(users.getId());
        passOTP.setCodeToken(verifyCode);
        passOTP.setIsActive(true);
        passOTP.setEmail(users.getEmail());
        passOTP.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        passOTPService.save(passOTP);

        passwordsDto.setFull_name(users.getFullName());
        passwordsDto.setVerifyCode(verifyCode);
        passwordsDto.setEmail(users.getEmail());

        emailService.queueEmailForgotAdmin(passwordsDto);
    }

    @PutMapping("/admin-account/forgot-pass/captcha/new-password/{token}")
    public ResponseObject changePasswordAPIAD(@PathVariable String token, @RequestBody ChangePassDto changePassDto) {
        try {
            PassOTP passOTP = passOTPService.findByToken(token);
            Users users = usersService.findById(passOTP.getUsersId());
            //get link hình
            //String imagePath = users.getAvatar();
            //Lưu mật khẩu mới vào db
            String passwordEncore = encoder.encode(changePassDto.getNewPass());
            users.setPassword(passwordEncore);
            //users.setAvatar(imagePath);
            Users updatePassUser = usersService.save(users);
            //Thành công thì loại luôn token đó
            passOTP.setIsActive(false);
            passOTPService.save(passOTP);

            return new ResponseObject("200", "Cập nhật thành công", updatePassUser);
        } catch (Exception e) {
            return new ResponseObject("404", "Cập nhật thất bại", null);
        }
    }

    @GetMapping("/admin-account/forgot-pass/{token}")
    public ResponseObject oldCodeAD(@PathVariable String token) {
        try {
            PassOTP passOTP = passOTPService.findByToken(token);
            Date dateCreated = passOTP.getDateCreated();
            Date now = new Date();

            long diffInMillis = now.getTime() - dateCreated.getTime();
            long diffInMinutes = diffInMillis / (60 * 1000);

            if (passOTP.getIsActive() && diffInMinutes < 10) {
                return new ResponseObject("200", "Hợp lệ", passOTP);
            } else {
                passOTP.setIsActive(false);
                passOTPService.save(passOTP);
                return new ResponseObject("404", "Không hợp lệ", null);
            }
        } catch (Exception e) {
            return new ResponseObject("404", "Không hợp lệ", null);
        }
    }
}
