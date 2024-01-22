package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.Hotel_RoomDto;
import com.main.traveltour.dto.agent.HotelsDto;
import com.main.traveltour.dto.agent.RoomTypesDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.*;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/agent/hotels/find-by-agency-id/{agencyId}")
    private Hotels findByUserId(@PathVariable int agencyId) {
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
}
