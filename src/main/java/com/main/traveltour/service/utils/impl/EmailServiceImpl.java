package com.main.traveltour.service.utils.impl;

import com.main.traveltour.dto.agent.hotel.AgenciesDto;
import com.main.traveltour.dto.auth.RegisterDto;
import com.main.traveltour.dto.customer.infomation.ForgotPasswordDto;
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
    private final Queue<BookingToursDto> emailQueueCustomerCancelTour = new LinkedList<>();
    private final Queue<ForgotPasswordDto> emailQueueSendOTPRegisterAgencies = new LinkedList<>();

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

    @Override
    public void sendMailCustomerCancelTour() {
        while (!emailQueueCustomerCancelTour.isEmpty()) {
            BookingToursDto bookingToursDto = emailQueueCustomerCancelTour.poll();
            TourDetails tourDetails = tourDetailsService.findById(bookingToursDto.getTourDetailId());
            Optional<Tours> tours = toursService.findById(tourDetails.getTourId());

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
                int capacityAdult = bookingToursDto.getCapacityAdult();
                int capacityKid = bookingToursDto.getCapacityKid();
                int unitPrice = unitPriceDecimal.intValue();

                BigDecimal orderTotal = BigDecimal.valueOf((capacityAdult * unitPrice) + (capacityKid * (unitPrice * 0.3)));

                if (bookingToursDto.getUserId() != null) {
                    helper.setTo(bookingToursDto.getCustomerEmail());
                } else {
                    helper.setTo(bookingToursDto.getCustomerEmail());
                }

                Map<String, Object> variables = new HashMap<>();
                variables.put("customerEmail", bookingToursDto.getCustomerEmail());
                variables.put("bookingId", bookingToursDto.getId());
                variables.put("dateTimeBooking", bookingToursDto.getDateCreated());
                variables.put("timeDelete", new Timestamp(System.currentTimeMillis()));
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
                    variables.put("paymentMethod", "Thanh toán tại quầy");
                }
                variables.put("tourName", tours.get().getTourName());
                variables.put("totalCapacity", capacityAdult + " Người lớn, " + capacityKid + " Trẻ em, " + bookingToursDto.getCapacityBaby() + " Em bé.");
                variables.put("departureDate", tourDetails.getDepartureDate());
                variables.put("arrivalDate", tourDetails.getArrivalDate());
                variables.put("unitPrice", ReplaceUtils.formatPrice(tourDetails.getUnitPrice()) + " VNĐ");
                variables.put("orderTotal", ReplaceUtils.formatPrice(orderTotal) + " VNĐ");

                Date currentDate = new Date();
                long daysRemaining = (tourDetails.getDepartureDate().getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24);
                int coc;
                BigDecimal moneyBack;

                if (bookingToursDto.getPaymentMethod() == 0 && bookingToursDto.getOrderStatus() == 0) {
                    coc = 0;
                    moneyBack = BigDecimal.ZERO;
                } else {
                    if (daysRemaining >= 30) {
                        coc = 1;
                    } else if (daysRemaining >= 26 && daysRemaining <= 29) {
                        coc = 5;
                    } else if (daysRemaining >= 15 && daysRemaining <= 25) {
                        coc = 30;
                    } else if (daysRemaining >= 8 && daysRemaining <= 14) {
                        coc = 50;
                    } else if (daysRemaining >= 2 && daysRemaining <= 7) {
                        coc = 80;
                    } else if (daysRemaining >= 0 && daysRemaining <= 1) {
                        coc = 100;
                    } else {
                        coc = 0;
                    }
                    BigDecimal cocPercentage = BigDecimal.valueOf(100 - coc).divide(BigDecimal.valueOf(100));
                    moneyBack = orderTotal.multiply(cocPercentage);
                }

                variables.put("refund", coc + "%");
                variables.put("moneyback", ReplaceUtils.formatPrice(moneyBack) + " VNĐ");
                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("customer-cancel-tour", variables), true);
                helper.setSubject("XÁC NHẬN HỦY TOUR.");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailCustomerCancelTour(BookingToursDto bookingToursDto) {
        emailQueueCustomerCancelTour.add(bookingToursDto);
    }

    @Override
    public void queueEmailOTPCus(ForgotPasswordDto passwordDto) {
        emailQueueSendOTPRegisterAgencies.add(passwordDto);
    }

    @Override
    public void sendMailOTPCus() {
        while (!emailQueueSendOTPRegisterAgencies.isEmpty()) {
            ForgotPasswordDto passwordDto = emailQueueSendOTPRegisterAgencies.poll();

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(passwordDto.getEmail());

                Map<String, Object> variables = new HashMap<>();
                variables.put("email", passwordDto.getEmail());
                variables.put("otp", passwordDto.getVerifyCode());

                helper.setFrom(passwordDto.getEmail());
                helper.setText(thymeleafService.createContent("otp-register-agencies", variables), true);
                helper.setSubject("TRAVELTOUR GỬI MÃ OTP ĐĂNG KÝ TÀI KHOẢN DOANH NGHIỆP");

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

    @Scheduled(fixedDelay = 5000)
    public void processForgotMail() {
        sendMailForgot();
    }

    @Scheduled(fixedDelay = 5000)
    public void processCustomerCancelTour() {
        sendMailCustomerCancelTour();
    }

    @Scheduled(fixedDelay = 5000)
    public void processEmailRegisterAgencies() {
        sendMailOTPCus();
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
}