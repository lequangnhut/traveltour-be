package com.main.traveltour.service.utils.impl;

import com.main.traveltour.dto.auth.RegisterDto;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.utils.ThymeleafService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@EnableScheduling
public class EmailServiceImpl implements EmailService {

    private final Queue<RegisterDto> emailQueueRegister = new LinkedList<>();

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private ThymeleafService thymeleafService;

    @Autowired
    private UsersService userService;

    @Value("${spring.mail.username}")
    private String email;

    @Override
    public void queueEmailRegister(RegisterDto registerDto) {
        emailQueueRegister.add(registerDto);
    }

    @Override
    public void sendMailRegister() {
        while (!emailQueueRegister.isEmpty()) {
            RegisterDto registerDto = emailQueueRegister.poll();
            Users users = userService.findByEmail(registerDto.getEmail());

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(registerDto.getEmail());

                Map<String, Object> variables = new HashMap<>();
                variables.put("email", registerDto.getEmail());
                variables.put("full_name", registerDto.getFullName());
                variables.put("token", users.getToken());

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("verify-email", variables), true);
                helper.setSubject("TRAVEL TOUR CẢM ƠN QUÝ KHÁCH HÀNG ĐÃ ĐĂNG KÝ TÀI KHOẢN");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void processRegister() {
        sendMailRegister();
    }
}