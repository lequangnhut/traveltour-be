package com.main.traveltour.service.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.traveltour.dto.agent.hotel.RoomTypeCustomerDto;
import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RoomTypeService {

    String findMaxId();
    Page<RoomTypes> findAll(Pageable pageable);

    Page<RoomTypes> findAllByHotelIdAndIsDelete(String hotelId, Boolean isDeleted, Pageable pageable);
    Page<RoomTypes> findAllWithSearchAndHotelId(String searchTerm, String hotelId, Pageable pageable);
    List<RoomTypes> findAllByHotelId(String hotelId);
    RoomTypes save(RoomTypes roomTypes);
    Optional<RoomTypes> findRoomTypeById(String roomTypeId);

    Optional<RoomTypes> findRoomTypeByRoomTypeId(String roomTypeId);

    RoomTypeCustomerDto findRoomTypesWithFiltersCustomer(
            BigDecimal priceFilter,
            List<Integer> hotelTypeIdListFilter,
            List<Integer> placeUtilitiesIdListFilter,
            List<Integer> roomUtilitiesIdListFilter,
            Boolean breakfastIncludedFilter,
            Boolean freeCancellationFilter,
            List<Integer> roomBedsIdListFilter,
            Integer amountRoomFilter,
            String locationFilter,
            Integer capacityAdultsFilter,
            Integer capacityChildrenFilter,
            Boolean isDeletedHotelFilter,
            Boolean isDeletedRoomTypeFilter,
            Timestamp checkInDateFiller,
            Timestamp checkOutDateFiller,
            String hotelIdFilter,
            int page,
            int size,
            String sort
    );

    List<RoomTypes> findAllRoomTypeByIds(List<String> ids);

    void registerRoomType(RoomTypes roomTypeDto, String hotelId, List<Integer> roomTypeUtilities, MultipartFile roomTypeAvatar, List<MultipartFile> listRoomTypeImg, LocalTime checkinTime, LocalTime checkoutTime, Integer bedTypeId) throws IOException;

    void deleteAllRoomTypeByIds(List<String> roomTypeIds) throws JsonProcessingException;

    void restoreAllRoomTypeByIds(List<String> roomTypeIds) throws JsonProcessingException;
}
