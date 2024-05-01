package com.main.traveltour.service.utils.impl;

import com.main.traveltour.config.DomainURL;
import com.main.traveltour.dto.agent.hotel.AgenciesDto;
import com.main.traveltour.dto.agent.transport.OrderTransportationsDto;
import com.main.traveltour.dto.auth.RegisterDto;
import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.dto.customer.hotel.OrderHotelCustomerDto;
import com.main.traveltour.dto.customer.infomation.*;
import com.main.traveltour.dto.customer.visit.BookingLocationCusDto;
import com.main.traveltour.dto.superadmin.AccountDto;
import com.main.traveltour.dto.superadmin.DataAccountDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.admin.HotelsServiceAD;
import com.main.traveltour.service.agent.PaymentMethodService;
import com.main.traveltour.service.agent.TransportationService;
import com.main.traveltour.service.customer.OrderVehicleDetailsService;
import com.main.traveltour.service.staff.*;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.service.utils.ThymeleafService;
import com.main.traveltour.utils.DateUtils;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.ReplaceUtils;
import com.main.traveltour.utils.TimeUtils;
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
import java.util.stream.Collectors;

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
    private final Queue<CancelBookingTourDTO> emailQueueStaffCancelTour = new LinkedList<>();
    private final Queue<ForgotPasswordDto> emailQueueSendOTPRegisterAgencies = new LinkedList<>();
    private final Queue<BookingDto> emailQueueBookingTourInvoices = new LinkedList<>();
    private final Queue<CancelOrderHotelsDto> emailQueueCustomerCancelHotel = new LinkedList<>();
    private final Queue<CancelOrderVisitsDto> emailQueueCustomerCancelVisit = new LinkedList<>();
    private final Queue<CancelOrderTransportationsDto> emailQueueCustomerCancelTrans = new LinkedList<>();
    private final Queue<OrderTransportationsDto> emailQueueCustomerBookingTrans = new LinkedList<>();
    private final Queue<BookingLocationCusDto> emailQueueCustomerBookingLocation = new LinkedList<>();
    private final Queue<ForgotPasswordDto> emailQueueForgotAdmin = new LinkedList<>();
    private final Queue<AgenciesDto> emailQueueDeleteAgency = new LinkedList<>();

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

    @Autowired
    private OrderVehicleDetailsService orderVehicleDetailsService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private TransportationService transportationService;

    @Autowired
    private OrderVisitLocationDetailService orderVisitLocationDetailService;

    @Autowired
    private VisitLocationService visitLocationService;


    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    private RoomTypesRepository roomTypesRepository;

    @Autowired
    private HotelsServiceAD hotelsServiceAD;

    @Autowired
    private OrderHotelsService orderHotelsService;

    @Autowired
    private PaymentMethodService paymentMethodService;

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
                variables.put("domain", DomainURL.BACKEND_URL);

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
                variables.put("domain", DomainURL.FRONTEND_URL);

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
                variables.put("domain", DomainURL.FRONTEND_URL);

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
                variables.put("domain", DomainURL.FRONTEND_URL);

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
                variables.put("totalCapacity", capacityAdult + " Người lớn, " + capacityKid + " Trẻ em.");
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
                variables.put("domain", DomainURL.BACKEND_URL);

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
                variables.put("totalCapacity", capacityAdult + " Người lớn, " + capacityKid + " Trẻ em.");
                variables.put("departureDate", tourDetails.getDepartureDate());
                variables.put("arrivalDate", tourDetails.getArrivalDate());
                variables.put("unitPrice", ReplaceUtils.formatPrice(tourDetails.getUnitPrice()) + " VNĐ");
                variables.put("orderTotal", ReplaceUtils.formatPrice(orderTotal) + " VNĐ");
                variables.put("refund", bookingToursDto.getCoc() + "%");
                variables.put("moneyback", ReplaceUtils.formatPrice(bookingToursDto.getMoneyBack()) + " VNĐ");
                variables.put("reasonNote", bookingToursDto.getReasonNote());
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
    public void sendMailStaffCancelTour() {
        while (!emailQueueStaffCancelTour.isEmpty()) {
            CancelBookingTourDTO bookingToursDto = emailQueueStaffCancelTour.poll();
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
                variables.put("totalCapacity", capacityAdult + " Người lớn, " + capacityKid + " Trẻ em.");
                variables.put("departureDate", tourDetails.getDepartureDate());
                variables.put("arrivalDate", tourDetails.getArrivalDate());
                variables.put("unitPrice", ReplaceUtils.formatPrice(tourDetails.getUnitPrice()) + " VNĐ");
                variables.put("orderTotal", ReplaceUtils.formatPrice(orderTotal) + " VNĐ");
                variables.put("refund", bookingToursDto.getCoc() + "%");
                variables.put("moneyback", ReplaceUtils.formatPrice(bookingToursDto.getMoneyBack()) + " VNĐ");
                variables.put("reasonNote", bookingToursDto.getReasonNote());
                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("staff-cancel-tour", variables), true);
                helper.setSubject("XÁC NHẬN HỦY TOUR.");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailStaffCancelTour(CancelBookingTourDTO bookingToursDto) {
        emailQueueStaffCancelTour.add(bookingToursDto);
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
                variables.put("totalCapacity", capacityAdult + " Người lớn, " + capacityKid + " Trẻ em.");
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
    public void queueEmailBookingLocation(BookingLocationCusDto bookingLocationCusDto) {
        emailQueueCustomerBookingLocation.add(bookingLocationCusDto);
    }

    @Override
    public void sendMailBookingLocation() {
        while (!emailQueueCustomerBookingLocation.isEmpty()) {
            BookingLocationCusDto bookingLocationCusDto = emailQueueCustomerBookingLocation.poll();
            com.main.traveltour.dto.staff.OrderVisitsDto orderVisitsDto = bookingLocationCusDto.getOrderVisitsDto();
            OrderVisits orderVisits = bookingLocationCusDto.getOrderVisits();
            List<OrderVisitDetails> orderVisitDetails = orderVisitLocationDetailService.findByOrderVisitId(orderVisits.getId());
            VisitLocations visitLocations = visitLocationService.findByIdAndIsActiveIsTrue(orderVisits.getVisitLocationId());

            String customerEmail = orderVisitsDto.getCustomerEmail();

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(customerEmail);

                Map<String, Object> variables = new HashMap<>();

                if (orderVisits.getPaymentMethod() == 1) {
                    variables.put("paymentMethod", "VÍ VNPAY");
                } else if (orderVisits.getPaymentMethod() == 2) {
                    variables.put("paymentMethod", "VÍ ZALOPAY");
                } else if (orderVisits.getPaymentMethod() == 3) {
                    variables.put("paymentMethod", "VÍ MOMO");
                } else {
                    variables.put("paymentMethod", "Thanh toán tại quầy");
                }

                variables.put("bookingId", orderVisits.getId());
                variables.put("dateTimeBooking", DateUtils.formatTimestamp(String.valueOf(new Timestamp(System.currentTimeMillis()))));
                variables.put("locationName", visitLocations.getVisitLocationName());
                variables.put("customerName", orderVisits.getCustomerName());
                variables.put("customerCitizenCard", orderVisits.getCustomerCitizenCard());
                variables.put("customerPhone", orderVisits.getCustomerPhone());
                variables.put("checkIn", DateUtils.formatTimestamp(String.valueOf(orderVisits.getCheckIn())));
                variables.put("orderTotal", orderVisits.getOrderTotal());

                variables.put("orderVisitDetails", orderVisitDetails);

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("order-location-verify", variables), true);
                helper.setSubject("DỊCH VỤ LỮ HÀNH TRAVELTOUR XIN CHÂN THÀNH CẢM ƠN QUÝ KHÁCH ĐÃ ĐẶT VÉ THAM QUAN CỦA CHÚNG TÔI.");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void sendMailCustomerBookingTransport() {
        while (!emailQueueCustomerBookingTrans.isEmpty()) {
            OrderTransportationsDto orderTransportDto = emailQueueCustomerBookingTrans.poll();
            TransportationSchedules schedules = transportationScheduleService.findById(orderTransportDto.getTransportationScheduleId());
            Optional<Transportations> transportationsOptional = transportationService.findTransportById(schedules.getTransportationId());
            Transportations transportations = transportationsOptional.get();

            String customerEmail = "";
            if (orderTransportDto.getUserId() != null) {
                customerEmail = orderTransportDto.getCustomerEmail();
            }

            BigDecimal orderTotal = new BigDecimal(orderTransportDto.getAmountTicket()).multiply(new BigDecimal(schedules.getUnitPrice().intValue()));

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                if (orderTransportDto.getUserId() != null) {
                    helper.setTo(customerEmail);
                } else {
                    helper.setTo(orderTransportDto.getCustomerEmail());
                }

                Map<String, Object> variables = new HashMap<>();
                variables.put("bookingId", orderTransportDto.getId());
                variables.put("dateTimeBooking", DateUtils.formatTimestamp(String.valueOf(new Timestamp(System.currentTimeMillis()))));
                variables.put("customerName", orderTransportDto.getCustomerName());
                variables.put("customerPhone", orderTransportDto.getCustomerPhone());
                variables.put("customerEmail", orderTransportDto.getCustomerEmail());

                if (orderTransportDto.getPaymentMethod() == 1) {
                    variables.put("paymentMethod", "VÍ VNPAY");
                } else if (orderTransportDto.getPaymentMethod() == 2) {
                    variables.put("paymentMethod", "VÍ ZALOPAY");
                } else if (orderTransportDto.getPaymentMethod() == 3) {
                    variables.put("paymentMethod", "VÍ MOMO");
                } else {
                    variables.put("paymentMethod", "Thanh toán tại quầy");
                }

                variables.put("transportBrand", transportations.getTransportationBrandsByTransportationBrandId().getTransportationBrandName());
                variables.put("scheduleId", schedules.getId());
                variables.put("amountTicket", orderTransportDto.getAmountTicket());
                variables.put("locationSchedule", schedules.getFromLocation() + " - " + schedules.getToLocation());
                variables.put("departureTime", TimeUtils.formatTime(schedules.getDepartureTime()));
                variables.put("orderTotal", ReplaceUtils.formatPrice(orderTotal) + "VNĐ");

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("order-transportation-verify", variables), true);
                helper.setSubject("DỊCH VỤ LỮ HÀNH TRAVELTOUR XIN CHÂN THÀNH CẢM ƠN QUÝ KHÁCH ĐÃ ĐẶT VÉ XE CỦA CHÚNG TÔI.");
                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailCustomerBookingTransport(OrderTransportationsDto orderTransportationsDto) {
        emailQueueCustomerBookingTrans.add(orderTransportationsDto);
    }

    @Override
    public void sendEmailBookingHotel(OrderHotelCustomerDto orderHotelCustomerDto, List<OrderDetailsHotelCustomerDto> orderDetailsHotelCustomerDtos) {

        OrderHotels orderHotels = orderHotelsService.findById(orderHotelCustomerDto.getId());
        List<OrderHotelDetails> orderHotelDetails = orderHotelDetailService.findByOrderHotelId(orderHotels.getId());

        orderHotels.setOrderHotelDetailsById(orderHotelDetails);
        List<RoomTypes> roomTypes = orderHotels.getOrderHotelDetailsById().stream()
                .map(item -> roomTypesRepository.findById(item.getRoomTypeId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        for (var order : orderHotels.getOrderHotelDetailsById()) {
            for (var roomType : roomTypes) {
                if (roomType.getId() == order.getRoomTypeId()) {
                    order.setRoomTypesByRoomTypeId(roomType);
                }
                if (orderHotels.getId() == order.getOrderHotelId()) {
                    order.setOrderHotelsByOrderHotelId(orderHotels);
                }
            }
        }

        String customerEmail = orderHotelCustomerDto.getCustomerEmail();
        PaymentMethod paymentMethod = paymentMethodService.findById(orderHotelCustomerDto.getPaymentMethod());

        BigDecimal totalAmount = orderHotels.getOrderHotelDetailsById().stream()
                .map(item -> BigDecimal.valueOf(item.getAmount()).multiply(item.getUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        try {
            if (customerEmail != null) {


                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(customerEmail);

                Map<String, Object> variables = new HashMap<>();

                variables.put("paymentMethod", paymentMethod.getDescription());

                variables.put("id", orderHotels.getId());
                variables.put("dateCreate", orderHotels.getDateCreated());
                variables.put("customerEmail", orderHotels.getCustomerEmail());
                variables.put("customerName", orderHotels.getCustomerName());
                variables.put("customerPhone", orderHotels.getCustomerPhone());
                variables.put("checkIn", DateUtils.formatTimestamp(String.valueOf(orderHotels.getCheckIn())));
                variables.put("orderTotal", orderHotels.getOrderTotal());
                variables.put("capacityAdult", orderHotels.getCapacityAdult());
                variables.put("capacityKid", orderHotels.getCapacityKid());
                variables.put("rooms", orderHotels.getOrderHotelDetailsById());
                variables.put("totalAmount", totalAmount);

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("order-hotel-verify", variables), true);
                helper.setSubject("DỊCH VỤ LỮ HÀNH TRAVELTOUR XIN CHÂN THÀNH CẢM ƠN QUÝ KHÁCH ĐÃ ĐẶT VÉ THAM QUAN CỦA CHÚNG TÔI.");

                sender.send(message);

                for (var emailCustomer : orderDetailsHotelCustomerDtos) {
                    if (emailCustomer.getCustomerEmail() != null) {

                        helper.setTo(emailCustomer.getCustomerEmail());

                        variables.put("paymentMethod", paymentMethod.getDescription());

                        variables.put("id", orderHotels.getId());
                        variables.put("dateCreate", orderHotels.getDateCreated());
                        variables.put("customerEmail", orderHotels.getCustomerEmail());
                        variables.put("customerName", orderHotels.getCustomerName());
                        variables.put("customerPhone", orderHotels.getCustomerPhone());
                        variables.put("checkIn", DateUtils.formatTimestamp(String.valueOf(orderHotels.getCheckIn())));
                        variables.put("orderTotal", orderHotels.getOrderTotal());
                        variables.put("capacityAdult", orderHotels.getCapacityAdult());
                        variables.put("capacityKid", orderHotels.getCapacityKid());
                        variables.put("rooms", orderHotels.getOrderHotelDetailsById());
                        variables.put("totalAmount", totalAmount);

                        helper.setFrom(email);
                        helper.setText(thymeleafService.createContent("order-hotel-verify", variables), true);
                        helper.setSubject("DỊCH VỤ LỮ HÀNH TRAVELTOUR XIN CHÂN THÀNH CẢM ƠN QUÝ KHÁCH ĐÃ ĐẶT VÉ THAM QUAN CỦA CHÚNG TÔI.");

                        sender.send(message);
                    }
                }
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendMailForgotAdmin() {
        while (!emailQueueForgotAdmin.isEmpty()) {
            ForgotPasswordDto passwordsDto = emailQueueForgotAdmin.poll();
            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(passwordsDto.getEmail());

                Map<String, Object> variables = new HashMap<>();
                variables.put("full_name", passwordsDto.getFull_name());
                variables.put("email", passwordsDto.getEmail());
                variables.put("verifyCode", passwordsDto.getVerifyCode());
                variables.put("domain", DomainURL.BACKEND_URL);

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("send-otp-admin", variables), true);
                helper.setSubject("THAY ĐỔI MẬT KHẨU");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailForgotAdmin(ForgotPasswordDto passwordsDto) {
        emailQueueForgotAdmin.add(passwordsDto);
    }

    @Override
    public void queueEmailDeleteAgency(AgenciesDto agenciesDto) {
        emailQueueDeleteAgency.add(agenciesDto);
    }

    @Override
    public void sendMailDeleteAgency() {
        while (!emailQueueDeleteAgency.isEmpty()) {
            AgenciesDto agenciesDto = emailQueueDeleteAgency.poll();
            Users users = userService.findById(agenciesDto.getUserId());
            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setTo(users.getEmail());

                Map<String, Object> variables = new HashMap<>();
                variables.put("name_agency", agenciesDto.getNameAgency());
                variables.put("note", agenciesDto.getNoted());
                variables.put("dateBlocked", agenciesDto.getDateBlocked());
                variables.put("domain", DomainURL.FRONTEND_URL);

                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("agency-delete", variables), true);
                helper.setSubject("TRAVELTOUR XIN THÔNG BÁO HỒ SƠ DOANH NGHIỆP BỊ TẠM KHÓA HOẠT ĐỘNG");

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

                if (cancelOrderHotelsDto.getPaymentMethod() == "MOMO") {
                    variables.put("paymentMethod", "VÍ MOMO");
                } else if (cancelOrderHotelsDto.getPaymentMethod() == "VNPAY") {
                    variables.put("paymentMethod", "VNPAY");
                } else if (cancelOrderHotelsDto.getPaymentMethod() == "ZALOPAY") {
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
                variables.put("reasonNote", cancelOrderHotelsDto.getReasonNote());
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
        while (!emailQueueCustomerCancelVisit.isEmpty()) {
            CancelOrderVisitsDto cancelOrderVisitsDto = emailQueueCustomerCancelVisit.poll();
            List<OrderVisitDetails> orderVisitDetailsList = orderVisitLocationDetailService.findByOrderVisitId(cancelOrderVisitsDto.getId());
            List<OrderVisitDetailsDto> orderVisitDetailsDtoList = EntityDtoUtils.convertToDtoList(orderVisitDetailsList, OrderVisitDetailsDto.class);

            BigDecimal orderTotal = cancelOrderVisitsDto.getOrderTotal(); // Lấy tổng tiền

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                if (cancelOrderVisitsDto.getUserId() != null) {
                    helper.setTo(cancelOrderVisitsDto.getCustomerEmail());
                } else {
                    helper.setTo(cancelOrderVisitsDto.getCustomerEmail());
                }

                Map<String, Object> variables = new HashMap<>();
                variables.put("customerEmail", cancelOrderVisitsDto.getCustomerEmail());
                variables.put("bookingId", cancelOrderVisitsDto.getId());
                variables.put("dateTimeBooking", cancelOrderVisitsDto.getDateCreated());
                variables.put("timeDelete", new Timestamp(System.currentTimeMillis()));
                variables.put("customerName", cancelOrderVisitsDto.getCustomerName());
                variables.put("customerCitizenCard", cancelOrderVisitsDto.getCustomerCitizenCard());
                variables.put("customerPhone", cancelOrderVisitsDto.getCustomerPhone());

                if (cancelOrderVisitsDto.getPaymentMethod() == 1) {
                    variables.put("paymentMethod", "VÍ VNPAY");
                } else if (cancelOrderVisitsDto.getPaymentMethod() == 2) {
                    variables.put("paymentMethod", "VÍ ZALOPAY");
                } else if (cancelOrderVisitsDto.getPaymentMethod() == 3) {
                    variables.put("paymentMethod", "VÍ MOMO");
                } else {
                    variables.put("paymentMethod", "Thanh toán tại quầy");
                }

                variables.put("visitName", cancelOrderVisitsDto.getVisitLocations().getVisitLocationName());
                variables.put("departureDate", cancelOrderVisitsDto.getCheckIn());
                variables.put("orderDetails", orderVisitDetailsDtoList);
                variables.put("orderTotal", ReplaceUtils.formatPrice(orderTotal) + " VNĐ");
                variables.put("refund", cancelOrderVisitsDto.getCoc() + "%");
                variables.put("reasonNote", cancelOrderVisitsDto.getReasonNote());
                variables.put("moneyback", ReplaceUtils.formatPrice(cancelOrderVisitsDto.getMoneyBack()) + " VNĐ");
                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("customer-cancel-visit", variables), true);
                helper.setSubject("XÁC NHẬN HỦY VÉ THAM QUAN.");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailCustomerCancelVisit(CancelOrderVisitsDto cancelOrderVisitsDto) {
        emailQueueCustomerCancelVisit.add(cancelOrderVisitsDto);
    }

    @Override
    public void sendMailCustomerCancelTrans() {
        while (!emailQueueCustomerCancelTrans.isEmpty()) {
            CancelOrderTransportationsDto cancelOrderTransportationsDto = emailQueueCustomerCancelTrans.poll();

            List<OrderTransportationDetails> orderTransportationDetailsList = orderVehicleDetailsService.findByOrderId(cancelOrderTransportationsDto.getId());
            List<OrderTransportationDetailsDto> orderTransportationDetailsDtos = EntityDtoUtils.convertToDtoList(orderTransportationDetailsList, OrderTransportationDetailsDto.class);

            BigDecimal orderTotal = cancelOrderTransportationsDto.getOrderTotal(); // Lấy tổng tiền

            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                if (cancelOrderTransportationsDto.getUserId() != null) {
                    helper.setTo(cancelOrderTransportationsDto.getCustomerEmail());
                } else {
                    helper.setTo(cancelOrderTransportationsDto.getCustomerEmail());
                }

                Map<String, Object> variables = new HashMap<>();
                variables.put("customerEmail", cancelOrderTransportationsDto.getCustomerEmail());
                variables.put("bookingId", cancelOrderTransportationsDto.getId());
                variables.put("dateTimeBooking", cancelOrderTransportationsDto.getDateCreated());
                variables.put("timeDelete", new Timestamp(System.currentTimeMillis()));
                variables.put("customerName", cancelOrderTransportationsDto.getCustomerName());
                variables.put("customerCitizenCard", cancelOrderTransportationsDto.getCustomerCitizenCard());
                variables.put("customerPhone", cancelOrderTransportationsDto.getCustomerPhone());

                if (cancelOrderTransportationsDto.getPaymentMethod() == 1) {
                    variables.put("paymentMethod", "VÍ VNPAY");
                } else if (cancelOrderTransportationsDto.getPaymentMethod() == 2) {
                    variables.put("paymentMethod", "VÍ ZALOPAY");
                } else if (cancelOrderTransportationsDto.getPaymentMethod() == 3) {
                    variables.put("paymentMethod", "VÍ MOMO");
                } else {
                    variables.put("paymentMethod", "Thanh toán tại quầy");
                }

                variables.put("brandName", cancelOrderTransportationsDto.getTransportationBrands().getTransportationBrandName());
                variables.put("tripName", "Từ " + cancelOrderTransportationsDto.getTransportationSchedulesByTransportationScheduleId().getFromLocation() + " - " + cancelOrderTransportationsDto.getTransportationSchedulesByTransportationScheduleId().getToLocation());
                variables.put("unitPrice", ReplaceUtils.formatPrice(cancelOrderTransportationsDto.getTransportationSchedulesByTransportationScheduleId().getUnitPrice()) + " VNĐ");
                variables.put("amount", cancelOrderTransportationsDto.getAmountTicket());
                variables.put("departureDate", cancelOrderTransportationsDto.getTransportationSchedulesByTransportationScheduleId().getDepartureTime());
                variables.put("arrivalDate", cancelOrderTransportationsDto.getTransportationSchedulesByTransportationScheduleId().getArrivalTime());
                variables.put("orderDetails", orderTransportationDetailsDtos);
                variables.put("orderTotal", ReplaceUtils.formatPrice(orderTotal) + " VNĐ");
                variables.put("refund", cancelOrderTransportationsDto.getCoc() + "%");
                variables.put("moneyback", ReplaceUtils.formatPrice(cancelOrderTransportationsDto.getMoneyBack()) + " VNĐ");
                variables.put("reasonNote", cancelOrderTransportationsDto.getReasonNote());
                helper.setFrom(email);
                helper.setText(thymeleafService.createContent("customer-cancel-trans", variables), true);
                helper.setSubject("XÁC NHẬN HỦY VÉ XE.");

                sender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void queueEmailCustomerCancelTrans(CancelOrderTransportationsDto cancelOrderTransportationsDto) {
        emailQueueCustomerCancelTrans.add(cancelOrderTransportationsDto);
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
    public void processBookingTransport() {
        sendMailCustomerBookingTransport();
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
    public void processStaffCancelTour() {
        sendMailStaffCancelTour();
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

    @Scheduled(fixedDelay = 5000)
    public void processForgotMailAdmin() {
        sendMailForgotAdmin();
    }

    @Scheduled(fixedDelay = 5000)
    public void processBookingLocation() {
        sendMailBookingLocation();
    }

    @Scheduled(fixedDelay = 5000)
    public void processDeleteAgency() {
        sendMailDeleteAgency();
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