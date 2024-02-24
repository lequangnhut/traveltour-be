package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.TourTripsDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourTrips;
import com.main.traveltour.service.staff.TourTripsService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/tour-trips/")
public class TourTripsAPI {

    @Autowired
    private TourTripsService tourTripsService;

    @GetMapping("find-all-tourTrips/{tourId}")
    private ResponseObject findAllTourTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "null") String tourId) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<TourTrips> items = tourTripsService.findTourTripsByTourId(tourId, PageRequest.of(page, size, sort));

        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("find-tourTrips-by-tourId/{tourId}")
    private ResponseObject findTourTripsByTourId(@RequestParam(defaultValue = "null") String tourId) {
        List<TourTrips> items = tourTripsService.findTourTripsByTourId(tourId);
        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("find-by-id/{id}")
    public ResponseObject findById(@PathVariable int id) {
        Optional<TourTrips> tourTrips = tourTripsService.findById(id);
        if (tourTrips.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourTrips);
        }
    }

    @PostMapping("create-tourTrips")
    public ResponseObject createTourTrips(@RequestPart TourTripsDto tourTripsDto) {
        try {
            int dayInTripIsMax = tourTripsService.getDayInTripIsMax(tourTripsDto.getTourDetailId());
            TourTrips tourTrips = EntityDtoUtils.convertToEntity(tourTripsDto, TourTrips.class);
            tourTrips.setDayInTrip(dayInTripIsMax + 1);
            tourTrips = tourTripsService.save(tourTrips);
            return new ResponseObject("200", "Thêm mới thành công", tourTrips);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Thêm mới thất bại", null);
        }

    }

    @PutMapping("update-tourTrips/{id}")
    public ResponseObject updateTourTrips(@PathVariable int id, @RequestPart TourTripsDto tourTripsDto) {
        try {
            tourTripsDto.setId(id);
            TourTrips TourTrips = EntityDtoUtils.convertToEntity(tourTripsDto, TourTrips.class);
            TourTrips updatedTourDetail = tourTripsService.save(TourTrips);
            return new ResponseObject("200", "Cập nhật thành công", updatedTourDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Cập nhật thất bại", null);
        }
    }

    @DeleteMapping("delete-tourTrips/{id}")
    public ResponseObject deleteTourTrips(@PathVariable int id) {
        try {
            TourTrips TourTrips = tourTripsService.getById(id);
            tourTripsService.delete(TourTrips);
            return new ResponseObject("204", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xó    a thất bại", null);
        }
    }
}
