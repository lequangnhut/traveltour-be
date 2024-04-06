package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.staff.BookingToursDto;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.BookingTourService;
import com.main.traveltour.service.staff.RevenueService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/revenue/")
public class RevenueAPI {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("find-all-revenue")
    public ResponseObject getAllRevenueYear4() {

        List<Map<String, Object>> revenue = revenueService.getRevenueByYear();

        if (revenue.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", revenue);
        }
    }

    @GetMapping("find-all-revenue-by-tourTypeId-and-year")
    public ResponseObject getAllRevenueByTourTypeIdAndYear(
            @RequestParam(required = false) Integer tourTypeId,
            @RequestParam(required = false) Integer year
    ) {
        year = (year == null) ? Year.now().getValue() : year;

        List<Integer> revenue = revenueService.countCompletedToursByYearAndTourType(tourTypeId, year);
        if (revenue.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", revenue);
        }
    }

    @GetMapping("get-all-year")
    public ResponseObject getAllYear() {

        List<Integer> getAllYear = revenueService.getAllYear();
        if (getAllYear.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", getAllYear);
        }
    }

}
