package com.main.traveltour.restcontroller.guide;

import com.main.traveltour.dto.admin.post.HotelsDto;
import com.main.traveltour.dto.guide.TourDetailsDtoGuide;
import com.main.traveltour.dto.guide.ToursDto;
import com.main.traveltour.dto.staff.OrderVisitDetailsGetDataDto;
import com.main.traveltour.dto.staff.TransportationSchedulesDto;
import com.main.traveltour.dto.staff.tour.TourDetailsGetDataDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.staff.*;
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
@RequestMapping("api/v1")
public class GuideController {

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private ToursService toursService;

    @Autowired
    private BookingTourHotelService bookingTourHotelService;

    @Autowired
    private BookingTourCustomerService bookingTourCustomerService;

    @Autowired
    private BookingTourTransportationService bookingTourTransportationService;

    @Autowired
    private BookingTourVisitService bookingTourVisitService;


    @GetMapping("/guide/tour-of-guide")
    private ResponseObject findAllTourWaitingOfGuide(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "12") int size,
                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                     @RequestParam(defaultValue = "asc") String sortDir,
                                                     @RequestParam(required = false) String searchTerm,
                                                     @RequestParam(required = false) Integer guideId,
                                                     @RequestParam(required = false) Integer tourStatus) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TourDetails> items = tourDetailsService.findTourGuide(guideId, tourStatus, searchTerm, PageRequest.of(page, size, sort));

        Page<TourDetailsDtoGuide> tourDetailsDtoGuides = items.map(tourDetails -> EntityDtoUtils.convertToDto(tourDetails, TourDetailsDtoGuide.class));

        if (tourDetailsDtoGuides.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoGuides);
        }
    }

    @GetMapping("/guide/find-by-id")
    public ResponseObject findId(@RequestParam(required = false) String tourTypeId,
                                 @RequestParam(required = false) String tourDetailId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Tours> findType = toursService.findById(tourTypeId);
        ToursDto toursDto = EntityDtoUtils.convertOptionalToDto(findType, ToursDto.class);
//
        List<BookingTourCustomers> bookingCustomerList = bookingTourCustomerService.findByTourDetailId(tourDetailId);

        response.put("toursDto", toursDto);
        response.put("bookingCustomerList", bookingCustomerList);

        return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
    }

    @GetMapping("/guide/find-all-order-hotel-for-tour")
    public ResponseObject getAllBookingTourHotel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String tourDetailId,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<Hotels> orderHotels = bookingTourHotelService.findHotelByTourDetailIdForGuide(tourDetailId, PageRequest.of(page, size, sort), searchTerm);

        if (orderHotels.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderHotels);
        }
    }

    @GetMapping("/guide/find-all-order-transportation-for-tour")
    public ResponseObject getAllBookingTourTrans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String tourDetailId,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<TransportationSchedules> transportationSchedules = bookingTourTransportationService.findTransportationSchedulesByTourDetailIdForGuide(tourDetailId, searchTerm, PageRequest.of(page, size, sort));

        Page<TransportationSchedulesDto> transportationSchedulesDtos = transportationSchedules.map(transportationSchedule -> EntityDtoUtils.convertToDto(transportationSchedule, TransportationSchedulesDto.class));

        if (transportationSchedulesDtos.isEmpty()) {
            return new ResponseObject("500", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedulesDtos);
        }
    }

    @GetMapping("/guide/find-all-order-visit-for-tour")
    public ResponseObject getAllBookingTourVisit(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String tourDetailId,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<VisitLocations> visitLocations = bookingTourVisitService.findVisitByTourDetailIdGuide(tourDetailId, searchTerm, PageRequest.of(page, size, sort));

        if (visitLocations.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocations);
        }
    }

    @GetMapping("/guide/find-all-booking-tour-visit-by-tour-detail-id-and-visit-id-for-guide")
    public ResponseObject getAllBookingTourVisit(
            @RequestParam(required = false) String tourDetailId, @RequestParam(required = false) String visitId) {

        List<OrderVisitDetails> orderVisitDetails = bookingTourVisitService.findOrderVisitDetailByTourDetailIdAndVisitIdGuide(tourDetailId, visitId);
        List<OrderVisitDetailsGetDataDto> orderVisitDetailsGetDataDtos = orderVisitDetails.stream()
                .map(orderVisitDetail -> EntityDtoUtils.convertToDto(orderVisitDetail, OrderVisitDetailsGetDataDto.class))
                .toList();
        if (orderVisitDetails.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderVisitDetailsGetDataDtos);
        }
    }

}
