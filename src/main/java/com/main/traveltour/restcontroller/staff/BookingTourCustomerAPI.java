package com.main.traveltour.restcontroller.staff;


import com.main.traveltour.dto.staff.BookingTourCustomersDto;
import com.main.traveltour.entity.BookingTourCustomers;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.BookingTourCustomerService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/booking-tour-customer/")
public class BookingTourCustomerAPI {

    @Autowired
    private BookingTourCustomerService bookingTourCustomerService;

    @GetMapping("find-all-booking-tour-customer-by-tour-details-id")
    public ResponseObject getAllBookingTour(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String tourDetailId,
            @RequestParam(required = false) String searchTerm) {

        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Page<BookingTourCustomers> bookingTourCustomers = bookingTourCustomerService.findBySearchTermAndTourDetailId(tourDetailId, searchTerm, PageRequest.of(page, size, sort));
            Page<BookingTourCustomersDto> bookingTourCustomersDto = bookingTourCustomers.map(bookingTourCustomer -> EntityDtoUtils.convertToDto(bookingTourCustomer, BookingTourCustomersDto.class));

            if (bookingTourCustomersDto.isEmpty()) {
                return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
            } else {
                return new ResponseObject("200", "Đã tìm thấy dữ liệu", bookingTourCustomersDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Không tìm thấy dữ liệu", e.getMessage());
        }
    }

    @GetMapping("find-by-id/{id}")
    private ResponseObject findById(@PathVariable int id) {
        try {
            Optional<BookingTourCustomers> bookingTourCustomer = bookingTourCustomerService.findById(id);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", EntityDtoUtils.convertOptionalToDto(bookingTourCustomer, BookingTourCustomersDto.class));
        } catch (Exception e) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }
    }

    @PostMapping("create-booking-tour-customer")
    private ResponseObject createAccountCustomer(@RequestPart BookingTourCustomersDto bookingTourCustomersDto,
                                                 @RequestPart String tourDetailId) {
        try {
            if (tourDetailId != null) {
                BookingTourCustomers bookingTourCustomer = EntityDtoUtils.convertToEntity(bookingTourCustomersDto, BookingTourCustomers.class);

                List<BookingTourCustomers> bookingTourCustomersList = bookingTourCustomerService.findByTourDetailId(tourDetailId);
                BookingTourCustomers firstCustomer = bookingTourCustomersList.stream().findFirst().orElse(null);

                bookingTourCustomer.setBookingTourId(firstCustomer.getBookingTourId());
                BookingTourCustomers createBookingTourCustomer = bookingTourCustomerService.create(bookingTourCustomer);
                return new ResponseObject("200", "Thêm thành công", createBookingTourCustomer);
            } else {
                return new ResponseObject("500", "Thêm thất bại", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Thêm thất bại", e.getMessage());
        }
    }

    @PutMapping("update-booking-tour-customer/{id}")
    public ResponseObject updateAccountCustomerById(
            @PathVariable int id,
            @RequestPart BookingTourCustomersDto bookingTourCustomersDto) {
        try {
            Optional<BookingTourCustomers> bookingTourCustomerOptional = bookingTourCustomerService.findById(id);
            if (bookingTourCustomerOptional.isEmpty()) {
                return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
            }

            BookingTourCustomers bookingTourCustomer = bookingTourCustomerOptional.get();
            bookingTourCustomer.setCustomerName(bookingTourCustomersDto.getCustomerName());
            bookingTourCustomer.setCustomerPhone(bookingTourCustomersDto.getCustomerPhone());
            bookingTourCustomer.setCustomerBirth(bookingTourCustomersDto.getCustomerBirth());

            BookingTourCustomers updatedBookingTourCustomer = bookingTourCustomerService.update(bookingTourCustomer);
            return new ResponseObject("200", "Cập nhật thành công", updatedBookingTourCustomer);
        } catch (Exception e) {
            return new ResponseObject("500", "Cập nhật thất bại: " + e.getMessage(), null);
        }
    }


    @DeleteMapping("delete-booking-tour-customer/{id}")
    private ResponseObject deleteAccountCustomerById(@PathVariable int id) {
        try {
            bookingTourCustomerService.delete(id);
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", true);
        } catch (Exception e) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", false);
        }

    }

    @GetMapping("check-duplicate-phone/{phone}")
    public Map<String, Boolean> checkPhone(@PathVariable String phone) {
        BookingTourCustomers bookingTourCustomer = bookingTourCustomerService.findByCustomerPhone(phone);
        Map<String, Boolean> response = new HashMap<>();

        if (bookingTourCustomer != null) {
            response.put("exists", true);
        } else {
            response.put("exists", false);
        }

        return response;
    }
}
