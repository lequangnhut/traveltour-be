package com.main.traveltour.restcontroller.customer.bookingLocation.momo;

import com.main.traveltour.config.URLConfig;
import com.main.traveltour.configpayment.momo.config.Environment;
import com.main.traveltour.configpayment.momo.enums.RequestType;
import com.main.traveltour.configpayment.momo.models.PaymentResponse;
import com.main.traveltour.configpayment.momo.processor.CreateOrderMoMo;
import com.main.traveltour.configpayment.momo.shared.utils.LogUtils;
import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.customer.BookingTourService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import com.main.traveltour.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/booking-location/")
public class BookingLocationMomoCusAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private BookingTourService bookingTourService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private EmailService emailService;

    @PostMapping("momo/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderVNPay(@RequestParam("tourDetailId") String tourDetailId,
                                                                 @RequestParam("bookingTourId") String bookingTourId,
                                                                 @RequestParam("ticketAdult") int ticketAdult,
                                                                 @RequestParam("ticketChildren") int ticketChildren) throws Exception {
        TourDetails tourDetails = tourDetailsService.findById(tourDetailId);
        BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
        int orderTotal = (int) ((ticketAdult * unitPriceDecimal.intValue()) + (ticketChildren * (unitPriceDecimal.intValue() * 0.3)));

        Map<String, Object> response = new HashMap<>();

        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());

        String orderInfo = "Thanh Toan Don Hang #" + bookingTourId;
        String returnURL = URLConfig.ConfigUrl + "/api/v1/customer/booking-tour/momo/success-payment";
        String notifyURL = "/api/v1/momo/success-payment";

        Environment environment = Environment.selectEnv("dev");

        PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(orderTotal), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);

        response.put("redirectUrl", captureWalletMoMoResponse.getPayUrl());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Thêm mới tour với momo
     */
    @PostMapping("create-book-tour-momo/{transactionId}")
    private ResponseObject updateBookingTour(@RequestBody BookingDto bookingDto, @PathVariable int transactionId) {
        BookingToursDto bookingToursDto = bookingDto.getBookingToursDto();
        List<Map<String, String>> bookingTourCustomersDto = bookingDto.getBookingTourCustomersDto();

        Integer userId = bookingToursDto.getUserId();
        Integer totalAmountBook = bookingToursDto.getCapacityAdult() + bookingToursDto.getCapacityKid() + bookingToursDto.getCapacityBaby();
        try {
            if (transactionId != 0) {
                if (userId != null) {
                    BookingTours bookingTours = EntityDtoUtils.convertToEntity(bookingToursDto, BookingTours.class);
                    bookingTours.setDateCreated(new Timestamp(System.currentTimeMillis()));
                    createBookingTour(bookingToursDto, bookingTours, bookingTourCustomersDto, totalAmountBook, 1);

                    createInvoices(bookingTours.getId());
                    createContracts(bookingTours.getId());
                    decreaseAmountTour(bookingTours.getTourDetailId(), totalAmountBook);
                } else {
                    createUser(bookingToursDto, bookingTourCustomersDto, totalAmountBook, transactionId);
                }
            } else {
                if (userId != null) {
                    BookingTours bookingTours = EntityDtoUtils.convertToEntity(bookingToursDto, BookingTours.class);
                    bookingTours.setDateCreated(new Timestamp(System.currentTimeMillis()));
                    createBookingTour(bookingToursDto, bookingTours, bookingTourCustomersDto, totalAmountBook, 2);

                    createInvoices(bookingTours.getId());
                    createContracts(bookingTours.getId());
                    decreaseAmountTour(bookingTours.getTourDetailId(), totalAmountBook);
                } else {
                    createUser(bookingToursDto, bookingTourCustomersDto, totalAmountBook, transactionId);
                }
            }
            emailService.queueEmailBookingTour(bookingDto);
            return new ResponseObject("200", "Thành công", bookingDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("404", "Thất bại", null);
        }
    }

    private void createUser(BookingToursDto bookingToursDto, List<Map<String, String>> bookingTourCustomersDto, Integer totalAmountBook, int transactionId) {
        String email = bookingToursDto.getCustomerEmail();
        String phone = bookingToursDto.getCustomerPhone();
        String citizenCard = bookingToursDto.getCustomerCitizenCard();

        Users userPhone = usersService.findByPhone(phone);
        Users userCitizenCard = usersService.findByCardId(citizenCard);
        Optional<Users> currentUserOptional = Optional.ofNullable(usersService.findByEmail(email));

        Users user = currentUserOptional.orElseGet(() -> {
            Users newUser = new Users();
            newUser.setEmail(bookingToursDto.getCustomerEmail());
            newUser.setPassword(RandomUtils.RandomToken(10));
            newUser.setFullName(bookingToursDto.getCustomerName());
            if (userPhone == null) {
                newUser.setPhone(bookingToursDto.getCustomerPhone());
            } else if (userCitizenCard == null) {
                newUser.setCitizenCard(bookingToursDto.getCustomerCitizenCard());
            }
            newUser.setIsActive(Boolean.TRUE);
            usersService.authenticateRegister(newUser);
            return newUser;
        });

        if (transactionId != 0) {
            BookingTours bookingTours = EntityDtoUtils.convertToEntity(bookingToursDto, BookingTours.class);
            bookingTours.setUserId(user.getId());
            bookingTours.setDateCreated(new Timestamp(System.currentTimeMillis()));
            createBookingTour(bookingToursDto, bookingTours, bookingTourCustomersDto, totalAmountBook, 1);

            createInvoices(bookingTours.getId());
            createContracts(bookingTours.getId());
            decreaseAmountTour(bookingTours.getTourDetailId(), totalAmountBook);
        } else {
            BookingTours bookingTours = EntityDtoUtils.convertToEntity(bookingToursDto, BookingTours.class);
            bookingTours.setUserId(user.getId());
            bookingTours.setDateCreated(new Timestamp(System.currentTimeMillis()));
            createBookingTour(bookingToursDto, bookingTours, bookingTourCustomersDto, totalAmountBook, 2);
        }
    }

    private void createBookingTour(BookingToursDto bookingToursDto, BookingTours bookingTours, List<Map<String, String>> bookingTourCustomersDto, Integer totalAmountBook, int orderStatus) {
        String bookingTourId = bookingToursDto.getId();

        if (bookingToursDto.getPaymentMethod() == 3) { // 3: Momo
            bookingTours.setOrderStatus(orderStatus);
        }
        bookingTourService.saveBookingTour(bookingTours);

        createBookingTourCustomers(bookingTourId, bookingTourCustomersDto);
    }

    private void createBookingTourCustomers(String bookingTourId, List<Map<String, String>> bookingTourCustomersDto) {
        for (Map<String, String> data : bookingTourCustomersDto) {
            BookingTourCustomers bookingTourCustomers = new BookingTourCustomers();

            for (Map.Entry<String, String> entry : data.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key.startsWith("NameKH")) {
                    int index = Integer.parseInt(key.substring(6));

                    bookingTourCustomers.setBookingTourId(bookingTourId);
                    bookingTourCustomers.setCustomerName(value);
                    bookingTourCustomers.setCustomerBirth(Date.valueOf(data.get("BirthKH" + index)));
                    bookingTourCustomers.setCustomerPhone(data.get("PhoneKH" + index));

                    bookingTourService.saveBookingTourCustomer(bookingTourCustomers);
                }
            }
        }
    }

    private void decreaseAmountTour(String tourDetailId, Integer totalAmountBook) {
        TourDetails details = tourDetailsService.getById(tourDetailId);

        int currentBookSeat = details.getBookedSeat();
        int numberOfGuest = details.getNumberOfGuests();
        int newBookSeat = currentBookSeat + totalAmountBook;

        if (newBookSeat > numberOfGuest) {
            details.setBookedSeat(currentBookSeat);
            tourDetailsService.save(details);
        } else {
            details.setBookedSeat(currentBookSeat + totalAmountBook);
            tourDetailsService.save(details);
        }
    }

    private void createInvoices(String bookingTourId) {
        Invoices invoices = new Invoices();
        invoices.setId(GenerateNextID.generateNextInvoiceID(bookingTourService.findMaxCodeInvoices()));
        invoices.setBookingTourId(bookingTourId);
        invoices.setDateCreated(new Timestamp(System.currentTimeMillis()));
        bookingTourService.saveBookingInvoices(invoices);
    }

    private void createContracts(String bookingTourId) {
        Contracts contracts = new Contracts();
        contracts.setId(GenerateNextID.generateNextContractID(bookingTourService.findMaxCodeContracts()));
        contracts.setBookingTourId(bookingTourId);
        contracts.setDateCreated(new Timestamp(System.currentTimeMillis()));
        bookingTourService.saveBookingContracts(contracts);
    }
}
