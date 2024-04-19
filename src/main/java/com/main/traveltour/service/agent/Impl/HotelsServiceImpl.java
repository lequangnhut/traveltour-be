package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.dto.agent.hotel.RegisterHotelDto;
import com.main.traveltour.dto.agent.hotel.RegisterRoomTypeDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.repository.*;
import com.main.traveltour.service.admin.RoomBedsServiceAD;
import com.main.traveltour.service.admin.RoomTypesServiceAD;
import com.main.traveltour.service.agent.*;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.service.utils.FileUploadResize;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelsServiceImpl implements HotelsService {
    @Autowired
    private HotelsRepository hotelsRepository;
    @Autowired
    private PlaceUtilitiesService placeUtilitiesService;
    @Autowired
    private FileUploadResize fileUploadResize;
    @Autowired
    private FileUpload fileUpload;
    @Autowired
    private HotelsService hotelsService;
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private HotelsTypeService hotelsTypeService;
    @Autowired
    private AgenciesService agenciesService;
    @Autowired
    private BedTypesRepository bedTypesRepository;
    @Autowired
    private RoomUtilitiesRepository roomUtilitiesRepository;
    @Autowired
    private RoomImageService roomImageService;
    @Autowired
    private RoomBedsServiceAD roomBedsServiceAD;

    @Override
    public List<Hotels> findAllListHotel() {
        return hotelsRepository.findAll();
    }

    @Override
    public List<Hotels> findAllByAgencyId(int agencyId) {
        return hotelsRepository.findAllByAgenciesIdAndIsDeleted(agencyId, false);
    }

    @Override
    public Hotels findByAgencyId(int agencyId) {
        return hotelsRepository.findByAgenciesId(agencyId);
    }

    @Override
    public String findMaxCode() {
        return hotelsRepository.findMaxCode();
    }

    @Override
    public Hotels save(Hotels hotels) {
        return hotelsRepository.save(hotels);
    }

    @Override
    public Optional<Hotels> findById(String id) {
        return hotelsRepository.findById(id);
    }

    @Override
    public void delete(Hotels hotels) {
        hotelsRepository.save(hotels);
    }

    @Override
    public List<Hotels> getAllHotels() {
        return hotelsRepository.findAll();
    }

    @Override
    public void registerInfoHotel(Hotels hotels, String hotelId, List<Integer> placeUtilities, MultipartFile avatarHotel) throws IOException {

        String avataHotelUpload = fileUploadResize.uploadFileResize(avatarHotel);

        hotels.setId(hotelId);
        hotels.setIsDeleted(false);
        hotels.setDateCreated(Timestamp.valueOf(java.time.LocalDateTime.now()));
        hotels.setIsActive(true);
        hotels.setIsAccepted(true);
        hotels.setHotelAvatar(avataHotelUpload);
        if (hotels.getHotelDescription() == null) {
            hotels.setHotelDescription("Không có");
        }

        List<PlaceUtilities> placeUtilitiesList = placeUtilities.stream()
                .map(placeUtilitiesService::findById)
                .collect(Collectors.toList());

        hotels.setPlaceUtilities(placeUtilitiesList);
        hotelsRepository.save(hotels);
    }

    @Override
    public void registerInfoHotel(
            RegisterHotelDto hotels, MultipartFile avatarHotel, List<Integer> placeUtilities,
            RegisterRoomTypeDto roomTypes, MultipartFile avatarRoomTypes, List<MultipartFile> listRoomTypeImg,
            List<Integer> roomTypeUtilities, Integer bedTypeId, LocalTime checkinTime, LocalTime checkoutTime) throws IOException {
        Objects.requireNonNull(avatarHotel, "{\"message\": \"Không có hình ảnh vui lòng thử lại\"}");
        Objects.requireNonNull(avatarRoomTypes, "{\"message\": \"Không có hình ảnh vui lòng thử lại\"}");
        Objects.requireNonNull(listRoomTypeImg, "{\"message\": \"Không có hình ảnh vui lòng thử lại\"}");
        // Thêm thông tin khách sạn
        String hotelId = GenerateNextID.generateNextCode("HTL", hotelsService.findMaxCode());
        String roomTypeId = GenerateNextID.generateNextCode("RT", roomTypeService.findMaxId());

        HotelTypes hotelTypes = hotelsTypeService.findById(hotels.getHotelTypeId()).orElseThrow(() -> new IllegalStateException("Không tìm thấy loại khách sạn"));
        Agencies agencies = agenciesService.findById(hotels.getAgenciesId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy doanh nghiệp"));
        List<PlaceUtilities> placeUtils = placeUtilities.stream().map(place -> placeUtilitiesService.findByPlaceUtilsId(place).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ khách sạn"))).toList();
        RoomBeds roomBed = bedTypesRepository.findById(bedTypeId)
                .map(bedType -> {
                    return RoomBeds.builder()
                            .roomTypeId(roomTypeId)
                            .bedTypeId(bedType.getId())
                            .build();
                })
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy loại giường"));

        List<RoomUtilities> roomUtilities = roomTypeUtilities.stream().map(utils -> roomUtilitiesRepository.findById(utils).orElseThrow(() -> new IllegalStateException("Không tìm thấy dịch vụ phòng!"))).toList();
        List<RoomImages> roomImages = listRoomTypeImg.stream()
                .map(roomTypeImg -> {
                    try {
                        return RoomImages.builder()
                                .roomTypeId(roomTypeId)
                                .roomTypeImg(fileUpload.uploadFile(roomTypeImg))
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        String roomTypeAvatar = fileUploadResize.uploadFileResize(avatarRoomTypes);

        Hotels hotel = Hotels.builder()
                .id(hotelId)
                .hotelName(hotels.getHotelName())
                .urlWebsite(hotels.getUrlWebsite())
                .phone(hotels.getPhone())
                .floorNumber(hotels.getFloorNumber())
                .province(hotels.getProvince())
                .district(hotels.getDistrict())
                .ward(hotels.getWard())
                .address(hotels.getAddress())
                .dateCreated(Timestamp.valueOf(LocalDateTime.now()))
                .isAccepted(false   )
                .isActive(true)
                .isDeleted(false)
                .hotelAvatar(fileUploadResize.uploadFileResize(avatarHotel))
                .hotelDescription(hotels.getHotelDescription())
                .longitude(hotels.getLongitude())
                .latitude(hotels.getLatitude())
                .hotelTypeId(hotels.getHotelTypeId())
                .agenciesId(hotels.getAgenciesId())
                .roomTypesById(null)
                .hotelImagesById(null)
                .hotelTypesByHotelTypeId(hotelTypes)
                .agenciesByAgenciesId(agencies)
                .placeUtilities(placeUtils)
                .build();
        hotelsService.save(hotel);

        RoomTypes roomType = RoomTypes.builder()
                .id(roomTypeId)
                .roomTypeName(roomTypes.getRoomTypeName())
                .hotelId(hotelId)
                .capacityAdults(roomTypes.getCapacityAdults())
                .capacityChildren(roomTypes.getCapacityChildren())
                .amountRoom(roomTypes.getAmountRoom())
                .price(roomTypes.getPrice())
                .breakfastIncluded(roomTypes.getBreakfastIncluded())
                .freeCancellation(roomTypes.getFreeCancellation())
                .checkinTime(checkinTime)
                .checkoutTime(checkoutTime)
                .isDeleted(false)
                .isActive(1)
                .roomTypeAvatar(roomTypeAvatar)
                .roomTypeDescription(roomTypes.getRoomTypeDescription())
                .hotelsByHotelId(hotel)
                .roomUtilities(roomUtilities)
                .build();
        roomTypeService.save(roomType);
        roomImageService.saveAllImages(roomImages);
        roomBedsServiceAD.save(roomBed);
    }

    @Override
    public List<Hotels> findHotelsByAgenciesIdAndIsDeleted(Integer agentId) {
        Objects.requireNonNull(agentId, "{\"message\": \"Dữ liệu không hợp lệ\"}");
        Agencies agencies = agenciesService.findById(agentId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy doanh nghiệp"));
        return hotelsRepository.findAllByAgenciesIdAndIsDeleted(agencies.getId(), false);
    }

}
