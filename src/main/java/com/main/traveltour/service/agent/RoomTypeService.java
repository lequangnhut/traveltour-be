package com.main.traveltour.service.agent;

import com.main.traveltour.dto.agent.hotel.RoomTypeCustomerDto;
import com.main.traveltour.entity.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
            int page,
            int size
    );
}
