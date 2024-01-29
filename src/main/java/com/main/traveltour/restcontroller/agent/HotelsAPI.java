package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.CompanyDataDto;
import com.main.traveltour.dto.agent.Hotel_RoomDto;
import com.main.traveltour.dto.agent.HotelsDto;
import com.main.traveltour.dto.agent.RoomTypesDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.*;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    @GetMapping("/agent/hotels/find-all-by-agency-id/{agencyId}")
    private List<Hotels> findAllByAgencyId(@PathVariable int agencyId) {
        return hotelsService.findAllByAgencyId(agencyId);
    }

    @GetMapping("/agent/hotels/find-by-agency-id/{agencyId}")
    private Hotels findByAgencyId(@PathVariable int agencyId) {
        return hotelsService.findByAgencyId(agencyId);
    }

    @GetMapping("/agent/hotels/list-hotels")
    private ResponseObject findAllHotels() {
        List<Hotels> hotels = hotelsService.findAllListHotel();

        if (hotels.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotels);
        }
    }

    @GetMapping("/agent/hotels/list-hotels-type")
    private ResponseObject findAllHotelsTYpe() {
        List<HotelTypes> hotelType = hotelsTypeService.findAllHotelType();

        if (hotelType.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", hotelType);
        }
    }

    @GetMapping("agent/hotels/list-place-utilities")
    private ResponseObject findAllPlaceUtilities() {
        List<PlaceUtilities> listPlaceUtilities = placeUtilitiesService.findAll();

        if (listPlaceUtilities.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", listPlaceUtilities);
        }
    }

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
    public void registerHotels(@RequestPart("dataHotelRoom") Hotel_RoomDto dataHotelRoom,
                               @RequestPart("roomTypeImage") List<MultipartFile> roomTypeImage,
                               @RequestPart("hotelAvatar") MultipartFile hotelAvatar,
                               @RequestPart("roomTypeAvatar") MultipartFile roomTypeAvatar) throws IOException {
        String hotelAvtar = fileUpload.uploadFile(hotelAvatar);
        HotelsDto hotelsDto = dataHotelRoom.getHotelsDto();

        List<PlaceUtilities> placeUtilities = dataHotelRoom.getSelectedPlaceUtilitiesIds()
                .stream().map(placeUtilitiesService::findByPlaceId).toList();

        Hotels hotels = EntityDtoUtils.convertToEntity(hotelsDto, Hotels.class);
        hotels.setId(hotelsDto.getId());
        hotels.setHotelAvatar(hotelAvtar);
        hotels.setIsAccepted(Boolean.TRUE);
        hotels.setPlaceUtilities(placeUtilities);
        hotelsService.save(hotels);

        createRoomType(dataHotelRoom, roomTypeAvatar, hotels.getId(), roomTypeImage);
    }

    private void createRoomType(Hotel_RoomDto dataHotelRoom, MultipartFile roomTypeAvatar, String hotelId, List<MultipartFile> roomTypeImage) throws IOException {
        String roomTypeId = GenerateNextID.generateNextCode("RT", roomTypeService.findMaxId());
        String imgPath = fileUpload.uploadFile(roomTypeAvatar);

        RoomTypesDto roomTypesDto = dataHotelRoom.getRoomTypesDto();

        List<RoomUtilities> roomUtilities = dataHotelRoom.getSelectedRoomUtilitiesIds()
                .stream().map(roomUtilitiesService::findByRoomUtilitiesId).toList();

        RoomTypes roomTypes = EntityDtoUtils.convertToEntity(roomTypesDto, RoomTypes.class);
        roomTypes.setId(roomTypeId);
        roomTypes.setHotelId(hotelId);
        roomTypes.setRoomTypeAvatar(imgPath);
        roomTypes.setRoomUtilities(roomUtilities);
        roomTypeService.save(roomTypes);

        createRoomImage(roomTypeId, roomTypeImage);
    }

    private void createRoomImage(String roomTypeId, List<MultipartFile> roomTypeImage) throws IOException {
        for (MultipartFile file : roomTypeImage) {
            String imgPath = fileUpload.uploadFile(file);
            RoomImages roomImages = new RoomImages();
            roomImages.setRoomTypeId(roomTypeId);
            roomImages.setRoomTypeImg(imgPath);
            roomImageService.save(roomImages);
        }
    }

    @PostMapping("agent/hotels/information-hotel/create")
    ResponseObject createHotel(
            @RequestPart("companyDataDto") CompanyDataDto companyDataDto,
            @RequestPart("selectHotelUtilities") List<Integer> selectHotelUtilities,
            @RequestParam("avatarHotel") MultipartFile avatarHotel
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
        hotels.setIsActive(true);
        hotels.setIsAccepted(true);

        List<PlaceUtilities> placeUtilitiesList = selectHotelUtilities.stream()
                .map(placeUtilitiesService::findById)
                .collect(Collectors.toList());

        hotels.setPlaceUtilities(placeUtilitiesList);
        hotelsService.save(hotels);


        return new ResponseObject("200", "Thông tin khách sạn được thêm thành công", null);
    }

    /**
     * Phương thức chỉnh sửa thông tin khách sạn
     * @param dataHotel thông tin khách sạn
     * @param selectedUtilities danh sách dịch vụ khách sạn
     * @param hotelAvatarUpdated ảnh đai diện khách sạn
     * @return danh sach khách san
     * @throws IOException lôi nêu không thêm được ảnh
     */
    @PutMapping("agent/hotels/information-hotel/update")
    ResponseObject updateHotel(
            @RequestPart("dataHotel") HotelsDto dataHotel,
            @RequestPart("selectedUtilities") List<Integer> selectedUtilities,
            @RequestParam("hotelAvatarUpdated") MultipartFile hotelAvatarUpdated
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
        } else if (oldAvataHotelUpload != null){
            hotels.get().setHotelAvatar(oldAvataHotelUpload);
        }

        List<PlaceUtilities> placeUtilitiesList = selectedUtilities.stream()
                .map(placeUtilitiesService::findById)
                .collect(Collectors.toList());

        hotels.get().setIsActive(true);
        hotels.get().setIsAccepted(true);
        hotels.get().setPlaceUtilities(placeUtilitiesList);

        hotelsService.save(hotels.get());

        return new ResponseObject("200", "Thông tin khách sạn được thay đổi thành công", null);
    }

    /**
     * Phương thức tìm khách sạn dựa vào id của khách sạn
     * @param id mã khách sạn
     * @return
     */
    @GetMapping("agent/hotels/findByHotelId/{id}")
    public ResponseObject findByHotel(
            @PathVariable("id") String id
    ) {
        Optional<Hotels> hotels = hotelsService.findById(id);
        if(hotels.isPresent()) {
            return new ResponseObject("200", "OK", hotels);
        }else{
            return new ResponseObject("400", "Không tim thấy dữ liệu", null);
        }

    }
}
