package com.main.traveltour.restcontroller.customer.bookingtour;

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
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class BookingTourCusAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private BookingTourService bookingTourService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private EmailService emailService;

    @PostMapping("book-tour/create-book-tour")
    private ResponseObject createBookingTour(@RequestBody BookingDto bookingDto) {
        BookingToursDto bookingToursDto = bookingDto.getBookingToursDto();
        List<Map<String, String>> bookingTourCustomersDto = bookingDto.getBookingTourCustomersDto();

        Integer userId = bookingToursDto.getUserId();
        Integer totalAmountBook = bookingToursDto.getCapacityAdult() + bookingToursDto.getCapacityKid() + bookingToursDto.getCapacityBaby();
        try {
            if (userId != null) {
                BookingTours bookingTourDto = EntityDtoUtils.convertToEntity(bookingToursDto, BookingTours.class);
                bookingTourDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
                if (bookingToursDto.getPaymentMethod() == 0) { // 0: travel
                    bookingTourDto.setOrderStatus(0); // 0: chờ thanh toán
                }
                bookingTourService.saveBookingTour(bookingTourDto);

                createBookingTourCustomers(bookingToursDto.getId(), bookingTourCustomersDto);
                createInvoices(bookingTourDto.getId());
                createContracts(bookingTourDto.getId());
                decreaseAmountTour(bookingTourDto.getTourDetailId(), totalAmountBook);
            } else {
                createUser(bookingToursDto, bookingTourCustomersDto, totalAmountBook);
            }
            return new ResponseObject("200", "Thành công", bookingDto);
        } catch (Exception e) {
            return new ResponseObject("404", "Thất bại", null);
        }
    }

    private void createUser(BookingToursDto bookingToursDto, List<Map<String, String>> bookingTourCustomersDto, Integer totalAmountBook) {
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

        BookingTours bookingTourDto = EntityDtoUtils.convertToEntity(bookingToursDto, BookingTours.class);
        bookingTourDto.setUserId(user.getId());
        bookingTourDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
        if (bookingToursDto.getPaymentMethod() == 0) { // 0: travel
            bookingTourDto.setOrderStatus(0); // 0: chờ thanh toán
        }
        bookingTourService.saveBookingTour(bookingTourDto);

        createBookingTourCustomers(bookingTourDto.getId(), bookingTourCustomersDto);
        createInvoices(bookingToursDto.getId());
        createContracts(bookingToursDto.getId());
        decreaseAmountTour(bookingTourDto.getTourDetailId(), totalAmountBook);
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
