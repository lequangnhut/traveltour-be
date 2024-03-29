package com.main.traveltour.service.staff.impl;

import com.main.traveltour.dto.staff.HotelsDto;
import com.main.traveltour.entity.Hotels;
import com.main.traveltour.repository.HotelsRepository;
import com.main.traveltour.service.staff.HotelServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HotelServiceServiceImpl implements HotelServiceService {

    @Autowired
    private HotelsRepository repo;

    @Override
    public Page<HotelsDto> findAvailableHotelsWithFilters(String searchTerm, String location, Date checkIn, Date checkOut, Integer numAdults, Integer numChildren, Integer numRooms, Pageable pageable) {
        List<Hotels> hotelsList = repo.findAllBySearch(searchTerm, location, numAdults, numChildren);

        List<HotelsDto> filteredAndConvertedHotels = hotelsList.stream().map(hotel -> {
                    int bookedRooms = calculateBookedRoomsByHotelId(hotel.getId(), checkIn, checkOut);
                    int allHotelRooms = calculateAllHotelRoomNumbersByHotelId(hotel.getId());
                    HotelsDto dto = convertToDto(hotel);
                    return (numRooms == null) || (numRooms <= (allHotelRooms - bookedRooms)) ? dto : null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        int start = Math.min((int) pageable.getOffset(), filteredAndConvertedHotels.size());
        int end = Math.min((start + pageable.getPageSize()), filteredAndConvertedHotels.size());

        List<HotelsDto> paginatedList = filteredAndConvertedHotels.isEmpty() ? Collections.emptyList() : filteredAndConvertedHotels.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, filteredAndConvertedHotels.size());
    }

    private int calculateBookedRoomsByHotelId(String hotelId, Date checkIn, Date checkOut) {
        Integer bookedRooms = repo.calculateBookedRoomsByHotelId(hotelId, checkIn, checkOut);
        return bookedRooms != null ? bookedRooms : 0;
    }

    private int calculateAllHotelRoomNumbersByHotelId(String hotelId) {
        Integer allHotelRooms = repo.calculateAllHotelRoomNumbersByHotelId(hotelId);
        return allHotelRooms != null ? allHotelRooms : 0;
    }

    private int CalculateAverageHotelRoomPrice(String hotelId) {
        Integer averageRoomPrice = repo.CalculateAverageHotelRoomPrice(hotelId);
        return averageRoomPrice != null ? averageRoomPrice : 0;
    }

    private HotelsDto convertToDto(Hotels hotel) {
        HotelsDto dto = new HotelsDto();
        dto.setId(hotel.getId());
        dto.setHotelName(hotel.getHotelName());
        dto.setUrlWebsite(hotel.getUrlWebsite());
        dto.setPhone(hotel.getPhone());
        dto.setFloorNumber(hotel.getFloorNumber());
        dto.setProvince(hotel.getProvince());
        dto.setDistrict(hotel.getDistrict());
        dto.setWard(hotel.getWard());
        dto.setAddress(hotel.getAddress());
        dto.setDateCreated(hotel.getDateCreated());
        dto.setDateDeleted(hotel.getDateDeleted());
        dto.setIsAccepted(hotel.getIsAccepted());
        dto.setIsActive(hotel.getIsActive());
        dto.setIsDeleted(hotel.getIsDeleted());
        dto.setHotelAvatar(hotel.getHotelAvatar());
        dto.setLongitude(hotel.getLongitude());
        dto.setLatitude(hotel.getLatitude());
        dto.setHotelTypeId(hotel.getHotelTypeId());
        dto.setAgenciesId(hotel.getAgenciesId());
        dto.setLocation(createLocationString(hotel.getProvince(), hotel.getDistrict(), hotel.getWard()));
        BigDecimal averageRoomPrice = new BigDecimal(CalculateAverageHotelRoomPrice(hotel.getId()));
        dto.setAverageRoomPrice(averageRoomPrice);
        return dto;
    }

    private String createLocationString(String province, String district, String ward) {
        StringBuilder locationBuilder = new StringBuilder();
        if (province != null && !province.isEmpty()) {
            locationBuilder.append(province);
        }
        if (district != null && !district.isEmpty()) {
            if (!locationBuilder.isEmpty()) {
                locationBuilder.append(", ");
            }
            locationBuilder.append(district);
        }
        if (ward != null && !ward.isEmpty()) {
            if (!locationBuilder.isEmpty()) {
                locationBuilder.append(", ");
            }
            locationBuilder.append(ward);
        }
        return locationBuilder.toString();
    }

    @Override
    public Hotels getHotelsById(String id) {
        return repo.getHotelsById(id);
    }

    @Override
    public Hotels findByRoomTypeId(String roomTypeId) {
        return repo.findByRoomTypeId(roomTypeId);
    }

    @Override
    public Long countHotels() {
        return repo.countHotels();
    }

}
