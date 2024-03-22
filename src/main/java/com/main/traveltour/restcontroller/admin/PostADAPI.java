package com.main.traveltour.restcontroller.admin;

import com.main.traveltour.dto.admin.post.*;
import com.main.traveltour.dto.customer.infomation.OrderVisitDetailsDto;
import com.main.traveltour.dto.customer.infomation.OrderVisitsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.HotelsServiceAD;
import com.main.traveltour.service.admin.RoomTypesServiceAD;
import com.main.traveltour.service.admin.TransServiceAD;
import com.main.traveltour.service.admin.VisitLocationsServiceAD;
import com.main.traveltour.service.agent.HotelsService;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/admin/post/")
public class PostADAPI {

    @Autowired
    private HotelsServiceAD hotelsServiceAD;

    @Autowired
    private RoomTypesServiceAD roomTypesServiceAD;

    @Autowired
    private TransServiceAD transServiceAD;

    @Autowired
    private VisitLocationsServiceAD visitLocationsServiceAD;

    @GetMapping("all-hotel-post")
    private ResponseObject findAllHotelPost(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String searchTerm,
                                            @RequestParam(required = false) Boolean isAccepted) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Hotels> items = searchTerm == null || searchTerm.isEmpty()
                ? hotelsServiceAD.findAllHotelPost(isAccepted, PageRequest.of(page, size, sort))
                : hotelsServiceAD.findAllHotelPostByName(isAccepted, PageRequest.of(page, size, sort), searchTerm);

        Page<HotelsDto> hotelsDtos = items.map(hotels -> EntityDtoUtils.convertToDto(hotels, HotelsDto.class));

        if (hotelsDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotelsDtos);
        }
    }

    @GetMapping("find-room-by-hotelId/{hotelId}")
    public ResponseObject findRoomByHotel (@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                 @RequestParam(defaultValue = "asc") String sortDir,
                                                 @RequestParam(required = false) String searchTerm,
                                                 @RequestParam(required = false) Integer isActive,
                                                 @PathVariable String hotelId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<RoomTypes> items = searchTerm == null || searchTerm.isEmpty()
                ? roomTypesServiceAD.findByHotelId(isActive, hotelId, PageRequest.of(page, size, sort))
                : roomTypesServiceAD.findByHotelIdAndName(isActive, hotelId, PageRequest.of(page, size, sort), searchTerm);

        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("all-brand-post")
    private ResponseObject findAllBrandPost(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String searchTerm,
                                            @RequestParam(required = false) Boolean isAccepted) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TransportationBrands> items = searchTerm == null || searchTerm.isEmpty()
                ? transServiceAD.findAllBrandPost(isAccepted, PageRequest.of(page, size, sort))
                : transServiceAD.findAllBrandPostByName(isAccepted, PageRequest.of(page, size, sort), searchTerm);

        Page<TransportationBrandsDto> transportationBrandsDtos = items.map(transportationBrands -> EntityDtoUtils.convertToDto(transportationBrands, TransportationBrandsDto.class));

        if (transportationBrandsDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationBrandsDtos);
        }
    }

    @GetMapping("find-trans-by-brandId/{brandId}")
    public ResponseObject findTransByBrandId (@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDir,
                                           @RequestParam(required = false) String searchTerm,
                                           @RequestParam(required = false) Boolean isActive,
                                           @PathVariable String brandId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<Transportations> items = searchTerm == null || searchTerm.isEmpty()
                ? transServiceAD.findAllTransPost(isActive, brandId, PageRequest.of(page, size, sort))
                : transServiceAD.findAllTransPostByName(isActive, brandId, PageRequest.of(page, size, sort), searchTerm);

        Page<TransportationsDto> transportationsDtos = items.map(transportations -> EntityDtoUtils.convertToDto(transportations, TransportationsDto.class));

        if (transportationsDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationsDtos);
        }
    }

    @GetMapping("find-trip-by-transId/{transId}")
    public ResponseObject findTripByTransId (@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "asc") String sortDir,
                                              @RequestParam(required = false) String searchTerm,
                                              @RequestParam(required = false) Boolean isActive,
                                              @PathVariable String transId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TransportationSchedules> items = searchTerm == null || searchTerm.isEmpty()
                ? transServiceAD.findAllSchedulesPost(isActive, transId, PageRequest.of(page, size, sort))
                : transServiceAD.findAllSchedulesPostByName(isActive, transId, PageRequest.of(page, size, sort), searchTerm);

        Page<TransportationSchedulesDto> transportationSchedulesDtos = items.map(transportationSchedules -> EntityDtoUtils.convertToDto(transportationSchedules, TransportationSchedulesDto.class));

        if (transportationSchedulesDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedulesDtos);
        }
    }

    @GetMapping("all-visit-post")
    private ResponseObject findAllVisitPost(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String searchTerm,
                                            @RequestParam(required = false) Boolean isAccepted) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<VisitLocations> items = searchTerm == null || searchTerm.isEmpty()
                ? visitLocationsServiceAD.findAllVisitPost(isAccepted, PageRequest.of(page, size, sort))
                : visitLocationsServiceAD.findAllVisitPostByName(isAccepted, PageRequest.of(page, size, sort), searchTerm);

        Page<VisitLocationsDto> visitLocationsDtos = items.map(visitLocations -> EntityDtoUtils.convertToDto(visitLocations, VisitLocationsDto.class));

        if (visitLocationsDtos.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationsDtos);
        }
    }
}
