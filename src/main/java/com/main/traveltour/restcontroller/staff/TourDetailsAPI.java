package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.TourDetailsDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/tour-detail")
public class TourDetailsAPI {

    @Autowired
    private TourDetailsService tourDetailsService;

    @GetMapping("/find-all-tourDetail")
    private ResponseObject findAllTourDetail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<TourDetails> items = searchTerm == null || searchTerm.isEmpty()
                ? tourDetailsService.findAll(PageRequest.of(page, size, sort))
                : tourDetailsService.findAllWithSearch(searchTerm, PageRequest.of(page, size, sort));

        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }


    @GetMapping("/find-by-id/{id}")
    public ResponseObject findById(@PathVariable String id) {
        Optional<TourDetails> tourDetail = tourDetailsService.findById(id);
        if (tourDetail.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourDetail);
        }
    }

    @PostMapping("/create-tourDetail")
    public ResponseObject createTourDetail(@RequestPart TourDetailsDto tourDetailsDto) {
        try {
            String tourDetailId = GenerateNextID.generateNextCode("TR", tourDetailsService.getMaxCodeTourDetailId());
            TourDetails tourDetail = EntityDtoUtils.convertToEntity(tourDetailsDto, TourDetails.class);
            tourDetail.setId(tourDetailId);
            tourDetail = tourDetailsService.save(tourDetail);
            return new ResponseObject("200", "Thêm mới thành công", tourDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Thêm mới thất bại", null);
        }

    }

    @PutMapping("/update-tourDetail/{id}")
    public ResponseObject updateTourDetail(@PathVariable String id, @RequestPart TourDetailsDto tourDetailsDto) {
        try {
            tourDetailsDto.setId(id);
            TourDetails tourDetails = EntityDtoUtils.convertToEntity(tourDetailsDto, TourDetails.class);
            TourDetails updatedTourDetail = tourDetailsService.save(tourDetails);
            return new ResponseObject("200", "Cập nhật thành công", updatedTourDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Cập nhật thất bại", null);
        }
    }

    @DeleteMapping("/delete-tourDetail/{id}")
    public ResponseObject deleteTourDetail(@PathVariable String id) {
        try {
            TourDetails tourDetails = tourDetailsService.getById(id);
            tourDetailsService.delete(tourDetails);
            System.out.println("TourDetail xóa đc rồi");
            return new ResponseObject("204", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xó    a thất bại", null);
        }
    }
}
