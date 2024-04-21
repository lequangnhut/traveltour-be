package com.main.traveltour.restcontroller.agent.hotel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.traveltour.dto.agent.hotel.RoomTypeAddDto;
import com.main.traveltour.dto.agent.hotel.RoomTypeDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.RoomBedsServiceAD;
import com.main.traveltour.service.agent.RoomImageService;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.agent.RoomUtilitiesService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.service.utils.FileUploadResize;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class RoomTypeAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private FileUploadResize fileUploadResize;


    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomImageService roomImageService;

    @Autowired
    private RoomBedsServiceAD roomBedsServiceAD;

    @Autowired
    private RoomUtilitiesService roomUtilitiesService;

    /**
     * Phương thức lấy toàn bộ thông tin loại phòng phân trang và sắp xếp
     *
     * @param page       trang
     * @param size       kích thước
     * @param sortBy     sắp xếp
     * @param sortDir    trường sắp xếp
     * @param searchTerm thông tin tìm kiếm
     * @param hotelId    mã khách sạn
     * @param isDeleted  trang thái xóa
     * @return kết quả
     */
    @GetMapping("agent/room-type/get-room-type")
    public ResponseObject getRoomType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String searchTerm,
            @RequestParam("hotelId") String hotelId,
            @RequestParam("isDelete") Boolean isDeleted
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<RoomTypes> items = searchTerm == null || searchTerm.isEmpty()
                ? roomTypeService.findAllByHotelIdAndIsDelete(hotelId, isDeleted, PageRequest.of(page, size, sort))
                : roomTypeService.findAllWithSearchAndHotelId(searchTerm, hotelId, PageRequest.of(page, size, sort));

        return new ResponseObject("200", "OK", items);
    }

    /**
     * Phương thức tìm phòng bằng mã phòng
     *
     * @param roomTypeId mã phòng
     * @return kết quả
     */
    @GetMapping("agent/room-type/get-room-type-by-id")
    public ResponseObject getRoomTypeById(
            @RequestParam("roomTypeId") String roomTypeId
    ) {
        Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(roomTypeId);
        if (roomTypes.isPresent()) {
            return new ResponseObject("200", "OK", roomTypes.get());
        } else {
            return new ResponseObject("404", "Null", null);
        }
    }

    /**
     * Phương thức lưu thông tin phòng
     *
     * @param roomTypesAddDto        thông tin phòng
     * @param roomTypeAvatarData     Ảnh đại diện phòng
     * @param listRoomTypeImg        Danh sách hình ảnh khách sạn
     * @param selectedCheckboxValues Danh sách dịch vụ khách sạn
     * @return kết quả
     */
    @PostMapping("agent/room-type/saveRoomType")
    public ResponseObject saveRoomType(
            @RequestPart("roomTypes") RoomTypeAddDto roomTypesAddDto,
            @RequestPart("roomTypeAvatarData") MultipartFile roomTypeAvatarData,
            @RequestPart("listRoomTypeImg") List<MultipartFile> listRoomTypeImg,
            @RequestPart("selectedCheckboxValues") List<Integer> selectedCheckboxValues,
            @RequestParam("checkinTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkinTime,
            @RequestParam("checkoutTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkoutTime
    ) {
        try {
            RoomTypes roomTypes = null;
            RoomBeds roomBeds = new RoomBeds();

            String roomTypeId = GenerateNextID.generateNextCode("RT", roomTypeService.findMaxId());

            String roomTypeAvatar = fileUploadResize.uploadFileResize(roomTypeAvatarData);

            roomTypesAddDto.setId(roomTypeId);
            roomTypesAddDto.setIsActive(1);
            roomTypesAddDto.setIsDeleted(false);
            roomTypesAddDto.setRoomTypeAvatar(roomTypeAvatar);

            roomTypes = EntityDtoUtils.convertToEntity(roomTypesAddDto, RoomTypes.class);

            roomTypes.setCheckinTime(checkinTime);
            roomTypes.setCheckoutTime(checkoutTime);
            List<RoomUtilities> roomUtilitiesList = selectedCheckboxValues.stream()
                    .map(roomUtilitiesService::findByRoomUtilitiesId)
                    .collect(Collectors.toList());

            roomTypes.setRoomUtilities(roomUtilitiesList);

            roomTypeService.save(roomTypes);

            // Thêm danh sách hỉnh ảnh cho phòng khách sạn
            for (MultipartFile roomTypeImage : listRoomTypeImg) {
                RoomImages roomImages = new RoomImages(); // Tạo một đối tượng RoomImages mới trong mỗi vòng lặp
                roomImages.setRoomTypeId(roomTypeId);
                roomImages.setRoomTypeImg(fileUpload.uploadFile(roomTypeImage));
                roomImageService.save(roomImages); // Lưu đối tượng RoomImages mới
            }


            // Thêm loại giường cho phòng khách sạn
            roomBeds.setRoomTypeId(roomTypeId);
            roomBeds.setBedTypeId(roomTypesAddDto.getBedTypeId());
            roomBedsServiceAD.save(roomBeds);
            return new ResponseObject("200", "Thêm phòng thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Lỗi khi thêm thông tin vui lòng kiểm tra lại", null);
        }
    }

    /**
     * Phương thức tìm kiếm loại phòng dựa vào mã phòng
     *
     * @param id mã phòng
     * @return kêt quả
     */
    @GetMapping("agent/room-type/findRoomTypeByRoomId")
    public ResponseObject findRoomTypeByRoomId(
            @RequestParam("id") String id
    ) {
        Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(id);

        if (roomTypes.isPresent()) {
            return new ResponseObject("200", "OK", roomTypes.get());
        } else {
            return new ResponseObject("404", "Null", null);
        }

    }


    /**
     * Phương thức sửa thông tin của phòng
     *
     * @param roomTypesAddDto thông tin phòng
     * @return kết quả
     */
    @PutMapping("agent/room-type/editInfoRoomType")
    public ResponseObject editInfoRoomType(
            @RequestPart("roomTypes") RoomTypeAddDto roomTypesAddDto,
            @RequestParam("checkinTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkinTime,
            @RequestParam("checkoutTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkoutTime
    ) {
        Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(roomTypesAddDto.getId());
        RoomBeds roomBeds = roomBedsServiceAD.findRoomBedsRoomTypeId(roomTypesAddDto.getId());

        roomTypes.get().setAmountRoom(roomTypesAddDto.getAmountRoom());
        roomTypes.get().setRoomTypeName(roomTypesAddDto.getRoomTypeName());
        roomTypes.get().setCapacityAdults(roomTypesAddDto.getCapacityAdults());
        roomTypes.get().setCapacityChildren(roomTypesAddDto.getCapacityChildren());
        roomTypes.get().setRoomTypeDescription(roomTypesAddDto.getRoomTypeDescription());
        roomTypes.get().setBreakfastIncluded(roomTypesAddDto.getBreakfastIncluded());
        roomTypes.get().setFreeCancellation(roomTypesAddDto.getFreeCancellation());
        roomTypes.get().setCheckinTime(checkinTime);
        roomTypes.get().setCheckoutTime(checkoutTime);
        roomTypes.get().setIsActive(1);

        roomTypeService.save(roomTypes.get());

        // Sửa loại giường cho phòng khách sạn
        if (roomBeds == null) {
            roomBeds = new RoomBeds();
            roomBeds.setRoomTypeId(roomTypesAddDto.getId());
            roomBeds.setBedTypeId(roomTypesAddDto.getBedTypeId());
            roomBedsServiceAD.save(roomBeds);
        } else if (!roomBeds.getBedTypeId().equals(roomTypesAddDto.getBedTypeId())) {
            roomBeds.setRoomTypeId(roomTypesAddDto.getId());
            roomBeds.setBedTypeId(roomTypesAddDto.getBedTypeId());
            roomBedsServiceAD.save(roomBeds);
        }
        return new ResponseObject("200", "Thêm phòng thành công", null);
    }

    /**
     * Phương thức cập nhật ảnh đại diện phòng
     *
     * @param roomTypeId  mã phòng
     * @param roomTypeImg ảnh đại diện phòng
     * @return kết quả
     */
    @PutMapping("agent/room-type/updateAvatarRoomType")
    public ResponseObject updateAvateRoomType(
            @RequestParam("roomTypeId") String roomTypeId,
            @RequestPart("roomTypeImg") MultipartFile roomTypeImg
    ) {
        try {
            Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(roomTypeId);
            if (roomTypes.isPresent()) {
                RoomTypes roomType = roomTypes.get();
                roomType.setRoomTypeAvatar(fileUploadResize.uploadFileResizeAndReducedQuality(roomTypeImg));
                roomTypeService.save(roomType);
                return new ResponseObject("200", "Thay đổi ảnh đại diện thành công", roomType);
            } else {
                return new ResponseObject("404", "Không tìm thấy phòng hiện tại!", null);
            }
        } catch (Exception e) {
            return new ResponseObject("500", "Đã xảy ra lỗi khi thực hiện thay đổi ảnh đại diện", null);
        }
    }

    /**
     * Phương thức tìm phòng bằng mã phòng
     *
     * @param roomTypeId mã phòng
     * @return kết quả
     */
    @GetMapping("agent/room-type/findRoomTypesById")
    public ResponseObject findRoomTypesById(
            @RequestParam("roomTypeId") String roomTypeId
    ) {
        Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(roomTypeId);
        RoomTypeDto roomTypeDto = EntityDtoUtils.convertToDto(roomTypes.get(), RoomTypeDto.class);
        if (roomTypes.isPresent()) {
            return new ResponseObject("200", "OK", roomTypeDto);
        } else {
            return new ResponseObject("404", "Null", null);
        }
    }


    @DeleteMapping("agent/room-type/deleteAllRoomTypeByIds")
    public ResponseEntity deleteAllRoomTypeById(
            @RequestParam(required = false) List<String> roomTypeIds
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            roomTypeService.deleteAllRoomTypeByIds(roomTypeIds);
            return ResponseEntity.ok(objectMapper.writeValueAsString(Collections.singletonMap("message", "Xóa phòng thành công!")));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(Collections.singletonMap("message", "Có lỗi xảy ra vui lòng thử lại sau!")));
        }
    }

    @DeleteMapping("agent/room-type/restoreAllRoomTypeByIds")
    public ResponseEntity restoreAllRoomTypeById(
            @RequestParam(required = false) List<String> roomTypeIds
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            roomTypeService.restoreAllRoomTypeByIds(roomTypeIds);
            return ResponseEntity.ok(objectMapper.writeValueAsString(Collections.singletonMap("message", "Khôi phục phòng thành công!")));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(Collections.singletonMap("message", "Có lỗi xảy ra vui lòng thử lại sau!")));
        }
    }
}
