package com.main.traveltour.service.staff;

import com.main.traveltour.entity.BookingTourCustomers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookingTourCustomerService {

    Page<BookingTourCustomers> findBySearchTermAndTourDetailId(String tourDetailId, String searchTerm, Pageable pageable);

    Optional<BookingTourCustomers> findById(Integer id);

    BookingTourCustomers findByCustomerPhone(String customerPhone);

    List<BookingTourCustomers> findByTourDetailId(String tourDetailId);

    BookingTourCustomers create(BookingTourCustomers bookingTourCustomers);

    BookingTourCustomers update(BookingTourCustomers bookingTourCustomers);

    void delete(Integer id);

    Long countCustomer();
}
