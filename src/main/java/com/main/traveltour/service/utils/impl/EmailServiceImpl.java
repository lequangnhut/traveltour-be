package com.main.traveltour.service.utils.impl;

import com.main.traveltour.dto.agent.hotel.AgenciesDto;
import com.main.traveltour.dto.auth.RegisterDto;
import com.main.traveltour.dto.customer.infomation.*;
import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.dto.superadmin.AccountDto;
import com.main.traveltour.dto.superadmin.DataAccountDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.repository.VisitLocationsRepository;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.admin.HotelsServiceAD;
import com.main.traveltour.service.agent.OrderVisitDetailService;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.staff.ToursService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.service.utils.ThymeleafService;
import com.main.traveltour.utils.DateUtils;
import com.main.traveltour.utils.EntityDtoUtils;
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
import java.util.concurrent.TimeUnit;

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
    private final Queue<CancelBookingTourDTO> emailQueueCustomerCancelTour = new LinkedList<>();
    private final Queue<ForgotPasswordDto> emailQueueSendOTPRegisterAgencies = new LinkedList<>();
    private final Queue<BookingDto> emailQueueBookingTourInvoices = new LinkedList<>();
    private final Queue<CancelOrderHotelsDto> emailQueueCustomerCancelHotel = new LinkedList<>();
    private final Queue<OrderVisitsDto> emailQueueCustomerCancelVisit = new LinkedList<>();
    private final Queue<OrderTransportationsDto> emailQueueCustomerCancelTrans = new LinkedList<>();

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

    @Autowired
    private OrderHotelDetailService orderHotelDetailService;

    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    private RoomTypesRepository roomTypesRepository;

    @Autowired
    private HotelsServiceAD hotelsServiceAD;

    @Autowired
    private OrderVisitDetailService orderVisitDetailService;

    @Autowired
    private VisitLocationsRepository visitLocationsRepository;


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
            CancelBookingTourDTO bookingToursDto = emailQueueCustomerCancelTour.poll();
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
                variables.put("refund", bookingToursDto.getCoc() + "%");
                variables.put("moneyback", ReplaceUtils.formatPrice(bookingToursDto.getMoneyBack()) + " VNĐ");
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
    public void queueEmailCustomerCancelTour(CancelBookingTourDTO bookingToursDto) {
        emailQueueCustomerCancelTour.add(bookingToursDto);
    }

    @Override
    public void queueEmailOTPCus(ForgotPasswordDto passwordDto) {
        emailQueueSendOTPRegisterAgencies.add(passwordDto);
    }

    @Override
    public void queueEmailBookingTourInvoices(BookingDto bookingDto) {
        emailQueueBookingTourInvoices.add(bookingDto);
    }

    @Override
    public void sendMailBookingTourInvoices() {
        while (!emailQueueBookingTourInvoices.isEmpty()) {
            BookingDto bookingDto = emailQueueBookingTourInvoices.poll();
            BookingToursDto bookingToursDto = bookingDto.getBookingToursDto();

            TourDetails tourDetails = tourDetailsService.findById(bookingToursDto.getTourDetailId());
            Optional<Tours> tours = toursService.findById(tourDetails.getTourId());

            String customerEmail = "";
            if (bookingToursDto.getUserId() != null) {
                customerEmail = bookingToursDto.getCustomerEmail();
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
                variables.put("dateTimeBooking", DateUtils.formatTimestamp(String.valueOf(new Timestamp(System.currentTimeMillis()))));
                variables.put("tourType", tours.get().getTourTypesByTourTypeId().getTourTypeName());
                variables.put("customerName", bookingToursDto.getCustomerName());
                variables.put("customerCitizenCard", bookingToursDto.getCustomerCitizenCard());
                variables.put("customerPhone", bookingToursDto.getCustomerPhone());
                variables.put("paymentMethod", "Tiền mặt");
                variables.put("tourName", tours.get().getTourName());
                variables.put("totalCapacity", capacityAdult + " Người lớn, " + capacityKid + " Trẻ em, " + bookingToursDto.getCapacityBaby() + " Em bé.");
                variables.put("departureDate", DateUtils.formatTimestamp(String.valueOf(tourDetails.getDepartureDate())));
                variables.put("arrivalDate", DateUtils.formatTimestamp(String.valueOf(tourDetails.getArrivalDate())));
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

    @Override
    public void sendMailCustomerCancelHotel() {
        while (!emailQueueCustomerCancelHotel.isEmpty()) {
            CancelOrderHotelsDto cancelOrderHotelsDto = emailQueueCustomerCancelHotel.poll();
            List<OrderHotelDetails> orderHotelDetails = orderHotelDetailService.findByOrderHotelId(cancelOrderHotelsDto.getId());
            List<OrderHotelDetailsDto> orderHotelDetailsDto = EntityDtoUtils.convertToDtoList(orderHotelDetails, OrderHotelDetailsDto.class);

            String name = null;
            BigDecimal orderTotal = cancelOrderHotelsDto.getOrderTotal(); // Lấy tổng tiền

            for (OrderHotelDetailsDto orderDetail : orderHotelDetailsDto) {
                RoomTypes roomType = orderDetail.getRoomTypes();
                Hotels hotelFound = hotelsServiceAD.findById(roomType.getHotelId());
                name = hotelFound.getHotelName();
            }

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                if (cancelOrderHotelsDto.getUserId() != null) {
                    helper.setTo(cancelOrderHotelsDto.getCustomerEmail());
                } else {
                    helper.setTo(cancelOrderHotelsDto.getCustomerEmail());
                }

                Map<String, Object> variables = new HashMap<>();
                variables.put("customerEmail", cancelOrderHotelsDto.getCustomerEmail());
                variables.put("bookingId", cancelOrderHotelsDto.getId());
                variables.put("dateTimeBooking", cancelOrderHotelsDto.getDateCreated());
                variables.put("timeDelete", new Timestamp(System.currentTimeMillis()));
                variables.put("customerName", cancelOrderHotelsDto.getCustomerName());
                variables.put("customerCitizenCard", cancelOrderHotelsDto.getCustomerCitizenCard());
                variables.put("customerPhone", cancelOrderHotelsDto.getCustomerPhone());

                if (cancelOrderHotelsDto.getPaymentMethod() == "Momo") {
                    variables.put("paymentMethod", "VÍ MOMO");
                } else if (cancelOrderHotelsDto.getPaymentMethod() == "VNPay") {
                    variables.put("paymentMethod", "VNPAY");
                } else if (cancelOrderHotelsDto.getPaymentMethod() == "ZaloPay") {
                    variables.put("paymentMethod", "VÍ ZALOPAY");
                } else {
                    variables.put("paymentMethod", "Thanh toán tại quầy");
                }

                variables.put("hotelName", name);
                variables.put("departureDate", cancelOrderHotelsDto.getCheckIn());
                variables.put("arrivalDate", cancelOrderHotelsDto.getCheckOut());
                variables.put("orderHotelDetails", orderHotelDetails);
                variables.put("orderTotal", ReplaceUtils.formatPrice(orderTotal) + " VNĐ");
                variables.put("refund", cancelOrderHotelsDto.getCoc() + "%");
                variables.put("moneyback", ReplaceUtils.formatPrice(cancelOrderHotelsDto.getMoneyBack()) + " VNĐ");
                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("customer-cancel-hotel", variables), true);
                helper.setSubject("XÁC NHẬN HỦY PHÒNG.");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailCustomerCancelHotel(CancelOrderHotelsDto cancelOrderHotelsDto) {
        emailQueueCustomerCancelHotel.add(cancelOrderHotelsDto);
    }

    @Override
    public void sendMailCustomerCancelVisit() {

    }

    @Override
    public void queueEmailCustomerCancelVisit(OrderVisitsDto orderVisitsDto) {
        emailQueueCustomerCancelVisit.add(orderVisitsDto);
    }

    @Override
    public void sendMailCustomerCancelTrans() {

    }

    @Override
    public void queueEmailCustomerCancelTrans(OrderTransportationsDto orderTransportationsDto) {
        emailQueueCustomerCancelTrans.add(orderTransportationsDto);
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

    @Scheduled(fixedDelay = 5000)
    public void processBookingTourInvoices() {
        sendMailBookingTourInvoices();
    }

    @Scheduled(fixedDelay = 5000)
    public void processCustomerCancelHotel() {
        sendMailCustomerCancelHotel();
    }

    @Scheduled(fixedDelay = 5000)
    public void processCustomerCancelVisit() {
        sendMailCustomerCancelVisit();
    }

    @Scheduled(fixedDelay = 5000)
    public void processCustomerCancelTrans() {
        sendMailCustomerCancelTrans();
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