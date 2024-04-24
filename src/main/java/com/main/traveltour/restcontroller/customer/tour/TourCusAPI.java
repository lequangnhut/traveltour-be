package com.main.traveltour.restcontroller.customer.tour;

import com.main.traveltour.dto.staff.tour.TourDetailsGetDataDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.TourTypes;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.staff.TourTypesService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class TourCusAPI {

    @Autowired
    private TourTypesService tourTypesService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @GetMapping("customer/tour/find-tour-detail-customer")
    private ResponseObject findAllTourDetail(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {

        Page<TourDetails> tourDetailsPage = tourDetailsService.findAll(PageRequest.of(page, size));
        Page<TourDetailsGetDataDto> tourDetailsDtoPage = tourDetailsPage.map(tourDetails -> EntityDtoUtils.convertToDto(tourDetails, TourDetailsGetDataDto.class));

        if (tourDetailsDtoPage.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoPage);
        }
    }

    @GetMapping("customer/tour/find-tour-detail-customer-by-filters")
    private ResponseObject findAllTourDetailByFilters(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String sortDir,
                                                      @RequestParam(required = false) BigDecimal price,
                                                      @RequestParam(required = false) List<Integer> tourTypeList,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date departure,
                                                      @RequestParam(required = false) String departureArrives,
                                                      @RequestParam(required = false) String departureFrom,
                                                      @RequestParam(required = false) Integer numberOfPeople
    ) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<TourDetails> tourDetailsPage = tourDetailsService.findTourDetailWithFilter(departureArrives, departureFrom, numberOfPeople, departure, price, tourTypeList, PageRequest.of(page, size, sort));
        Page<TourDetailsGetDataDto> tourDetailsDtoPage = tourDetailsPage.map(tourDetails -> EntityDtoUtils.convertToDto(tourDetails, TourDetailsGetDataDto.class));

        if (tourDetailsDtoPage.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoPage);
        }
    }

    @GetMapping("customer/tour/getAListOfPopularTours")
    private ResponseObject getAListOfPopularTours(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date departure
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        List<TourDetails> tourDetailsList = tourDetailsService.getAListOfPopularTours(departure, price);

        List<TourDetails> tourDetailsList2 = tourDetailsService.getAListOfPopularTours(departure, null);
        tourDetailsList2.removeAll(tourDetailsList);

        List<TourDetails> tourDetailsList3 = tourDetailsService.getAListOfPopularTours(null, null);
        tourDetailsList3.removeAll(tourDetailsList);
        tourDetailsList3.removeAll(tourDetailsList2);

        Set<TourDetails> filteredSet = new LinkedHashSet<>(tourDetailsList);
        filteredSet.addAll(tourDetailsList2);
        filteredSet.addAll(tourDetailsList3);

        List<TourDetails> filteredList = new ArrayList<>(filteredSet);

        Comparator<TourDetails> priceComparator = Comparator.comparing(TourDetails::getUnitPrice);
        if (sortBy.equals("unitPrice")) {
            if (sortDir.equals("asc")) {
                filteredList.sort(priceComparator);
            } else {
                filteredList.sort(priceComparator.reversed());
            }
        }

        int totalElements = filteredList.size();
        int start = Math.min((int) page * size, totalElements);
        int end = Math.min(start + size, totalElements);
        List<TourDetails> pageContent = filteredList.subList(start, end);
        Page<TourDetails> tourDetailsPage = new PageImpl<>(pageContent, PageRequest.of(page, size, sort), totalElements);

        Page<TourDetailsGetDataDto> tourDetailsDtoPage = tourDetailsPage.map(
                tourDetails -> EntityDtoUtils.convertToDto(tourDetails, TourDetailsGetDataDto.class));

        if (tourDetailsDtoPage.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoPage);
        }
    }

    @GetMapping("customer/tour/get-tour-detail-customer-data-list")
    private ResponseObject getAllDataList() {
        List<TourDetails> tourDetailsList = tourDetailsService.findAll();
        Set<String> dataSet = new LinkedHashSet<>();
        for (TourDetails tourDetails : tourDetailsList) {
            dataSet.add(tourDetails.getToursByTourId().getTourName());
            dataSet.add(tourDetails.getToursByTourId().getTourTypesByTourTypeId().getTourTypeName());
            dataSet.add(tourDetails.getUsersByGuideId().getFullName());
            dataSet.add(tourDetails.getToLocation());
            dataSet.add(tourDetails.getFromLocation());
        }
        List<String> uniqueDataList = new ArrayList<>(dataSet);

        if (uniqueDataList.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", uniqueDataList);
        }
    }

    @GetMapping("customer/tour/get-tour-detail-home-data-list")
    private ResponseObject getAllHomeDataList() {
        Map<String, Object> response = new HashMap<>();
        List<TourDetails> tourDetailsList = tourDetailsService.findAllOrderByBookingCountDesc();

        Set<Map<String, Object>> tourDataList = new LinkedHashSet<>();
        Set<String> departureFromSet = new LinkedHashSet<>();

        for (TourDetails tourDetails : tourDetailsList) {
            Map<String, Object> tourData = new HashMap<>();
            tourData.put("tourName", tourDetails.getToursByTourId().getTourName());
            tourData.put("toLocation", tourDetails.getToLocation());
            tourData.put("image", tourDetails.getToursByTourId().getTourImg());
            tourData.put("price", tourDetails.getUnitPrice());
            tourDataList.add(tourData);

            departureFromSet.add(tourDetails.getFromLocation());
        }

        List<Map<String, Object>> departureArrives = new ArrayList<>(tourDataList);
        response.put("departureArrives", departureArrives);

        List<String> departureFrom = new ArrayList<>(departureFromSet);
        response.put("departureFrom", departureFrom);

        if (tourDetailsList.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
        }
    }


    @GetMapping("customer/tour/find-all-tour-trend")
    private ResponseObject findAllTourTrend() {
        List<Object[]> tourTrend = tourDetailsService.findTourTrend();

        if (tourTrend.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourTrend);
        }
    }

    @GetMapping("customer/tour/find-all-tour-type")
    private ResponseObject findAllTourType() {
        List<TourTypes> tourTypes = tourTypesService.findAll();

        if (tourTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourTypes);
        }
    }

    @GetMapping("customer/tour/find-by-tour-detail-id/{tourDetailId}")
    private ResponseObject findByTourDetailId(@PathVariable String tourDetailId) {
        Optional<TourDetails> tourDetails = Optional.ofNullable(tourDetailsService.findById(tourDetailId));
        TourDetailsGetDataDto tourDetailsDto = EntityDtoUtils.convertOptionalToDto(tourDetails, TourDetailsGetDataDto.class);

        if (tourDetails.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDto);
        }
    }
}
