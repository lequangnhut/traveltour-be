package com.main.traveltour.restcontroller.agent.hotel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.traveltour.dto.agent.hotel.*;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.admin.RoomBedsServiceAD;
import com.main.traveltour.service.agent.*;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class HotelsAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private BedTypeService bedTypeService;

    @Autowired
    private RoomBedsServiceAD roomBedsService;

    @Autowired
    private HotelsService hotelsService;

    @Autowired
    private HotelsTypeService hotelsTypeService;

    @Autowired
    private RoomUtilitiesService roomUtilitiesService;

    @Autowired
    private PlaceUtilitiesService placeUtilitiesService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomImageService roomImageService;

    /**
     * Phương thức tìm kiếm khách sạn dựa vào id của đại lý
     *
     * @param agencyId mã đại lý
     * @return danh sách khách sạn
     */
    @GetMapping("/agent/hotels/find-all-by-agency-id/{agencyId}")
    private List<Hotels> findAllByAgencyId(@PathVariable int agencyId) {
        return hotelsService.findAllByAgencyId(agencyId);
    }

    /**
     * Phương thức tìm kiếm khách sạn dựa vào id của đại lý
     * @param agencyId mã đại lý
     * @return danh sách khách sạn
     */
    @GetMapping("/agent/hotels/find-by-agency-id/{agencyId}")
    private Hotels findByAgencyId(@PathVariable int agencyId) {
        return hotelsService.findByAgencyId(agencyId);
    }

    /**
     * Phương thức tìm kiếm danh sách khách sạn
     * @return danh sách khách sạn
     */
    @GetMapping("/agent/hotels/list-hotels")
    private ResponseObject findAllHotels() {
        List<Hotels> hotels = hotelsService.findAllListHotel();

        if (hotels.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotels);
        }
    }

    /**
     * Phương thức tìm kiếm khách sạn theo mã đối tác và trạng thái xóa
     * @param agencyId mã đối tác
     * @return danh sách khách sạn
     */
    @GetMapping("/agent/hotels/findAllByAgencyIdAndStatusDelete/{agencyId}")
    private ResponseEntity<List<Hotels>> findAllByAgencyIdAndStatusDelete(@PathVariable int agencyId) throws JsonProcessingException {
        List<Hotels> hotels = hotelsService.findHotelsByAgenciesIdAndIsDeleted(agencyId);
        return ResponseEntity.ok(hotels);
    }

    /**
     * Phương thức tìm kiếm loại khách sạn
     *
     * @return danh sách loại khách sạn
     */
    @GetMapping("/agent/hotels/list-hotels-type")
    private ResponseObject findAllHotelsTYpe() {
        List<HotelTypes> hotelType = hotelsTypeService.findAllHotelType();

        if (hotelType.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotelType);
        }
    }

    /**
     * Phương thức tìm kiếm dịch vụ khách sạn
     *
     * @return danh sách dịch vụ khách sạn
     */
    @GetMapping("agent/hotels/list-place-utilities")
    private ResponseObject findAllPlaceUtilities() {
        List<PlaceUtilities> listPlaceUtilities = placeUtilitiesService.findAll();

        if (listPlaceUtilities.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", listPlaceUtilities);
        }
    }

    /**
     * Phương thức tìm kiếm loại phòng dựa vào các tiêu chí
     *
     * @return danh sách loại phòng
     */
    @GetMapping("agent/hotels/list-bed-type")
    private ResponseObject findAllListBedType() {
        List<BedTypes> bedTypes = bedTypeService.findAllListBedTypes();

        if (bedTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", bedTypes);
        }
    }

    @GetMapping("agent/hotels/list-room-utilities")
    private ResponseObject findAllRoomUtilities() {
        List<RoomUtilities> roomUtilities = roomUtilitiesService.findAllRoomUtils();

        if (roomUtilities.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", roomUtilities);
        }
    }


    @PostMapping(value = "/agent/hotels/register-hotels", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerHotels(@RequestPart("hotels") RegisterHotelDto hotelsDto,
                                                 @RequestPart("roomType") RegisterRoomTypeDto roomTypeDto,
                                                 @RequestPart("placeUtilities") List<Integer> placeUtilities,
                                                 @RequestPart("roomTypeUtilities") List<Integer> roomTypeUtilities,
                                                 @RequestPart("listRoomTypeImg") List<MultipartFile> listRoomTypeImg,
                                                 @RequestParam("bedTypeId") Integer bedTypeId,
                                                 @RequestParam("checkinTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkinTime,
                                                 @RequestParam("checkoutTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkoutTime,
                                                 @RequestParam("avatarHotel") MultipartFile avatarHotel,
                                                 @RequestParam("roomTypeAvatar") MultipartFile avatarRoomType) {

        try {
            hotelsService.registerInfoHotel(hotelsDto, avatarHotel, placeUtilities, roomTypeDto, avatarRoomType, listRoomTypeImg, roomTypeUtilities, bedTypeId, checkinTime, checkoutTime);
            return ResponseEntity.ok("{\"message\": \"Thêm thông tin khách sạn thành công\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi không mong muốn.");
        } catch (IOException io) {
            io.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi thêm hình ảnh. Vui lòng giảm chất lượng ảnh hoặc thay bằng ảnh khác.");
        }
    }


    /**
     * Phương thức tạo mới thông tin loại phòng
     *
     * @param dataHotelRoom  thông tin khách sạn
     * @param roomTypeAvatar ảnh loại phòng
     * @param hotelId        mã khách sạn
     * @param roomTypeImage  danh sách ảnh loại phòng
     * @throws IOException Throws IOException
     */
    private void createRoomType(
            Hotel_RoomDto dataHotelRoom,
            MultipartFile roomTypeAvatar,
            String hotelId,
            List<MultipartFile> roomTypeImage,
            LocalTime checkinTime,
            LocalTime checkoutTime,
            Integer roomBedId,
            List<Integer> selectedRoomUtilitiesIds) throws IOException {
        String roomTypeId = GenerateNextID.generateNextCode("RT", roomTypeService.findMaxId());
        String imgPath = fileUpload.uploadFile(roomTypeAvatar);

        RoomTypesDto roomTypesDto = dataHotelRoom.getRoomTypesDto();

        List<RoomUtilities> roomUtilities = selectedRoomUtilitiesIds
                .stream().map(roomUtilitiesService::findByRoomUtilitiesId).toList();

        RoomTypes roomTypes = EntityDtoUtils.convertToEntity(roomTypesDto, RoomTypes.class);
        roomTypes.setId(roomTypeId);
        roomTypes.setHotelId(hotelId);
        roomTypes.setRoomTypeAvatar(imgPath);
        roomTypes.setCheckinTime(checkinTime);
        roomTypes.setCheckoutTime(checkoutTime);
        roomTypes.setRoomUtilities(roomUtilities);
        roomTypeService.save(roomTypes);

        createRoomImage(roomTypeId, roomTypeImage);
        createRoomBed(roomTypeId, roomBedId);
    }

    /**
     * Phương thức tạo mới thông tin loại phòng
     *
     * @param roomTypeId    mã loại phòng
     * @param roomTypeImage ảnh loại phòng
     * @throws IOException lôi nêu không thêm được ảnh
     */
    private void createRoomImage(String roomTypeId, List<MultipartFile> roomTypeImage) throws IOException {
        for (MultipartFile file : roomTypeImage) {
            String imgPath = fileUpload.uploadFile(file);
            RoomImages roomImages = new RoomImages();
            roomImages.setRoomTypeId(roomTypeId);
            roomImages.setRoomTypeImg(imgPath);
            roomImageService.save(roomImages);
        }
    }

    /**
     * Phương thức tạo mới thông tin loại giường
     *
     * @param roomTypeId mã loại phòng
     * @param roomBedId  mã loại giường
     */
    private void createRoomBed(String roomTypeId, Integer roomBedId) {
        RoomBeds roomBeds = new RoomBeds();
        roomBeds.setRoomTypeId(roomTypeId);
        roomBeds.setBedTypeId(roomBedId);

        roomBedsService.save(roomBeds);
    }

    /**
     * Phương thức tạo mới thông tin khách sạn
     *
     * @param companyDataDto       thông tin khách sạn
     * @param selectHotelUtilities danh sách dịch vụ khách sạn
     * @param avatarHotel          ảnh đại diện khách sạn
     * @param longitude            kinh độ
     * @param latitude             vĩ độ
     * @return danh sách khách sạn
     * @throws IOException lôi nêu không thêm được ảnh
     */
    @PostMapping("agent/hotels/information-hotel/create")
    ResponseObject createHotel(
            @RequestPart("companyDataDto") CompanyDataDto companyDataDto,
            @RequestPart("selectHotelUtilities") List<Integer> selectHotelUtilities,
            @RequestParam("avatarHotel") MultipartFile avatarHotel,
            @RequestPart("longitude") String longitude,
            @RequestPart("latitude") String latitude
    ) throws IOException {

        Hotels hotels = new Hotels();

        String idHotel = GenerateNextID.generateNextCode("HTL", hotelsService.findMaxCode());

        String avataHotelUpload = fileUpload.uploadFile(avatarHotel);

        // Thêm khách sạn
        hotels.setId(idHotel);
        hotels.setHotelName(companyDataDto.getHotelName());
        hotels.setPhone(companyDataDto.getPhoneNumber());
        hotels.setUrlWebsite(companyDataDto.getWebsite());
        hotels.setProvince(companyDataDto.getProvinceName());
        hotels.setDistrict(companyDataDto.getDistrictName());
        hotels.setWard(companyDataDto.getWardName());
        hotels.setAddress(companyDataDto.getAddress());
        hotels.setFloorNumber(Integer.valueOf(companyDataDto.getFloorNumber()));
        hotels.setHotelTypeId(companyDataDto.getHotelType());
        hotels.setAgenciesId(companyDataDto.getAgencyId());
        hotels.setHotelAvatar(avataHotelUpload);
        hotels.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        hotels.setLongitude(longitude);
        hotels.setLatitude(latitude);
        hotels.setIsActive(true);
        hotels.setIsAccepted(true);
        hotels.setIsDeleted(false);

        List<PlaceUtilities> placeUtilitiesList = selectHotelUtilities.stream()
                .map(placeUtilitiesService::findById)
                .collect(Collectors.toList());

        hotels.setPlaceUtilities(placeUtilitiesList);
        hotelsService.save(hotels);


        return new ResponseObject("200", "Thông tin khách sạn được thêm thành công", null);
    }

    /**
     * Phương thức chỉnh sửa thông tin khách sạn
     *
     * @param dataHotel          thông tin khách sạn
     * @param selectedUtilities  danh sách dịch vụ khách sạn
     * @param hotelAvatarUpdated ảnh đai diện khách sạn
     * @return danh sach khách san
     * @throws IOException lôi nêu không thêm được ảnh
     */
    @PutMapping("agent/hotels/information-hotel/update")
    ResponseObject updateHotel(
            @RequestPart("dataHotel") HotelsDto dataHotel,
            @RequestPart("selectedUtilities") List<Integer> selectedUtilities,
            @RequestParam("hotelAvatarUpdated") MultipartFile hotelAvatarUpdated,
            @RequestParam(value = "longitude", defaultValue = "") String longitude,
            @RequestParam(value = "latitude", defaultValue = "") String latitude
    ) throws IOException {

        Optional<Hotels> hotels = hotelsService.findById(dataHotel.getId());

        String avataHotelUpload = null;
        String oldAvataHotelUpload = hotels.get().getHotelAvatar();

        if (hotelAvatarUpdated != null && !hotelAvatarUpdated.isEmpty()) {
            avataHotelUpload = fileUpload.uploadFile(hotelAvatarUpdated);
        }

        hotels = Optional.ofNullable(EntityDtoUtils.convertToEntity(dataHotel, Hotels.class));
        if (avataHotelUpload != null) {
            hotels.get().setHotelAvatar(avataHotelUpload);
        } else if (oldAvataHotelUpload != null) {
            hotels.get().setHotelAvatar(oldAvataHotelUpload);
        }

        List<PlaceUtilities> placeUtilitiesList = selectedUtilities.stream()
                .map(placeUtilitiesService::findById)
                .collect(Collectors.toList());
        hotels.get().setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        hotels.get().setIsActive(true);
        hotels.get().setIsAccepted(true);
        hotels.get().setIsDeleted(false);
        hotels.get().setPlaceUtilities(placeUtilitiesList);

        if (longitude != null && !longitude.isEmpty()) {
            hotels.get().setLongitude(longitude);
        }
        if (latitude != null && !latitude.isEmpty()) {
            hotels.get().setLatitude(latitude);
        }

        hotelsService.save(hotels.get());

        return new ResponseObject("200", "Thông tin khách sạn được thay đổi thành công", null);
    }

    /**
     * Phương thức tìm khách sạn dựa vào id của khách sạn
     *
     * @param id mã khách sạn
     * @return danh sách khách sạn
     */
    @GetMapping("agent/hotels/findByHotelId/{id}")
    public ResponseObject findByHotel(
            @PathVariable("id") String id
    ) {
        Optional<Hotels> hotels = hotelsService.findById(id);
        if (hotels.isPresent()) {
            return new ResponseObject("200", "OK", hotels);
        } else {
            return new ResponseObject("400", "Không tim thấy dữ liệu", null);
        }

    }

    /**
     * Phương thức xóa khách sạn dựa vào id của khách sạn
     *
     * @param id mã khách sạn
     * @return danh sách khách sạn
     */
    @DeleteMapping("agent/hotels/deleteHotel/{id}")
    public ResponseObject deleteHotel(
            @PathVariable("id") String id
    ) {
        Optional<Hotels> hotels = hotelsService.findById(id);
        if (hotels.isPresent()) {
            hotels.get().setIsDeleted(true);
            hotels.get().setDateDeleted(Timestamp.valueOf(LocalDateTime.now()));
            hotelsService.save(hotels.get());
            return new ResponseObject("200", "Xóa dữ liệu thành công", null);
        } else {
            return new ResponseObject("400", "Không tim thấy dữ liệu", null);
        }

    }
}
