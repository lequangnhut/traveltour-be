package com.main.traveltour.restcontroller.customer.bookingtour.service.Impl;

import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.restcontroller.customer.bookingtour.service.BookingTourAPIService;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.customer.BookingTourService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import com.main.traveltour.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingTourAPIServiceImpl implements BookingTourAPIService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private BookingTourService bookingTourService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Override
    public void createUser(BookingToursDto bookingToursDto, List<Map<String, String>> bookingTourCustomersDto, Integer totalAmountBook, Integer orderStatus) {
        TourDetails tourDetails = tourDetailsService.findById(bookingToursDto.getTourDetailId());

        String email = bookingToursDto.getCustomerEmail();
        String phone = bookingToursDto.getCustomerPhone();
        String citizenCard = bookingToursDto.getCustomerCitizenCard();

        BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
        int capacityAdult = bookingToursDto.getCapacityAdult();
        int capacityKid = bookingToursDto.getCapacityKid();
        int unitPrice = unitPriceDecimal.intValue();

        BigDecimal orderTotal = BigDecimal.valueOf((capacityAdult * unitPrice) + (capacityKid * (unitPrice * 0.3)));

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
        bookingTourDto.setOrderTotal(orderTotal);
        bookingTourDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
        bookingTourDto.setOrderStatus(orderStatus); // 0: chờ thanh toán
        bookingTourService.saveBookingTour(bookingTourDto);

        createBookingTourCustomers(bookingTourDto.getId(), bookingTourCustomersDto); // tạo danh sách khách hàng

        if (orderStatus != 0) { // nếu bằng 1 thì thành công còn ngược lại thì thất bại
            decreaseAmountTour(bookingTourDto.getTourDetailId(), totalAmountBook); // trừ số lượng tour detail
            createInvoices(bookingToursDto.getId()); // tạo hóa đơn
            createContracts(bookingToursDto.getId()); // tạo hợp đồng
        }
    }

    @Override
    public void createBookingTour(BookingToursDto bookingToursDto, BookingTours bookingTours, List<Map<String, String>> bookingTourCustomersDto, Integer orderStatus) {
        String bookingTourId = bookingToursDto.getId();

        bookingTours.setOrderStatus(orderStatus);
        bookingTourService.saveBookingTour(bookingTours);

        createBookingTourCustomers(bookingTourId, bookingTourCustomersDto);
    }

    @Override
    public void createBookingTourCustomers(String bookingTourId, List<Map<String, String>> bookingTourCustomersDto) {
        for (Map<String, String> data : bookingTourCustomersDto) {
            BookingTourCustomers bookingTourCustomers = new BookingTourCustomers();

            for (Map.Entry<String, String> entry : data.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key.startsWith("Name") && key.length() == 5) {
                    int index = Integer.parseInt(key.substring(4));

                    bookingTourCustomers.setBookingTourId(bookingTourId);
                    bookingTourCustomers.setCustomerName(value);
                    bookingTourCustomers.setCustomerBirth(Date.valueOf(data.get("Birth" + index)));
                    bookingTourCustomers.setCustomerPhone(data.get("Phone" + index));

                    bookingTourService.saveBookingTourCustomer(bookingTourCustomers);
                }
            }
        }
    }

    @Override
    public void decreaseAmountTour(String tourDetailId, Integer totalAmountBook) {
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

    @Override
    public void createInvoices(String bookingTourId) {
        Invoices invoices = new Invoices();
        invoices.setId(GenerateNextID.generateNextInvoiceID(bookingTourService.findMaxCodeInvoices()));
        invoices.setBookingTourId(bookingTourId);
        invoices.setDateCreated(new Timestamp(System.currentTimeMillis()));
        bookingTourService.saveBookingInvoices(invoices);
    }

    @Override
    public void createContracts(String bookingTourId) {
        Contracts contracts = new Contracts();
        contracts.setId(GenerateNextID.generateNextContractID(bookingTourService.findMaxCodeContracts()));
        contracts.setBookingTourId(bookingTourId);
        contracts.setDateCreated(new Timestamp(System.currentTimeMillis()));
        bookingTourService.saveBookingContracts(contracts);
    }
}
