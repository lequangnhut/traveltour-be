package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.RoomTypeAddDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.BedTypesServiceAD;
import com.main.traveltour.service.admin.RoomBedsServiceAD;
import com.main.traveltour.service.agent.RoomImageService;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.agent.RoomUtilitiesService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.service.utils.FileUploadResize;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import com.main.traveltour.utils.UploadFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    @GetMapping("agent/room-type/get-room-type")
    public ResponseObject getRoomType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String searchTerm,
            @RequestParam("hotelId") String hotelId,
            @RequestParam("isDelete") Boolean isDelete
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Sử dụng phương thức tìm kiếm mới trong service
        Page<RoomTypes> items = searchTerm == null || searchTerm.isEmpty()
                ? roomTypeService.findAllByHotelIdAndIsDelete(hotelId, isDelete, PageRequest.of(page, size, sort))
                : roomTypeService.findAllWithSearchAndHotelId(searchTerm, hotelId, PageRequest.of(page, size, sort));

        System.out.println(page + size + sortBy + sortDir + searchTerm + hotelId);

        return new ResponseObject("200", "OK", items);
    }

    @GetMapping("agent/room-type/get-room-type-by-id")
    public ResponseObject getRoomTypeById(
            @RequestParam("roomTypeId") String roomTypeId
    ) {
        Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(roomTypeId);

        if(roomTypes.isPresent()){
            return new ResponseObject("200", "OK",  roomTypes.get());
        }else{
            return new ResponseObject("404", "Null", null);
        }

    }

    @PostMapping("agent/room-type/saveRoomType")
    public ResponseObject saveRoomType(
            @RequestPart("roomTypes") RoomTypeAddDto roomTypesAddDto,
            @RequestPart("roomTypeAvatarData") MultipartFile roomTypeAvatarData,
            @RequestPart("listRoomTypeImg") List<MultipartFile> listRoomTypeImg,
            @RequestPart("selectedCheckboxValues") List<Integer> selectedCheckboxValues
    ) {
        try{
            RoomTypes roomTypes = null;
            RoomBeds roomBeds = new RoomBeds();
            List<String> listRoomTypesImage = new ArrayList<String>();

            String roomTypeId = GenerateNextID.generateNextCode("RT", roomTypeService.findMaxId());

            String roomTypeAvatar = fileUploadResize.uploadFileResize(roomTypeAvatarData);

            roomTypesAddDto.setId(roomTypeId);
            roomTypesAddDto.setIsDeleted(false);
            roomTypesAddDto.setRoomTypeAvatar(roomTypeAvatar);

            roomTypes = EntityDtoUtils.convertToEntity(roomTypesAddDto, RoomTypes.class);

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
        }catch (Exception e){
            return new ResponseObject("500", "Lỗi khi thêm thông tin vui lòng kiểm tra lại", null);
        }
    }
}
