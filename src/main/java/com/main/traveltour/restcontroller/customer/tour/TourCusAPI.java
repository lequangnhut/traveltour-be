package com.main.traveltour.restcontroller.customer.tour;

import com.main.traveltour.dto.staff.tour.TourDetailsGetDataDto;
import com.main.traveltour.dto.staff.tour.TourDetailsRatingDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.TourTypes;
import com.main.traveltour.service.customer.UserCommentsService;
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
    @Autowired
    private UserCommentsService userCommentsService;

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
                                                      @RequestParam(required = false) List<String> cleanArrives,
                                                      @RequestParam(required = false) List<String> cleanFrom,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date departure,
                                                      @RequestParam(required = false) String departureArrives,
                                                      @RequestParam(required = false) String departureFrom,
                                                      @RequestParam(required = false) Integer numberOfPeople
    ) {
        Sort sort = Sort.unsorted();
        if (!sortBy.equals("dateCreated")) {
            sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
        }

        Page<TourDetails> tourDetailsPage = tourDetailsService.findTourDetailWithFilter(departureArrives, departureFrom, numberOfPeople, departure, price, tourTypeList, PageRequest.of(page, size, sort));

//        if (tourDetailsPage.isEmpty()) {
//            List<String> tourDetailIdList = new ArrayList<>();
//            if (cleanArrives != null) {
//                List<String> combinations = generateCombinations(cleanArrives);
//                for (String combination : combinations) {
//                    List<String> ids = tourDetailsService.findTourDetailIdList(combination, null, tourDetailIdList.isEmpty() ? null : tourDetailIdList);
//                    if (!ids.isEmpty()) {
//                        tourDetailIdList.addAll(ids);
//                    }
//                }
//            }
//            if (cleanFrom != null) {
//                List<String> combinations = generateCombinations(cleanFrom);
//                for (String combination : combinations) {
//                    List<String> ids = tourDetailsService.findTourDetailIdList(combination, null, tourDetailIdList.isEmpty() ? null : tourDetailIdList);
//                    if (!ids.isEmpty()) {
//                        tourDetailIdList.addAll(ids);
//                    }
//                }
//            }
//            if (tourDetailIdList.isEmpty()) {
//                tourDetailIdList = null;
//            }
//            tourDetailsPage = tourDetailsService.findTourDetailWithInFilter(tourDetailIdList, numberOfPeople, departure, price, tourTypeList, PageRequest.of(page, size, sort));
//        }

        Page<TourDetailsRatingDto> tourDetailsDtoPage = tourDetailsPage.map(tourDetails -> EntityDtoUtils.convertToDto(tourDetails, TourDetailsRatingDto.class));

        if (tourDetailsDtoPage.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            tourDetailsDtoPage.getContent().forEach(tour -> tour.setRate(userCommentsService.findScoreRatingByRoomTypeId(tour.getTourId())));
            tourDetailsDtoPage.forEach(tour -> tour.setCountRating(userCommentsService.findCountRatingByRoomTypeId(tour.getTourId())));
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetailsDtoPage);
        }
    }

    private static List<String> generateCombinations(List<String> input) {
        List<String> combinations = new ArrayList<>();
        generateCombinationsRecursive(input, 0, "", combinations);
        return combinations;
    }

    private static void generateCombinationsRecursive(List<String> input, int index, String current, List<String> combinations) {
        if (index == input.size()) {
            if (!current.isEmpty()) {
                combinations.add(current.trim());
            }
            return;
        }
        generateCombinationsRecursive(input, index + 1, current + input.get(index) + " ", combinations);
        generateCombinationsRecursive(input, index + 1, current, combinations);
    }

    @GetMapping("customer/tour/getAListOfPopularTours")
    private ResponseObject getAListOfPopularTours(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String departureArrives,
            @RequestParam(required = false) String departureFrom,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date departure
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        List<Integer> listOfIdsOfTourTypes = null;
        if (departureArrives != null) {
            listOfIdsOfTourTypes = tourDetailsService.getListOfIdsOfTourTypes(departureArrives);
            if (listOfIdsOfTourTypes.isEmpty()) {
                listOfIdsOfTourTypes = null;
            }
        }

        List<TourDetails> tourDetailsList = tourDetailsService.getAListOfPopularTours(departureArrives, departureFrom, departure, price, null);

        List<TourDetails> tourDetailsList1 = tourDetailsService.getAListOfPopularTours(departureArrives, null, departure, price, listOfIdsOfTourTypes);
        tourDetailsList1.removeAll(tourDetailsList);

        List<TourDetails> tourDetailsList2 = tourDetailsService.getAListOfPopularTours(null, departureFrom, departure, price, listOfIdsOfTourTypes);
        tourDetailsList2.removeAll(tourDetailsList);
        tourDetailsList2.removeAll(tourDetailsList1);

        List<TourDetails> tourDetailsList3 = tourDetailsService.getAListOfPopularTours(null, null, departure, price, listOfIdsOfTourTypes);
        tourDetailsList3.removeAll(tourDetailsList);
        tourDetailsList3.removeAll(tourDetailsList1);
        tourDetailsList3.removeAll(tourDetailsList2);

        List<TourDetails> tourDetailsList4 = tourDetailsService.getAListOfPopularTours(null, null, departure, null, null);
        tourDetailsList4.removeAll(tourDetailsList);
        tourDetailsList4.removeAll(tourDetailsList1);
        tourDetailsList4.removeAll(tourDetailsList2);
        tourDetailsList4.removeAll(tourDetailsList3);

        List<TourDetails> tourDetailsList5 = tourDetailsService.getAListOfPopularTours(null, null, null, price, null);
        tourDetailsList5.removeAll(tourDetailsList);
        tourDetailsList5.removeAll(tourDetailsList1);
        tourDetailsList5.removeAll(tourDetailsList2);
        tourDetailsList5.removeAll(tourDetailsList3);
        tourDetailsList5.removeAll(tourDetailsList3);

        Set<TourDetails> filteredSet = new LinkedHashSet<>(tourDetailsList);
        filteredSet.addAll(tourDetailsList1);
        filteredSet.addAll(tourDetailsList2);
        filteredSet.addAll(tourDetailsList3);
        filteredSet.addAll(tourDetailsList4);
        filteredSet.addAll(tourDetailsList5);

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

        Page<TourDetailsRatingDto> tourDetailsDtoPage = tourDetailsPage.map(
                tourDetails -> EntityDtoUtils.convertToDto(tourDetails, TourDetailsRatingDto.class));

        if (tourDetailsDtoPage.isEmpty()) {
            return new ResponseObject("204", "Không tìm thấy dữ liệu", null);
        } else {
            tourDetailsDtoPage.getContent().forEach(tour -> tour.setRate(userCommentsService.findScoreRatingByRoomTypeId(tour.getTourId())));
            tourDetailsDtoPage.forEach(tour -> tour.setCountRating(userCommentsService.findCountRatingByRoomTypeId(tour.getTourId())));
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
