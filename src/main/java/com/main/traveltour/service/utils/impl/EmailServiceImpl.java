package com.main.traveltour.service.utils.impl;

import com.main.traveltour.dto.agent.hotel.AgenciesDto;
import com.main.traveltour.dto.auth.RegisterDto;
import com.main.traveltour.dto.customer.ForgotPasswordDto;
import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.dto.superadmin.AccountDto;
import com.main.traveltour.dto.superadmin.DataAccountDto;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.Tours;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.staff.ToursService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.service.utils.ThymeleafService;
import com.main.traveltour.utils.ReplaceUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

@Service
@EnableScheduling
public class EmailServiceImpl implements EmailService {

    private final Queue<RegisterDto> emailQueueRegister = new LinkedList<>();
    private final Queue<DataAccountDto> emailQueueCreateBusiness = new LinkedList<>();
    private final Queue<AgenciesDto> emailQueueRegisterAgency = new LinkedList<>();
    private final Queue<AgenciesDto> emailQueueAcceptedAgency = new LinkedList<>();
    private final Queue<AgenciesDto> emailQueueDeniedAgency = new LinkedList<>();
    private final Queue<BookingDto> emailQueueBookingTour = new LinkedList<>();

    private final Queue<ForgotPasswordDto> emailQueueForgot = new LinkedList<>();

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private ThymeleafService thymeleafService;

    @Autowired
    private UsersService userService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private ToursService toursService;

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
                helper.setSubject("TRAVELTOUR CẢM ƠN QUÝ KHÁCH HÀNG ĐÃ ĐĂNG KÝ TÀI KHOẢN");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailCreateBusiness(DataAccountDto dataAccountDto) {
        emailQueueCreateBusiness.add(dataAccountDto);
    }

    @Override
    public void sendMailCreateBusiness() {
        while (!emailQueueCreateBusiness.isEmpty()) {
            DataAccountDto dataAccountDto = emailQueueCreateBusiness.poll();
            AccountDto accountDto = dataAccountDto.getAccountDto();
            String businessRole = convertRolesToVietnamese(dataAccountDto.getRoles());
            Users users = userService.findByEmail(accountDto.getEmail());

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(dataAccountDto.getAccountDto().getEmail());

                Map<String, Object> variables = new HashMap<>();
                variables.put("full_name", users.getFullName());
                variables.put("email", users.getEmail());
                variables.put("password", accountDto.getPassword());
                variables.put("businessRole", businessRole);

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("notification-account-business", variables), true);
                helper.setSubject("TRAVELTOUR CHÂN THÀNH CẢM ƠN QUÝ DOANH NGHIỆP ĐÃ ĐĂNG KÝ HỢP TÁC");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailRegisterAgency(AgenciesDto agenciesDto) {
        emailQueueRegisterAgency.add(agenciesDto);
    }

    @Override
    public void sendMailRegisterAgency() {
        while (!emailQueueRegisterAgency.isEmpty()) {
            AgenciesDto agenciesDto = emailQueueRegisterAgency.poll();
            Users users = userService.findById(agenciesDto.getUserId());

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(users.getEmail());

                Map<String, Object> variables = new HashMap<>();
                variables.put("name_agency", agenciesDto.getNameAgency());

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("agency-success", variables), true);
                helper.setSubject("TRAVELTOUR CHÂN THÀNH CẢM ƠN QUÝ DOANH NGHIỆP ĐÃ ĐĂNG KÝ HỢP TÁC");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailAcceptedAgency(AgenciesDto agenciesDto) {
        emailQueueAcceptedAgency.add(agenciesDto);
    }

    @Override
    public void sendMailAcceptedAgency() {
        while (!emailQueueAcceptedAgency.isEmpty()) {
            AgenciesDto agenciesDto = emailQueueAcceptedAgency.poll();
            Users users = userService.findById(agenciesDto.getUserId());
            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(users.getEmail());

                Map<String, Object> variables = new HashMap<>();
                variables.put("name_agency", agenciesDto.getNameAgency());

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("agency-accepted", variables), true);
                helper.setSubject("TRAVELTOUR XIN THÔNG BÁO HỒ SƠ DOANH NGHIỆP ĐƯỢC THÔNG QUA");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailDeniedAgency(AgenciesDto agenciesDto) {
        emailQueueDeniedAgency.add(agenciesDto);
    }

    @Override
    public void sendMailDeniedAgency() {
        while (!emailQueueDeniedAgency.isEmpty()) {
            AgenciesDto agenciesDto = emailQueueDeniedAgency.poll();
            Users users = userService.findById(agenciesDto.getUserId());
            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(users.getEmail());

                Map<String, Object> variables = new HashMap<>();
                variables.put("name_agency", agenciesDto.getNameAgency());

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("agency-failed", variables), true);
                helper.setSubject("TRAVELTOUR XIN THÔNG BÁO HỒ SƠ DOANH NGHIỆP KHÔNG HỢP LỆ");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailBookingTour(BookingDto bookingDto) {
        emailQueueBookingTour.add(bookingDto);
    }

    @Override
    public void sendMailBookingTour() {
        while (!emailQueueBookingTour.isEmpty()) {
            BookingDto bookingDto = emailQueueBookingTour.poll();
            BookingToursDto bookingToursDto = bookingDto.getBookingToursDto();

            TourDetails tourDetails = tourDetailsService.findById(bookingToursDto.getTourDetailId());
            Optional<Tours> tours = toursService.findById(tourDetails.getTourId());

            String customerEmail = "";
            if (bookingToursDto.getUserId() != null) {
                Users users = userService.findById(bookingToursDto.getUserId());
                customerEmail = users.getEmail();
            }

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
                int capacityAdult = bookingToursDto.getCapacityAdult();
                int capacityKid = bookingToursDto.getCapacityKid();
                int unitPrice = unitPriceDecimal.intValue();

                BigDecimal orderTotal = BigDecimal.valueOf((capacityAdult * unitPrice) + (capacityKid * (unitPrice * 0.3)));

                if (bookingToursDto.getUserId() != null) {
                    helper.setTo(customerEmail);
                } else {
                    helper.setTo(bookingToursDto.getCustomerEmail());
                }

                Map<String, Object> variables = new HashMap<>();
                variables.put("bookingId", bookingToursDto.getId());
                variables.put("dateTimeBooking", new Timestamp(System.currentTimeMillis()));
                variables.put("tourType", tours.get().getTourTypesByTourTypeId().getTourTypeName());
                variables.put("customerName", bookingToursDto.getCustomerName());
                variables.put("customerCitizenCard", bookingToursDto.getCustomerCitizenCard());
                variables.put("customerPhone", bookingToursDto.getCustomerPhone());
                if (bookingToursDto.getPaymentMethod() == 1) {
                    variables.put("paymentMethod", "VÍ VNPAY");
                } else if (bookingToursDto.getPaymentMethod() == 2) {
                    variables.put("paymentMethod", "VÍ ZALOPAY");
                } else if (bookingToursDto.getPaymentMethod() == 3) {
                    variables.put("paymentMethod", "VÍ MOMO");
                } else {
                    variables.put("paymentMethod", "Tiền mặt");
                }
                variables.put("tourName", tours.get().getTourName());
                variables.put("totalCapacity", capacityAdult + " Người lớn, " + capacityKid + " Trẻ em, " + bookingToursDto.getCapacityBaby() + " Em bé.");
                variables.put("departureDate", tourDetails.getDepartureDate());
                variables.put("arrivalDate", tourDetails.getArrivalDate());
                variables.put("unitPriceAdult", ReplaceUtils.formatPrice(tourDetails.getUnitPrice()) + " VNĐ");
                variables.put("unitPriceKid", ReplaceUtils.formatPrice(BigDecimal.valueOf(unitPrice * 0.3)) + " VNĐ");
                variables.put("orderTotal", ReplaceUtils.formatPrice(orderTotal) + " VNĐ");

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("order-tour-verify", variables), true);
                helper.setSubject("DỊCH VỤ LỮ HÀNH TRAVELTOUR XIN CHÂN THÀNH CẢM ƠN QUÝ KHÁCH ĐÃ ĐẶT TOUR CỦA CHÚNG TÔI.");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void sendMailForgot() {
        while (!emailQueueForgot.isEmpty()) {
            ForgotPasswordDto passwordsDto = emailQueueForgot.poll();
            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(passwordsDto.getEmail());

                Map<String, Object> variables = new HashMap<>();
                variables.put("full_name", passwordsDto.getFull_name());
                variables.put("email", passwordsDto.getEmail());
                variables.put("verifyCode", passwordsDto.getVerifyCode());

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("send-otp", variables), true);
                helper.setSubject("THAY ĐỔI MẬT KHẨU");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailForgot(ForgotPasswordDto passwordsDto) {
        emailQueueForgot.add(passwordsDto);
    }

    @Scheduled(fixedDelay = 5000)
    public void processRegister() {
        sendMailRegister();
    }

    @Scheduled(fixedDelay = 5000)
    public void processCreateBusiness() {
        sendMailCreateBusiness();
    }

    @Scheduled(fixedDelay = 5000)
    public void processRegisterAgency() {
        sendMailRegisterAgency();
    }

    @Scheduled(fixedDelay = 5000)
    public void processAcceptedAgency() {
        sendMailAcceptedAgency();
    }

    @Scheduled(fixedDelay = 5000)
    public void processDeniedAgency() {
        sendMailDeniedAgency();
    }

    @Scheduled(fixedDelay = 5000)
    public void processBookingTour() {
        sendMailBookingTour();
    }

    private String convertRolesToVietnamese(List<String> roles) {
        StringBuilder roleString = new StringBuilder();

        for (String role : roles) {
            switch (role) {
                case "ROLE_AGENT_HOTEL":
                    roleString.append("Đại lý Khách sạn");
                    break;
                case "ROLE_AGENT_TRANS":
                    roleString.append("Đại lý Vận chuyển");
                    break;
                case "ROLE_AGENT_PLACE":
                    roleString.append("Đại lý Tham quan");
                    break;
            }
            roleString.append(", ");
        }

        if (!roleString.isEmpty()) {
            roleString.deleteCharAt(roleString.length() - 2);
        }

        return roleString.toString();
    }

    @Scheduled(fixedDelay = 5000)
    public void processForgotMail() {
        sendMailForgot();
    }
}