package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.dto.agent.hotel.RoomTypeCustomerDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.service.admin.RoomBedsServiceAD;
import com.main.traveltour.service.agent.BedTypeService;
import com.main.traveltour.service.agent.RoomImageService;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.utils.FileUploadResize;
import com.main.traveltour.utils.GenerateNextID;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypesRepository roomTypesRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FileUploadResize fileUploadResize;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomImageService roomImageService;

    @Autowired
    private RoomBedsServiceAD roomBedsServiceAD;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomTypes.class);

    @Override
    public String findMaxId() {
        return roomTypesRepository.findMaxId();
    }

    @Override
    public Page<RoomTypes> findAll(Pageable pageable) {
        return roomTypesRepository.findAll(pageable);
    }

    @Override
    public Page<RoomTypes> findAllWithSearchAndHotelId(String searchTerm, String hotelId, Pageable pageable) {
        return roomTypesRepository.findBySearchTermAndHotelId(searchTerm, hotelId, pageable);
    }

    @Override
    public Page<RoomTypes> findAllByHotelIdAndIsDelete(String hotelId, Boolean isDelete, Pageable pageable) {
        return roomTypesRepository.findByHotelIdAndIsDeleted(hotelId, isDelete, pageable);
    }

    @Override
    public List<RoomTypes> findAllByHotelId(String hotelId) {
        return roomTypesRepository.findAllByHotelId(hotelId);
    }

    @Override
    public RoomTypes save(RoomTypes roomTypes) {
        return roomTypesRepository.save(roomTypes);
    }

    @Override
    public Optional<RoomTypes> findRoomTypeById(String roomTypeId) {
        return roomTypesRepository.findById(roomTypeId);
    }

    @Override
    public Optional<RoomTypes> findRoomTypeByRoomTypeId(String roomTypeId) {
        return roomTypesRepository.findById(roomTypeId);
    }

    @Transactional(readOnly = true)
    public RoomTypeCustomerDto findRoomTypesWithFiltersCustomer(
            BigDecimal priceFilter, List<Integer> hotelTypeIdListFilter,
            List<Integer> placeUtilitiesIdListFilter, List<Integer> roomUtilitiesIdListFilter,
            Boolean breakfastIncludedFilter, Boolean freeCancellationFilter, List<Integer> roomBedsIdListFilter,
            Integer amountRoomFilter, String locationFilter, Integer capacityAdultsFilter,
            Integer capacityChildrenFilter, Boolean isDeletedHotelFilter, Boolean isDeletedRoomTypeFilter,
            Timestamp checkInDateFiller, Timestamp checkOutDateFiller,
            String hotelIdFilter, int page, int size, String sort) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoomTypes> query = builder.createQuery(RoomTypes.class);
        Root<RoomTypes> root = query.from(RoomTypes.class);
        RoomTypeCustomerDto roomTypeCustomerResult = new RoomTypeCustomerDto();
        // Tạo danh sách các điều kiện (predicates)
        List<Predicate> predicates = new ArrayList<>();

        if (priceFilter != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("price"), priceFilter));
        }

        if (hotelTypeIdListFilter != null && !hotelTypeIdListFilter.isEmpty()) {
            Join<RoomTypes, Hotels> hotelsJoin = root.join("hotelsByHotelId", JoinType.LEFT);
            predicates.add(hotelsJoin.get("hotelTypeId").in(hotelTypeIdListFilter));
        }

        if (placeUtilitiesIdListFilter != null && !placeUtilitiesIdListFilter.isEmpty()) {
            Join<RoomTypes, Hotels> hotelsJoin = root.join("hotelsByHotelId", JoinType.LEFT);
            Join<Hotels, PlaceUtilities> placeUtilitiesJoin = hotelsJoin.join("placeUtilities");
            predicates.add(placeUtilitiesJoin.get("id").in(placeUtilitiesIdListFilter));
        }

        if (roomUtilitiesIdListFilter != null && !roomUtilitiesIdListFilter.isEmpty()) {
            Join<RoomTypes, RoomUtilities> roomUtilitiesJoin = root.join("roomUtilities");
            predicates.add(roomUtilitiesJoin.get("id").in(roomUtilitiesIdListFilter));
        }

        if (roomBedsIdListFilter != null && !roomBedsIdListFilter.isEmpty()) {
            Join<RoomTypes, RoomBeds> roomBedsJoin = root.join("roomBedsById");
            predicates.add(roomBedsJoin.get("bedTypeId").in(roomBedsIdListFilter));
        }

        if (amountRoomFilter != null && amountRoomFilter > 0) {
            predicates.add(builder.equal(root.get("amountRoom"), amountRoomFilter));
        }

        if (locationFilter != null && !locationFilter.isEmpty()) {
            Join<RoomTypes, Hotels> hotelsJoin = root.join("hotelsByHotelId", JoinType.LEFT);
            Predicate locationPredicate = builder.or(
                    builder.like(hotelsJoin.get("hotelName"), "%" + locationFilter + "%"),
                    builder.like(hotelsJoin.get("province"), "%" + locationFilter + "%"),
                    builder.like(hotelsJoin.get("district"), "%" + locationFilter + "%"),
                    builder.like(hotelsJoin.get("ward"), "%" + locationFilter + "%")
            );
            predicates.add(locationPredicate);
        }


        if (capacityAdultsFilter != null && capacityAdultsFilter > 0) {
            predicates.add(builder.lessThanOrEqualTo(root.get("capacityAdults"), capacityAdultsFilter));
        }

        if (capacityChildrenFilter != null && capacityChildrenFilter > 0) {
            predicates.add(builder.lessThanOrEqualTo(root.get("capacityChildren"), capacityChildrenFilter));
        }

        if (isDeletedHotelFilter != null) {
            Join<RoomTypes, Hotels> hotelsJoin = root.join("hotelsByHotelId", JoinType.LEFT);
            predicates.add(builder.equal(hotelsJoin.get("isDeleted"), isDeletedHotelFilter));
        }

        if (isDeletedRoomTypeFilter != null) {
            predicates.add(builder.equal(root.get("isDeleted"), isDeletedRoomTypeFilter));
        }

        if (breakfastIncludedFilter != null) {
            predicates.add(builder.equal(root.get("breakfastIncluded"), breakfastIncludedFilter));
        }

        if (freeCancellationFilter != null) {
            predicates.add(builder.equal(root.get("freeCancellation"), freeCancellationFilter));
        }

        if (checkInDateFiller != null && checkOutDateFiller != null) {
            List<Object[]> resultListAmountRoom = entityManager.createNativeQuery(
                            "SELECT DISTINCT room_types.id, SUM(order_hotel_details.amount) AS totalBooked " +
                                    "FROM room_types " +
                                    "INNER JOIN order_hotel_details ON room_types.id = order_hotel_details.room_type_id " +
                                    "INNER JOIN order_hotels ON order_hotel_details.order_hotel_id = order_hotels.id " +
                                    "WHERE order_hotels.check_in <= :checkOutDate AND order_hotels.check_out >= :checkInDate " +
                                    "GROUP BY room_types.id")
                    .setParameter("checkInDate", checkInDateFiller)
                    .setParameter("checkOutDate", checkOutDateFiller)
                    .getResultList();


            for (Object[] result : resultListAmountRoom) {
                String roomId = (String) result[0];
                BigDecimal numberOfRoomsBookedBigDecimal = (BigDecimal) result[1];
                Long numberOfRoomsBooked = numberOfRoomsBookedBigDecimal.longValue(); // Chuyển đổi từ BigDecimal sang Long
                RoomTypes roomType = entityManager.find(RoomTypes.class, roomId);
                if (roomType != null) {
                    Integer remainingRooms = roomType.getAmountRoom() - numberOfRoomsBooked.intValue();
                    if (remainingRooms < 0) {
                        remainingRooms = 0;
                    }

                    roomType.setAmountRoom(remainingRooms);
                    entityManager.merge(roomType);
                }
            }

        }

        if (sort != null && !sort.isEmpty()) {
            if ("01".equalsIgnoreCase(sort)) {
                query.orderBy(builder.asc(root.get("price")));
            } else if ("02".equalsIgnoreCase(sort)) {
                query.orderBy(builder.desc(root.get("price")));
            } else if ("03".equalsIgnoreCase(sort)) {
                query.orderBy(builder.asc(root.get("amountRoom")));
            } else if ("04".equalsIgnoreCase(sort)) {
                query.orderBy(builder.desc(root.get("amountRoom")));
            }
        }

        if (hotelIdFilter != null && !hotelIdFilter.isEmpty()) {
            Join<RoomTypes, Hotels> hotelsJoin = root.join("hotelsByHotelId", JoinType.LEFT);
            predicates.add(builder.equal(hotelsJoin.get("id"), hotelIdFilter));
        }

        query.where(predicates.toArray(new Predicate[0]));

        roomTypeCustomerResult.setTotalCount(entityManager.createQuery(query).getResultList().size());
        roomTypeCustomerResult.setRoomTypes(
                entityManager.createQuery(query)
                        .setFirstResult(page * size)
                        .setMaxResults(size)
                        .getResultList()
        );

        return roomTypeCustomerResult;
    }

    @Override
    public List<RoomTypes> findAllRoomTypeByIds(List<String> ids) {
        return roomTypesRepository.findByIdIn(ids);
    }

    @Override
    public void registerRoomType(RoomTypes roomTypes, String hotelId, List<Integer> roomTypeUtilities, MultipartFile roomTypeAvatar, List<MultipartFile> listRoomTypeImg, LocalTime checkinTime, LocalTime checkoutTime, Integer bedTypeId) throws IOException {
        String roomTypeAvatarUpload = fileUploadResize.uploadFileResize(roomTypeAvatar);
        String roomTypeId = GenerateNextID.generateNextCode("RT", roomTypeService.findMaxId());

        roomTypes.setId(roomTypeId);
        roomTypes.setHotelId(hotelId);
        roomTypes.setRoomTypeAvatar(roomTypeAvatarUpload);
        roomTypes.setCheckinTime(checkinTime);
        roomTypes.setCheckoutTime(checkoutTime);
        roomTypes.setIsDeleted(false);
        roomTypesRepository.save(roomTypes);

        listRoomTypeImg.stream().map(roomTypeImage -> {
            RoomImages roomImages = new RoomImages();
            roomImages.setRoomTypeId(roomTypes.getId());
            try {
                roomImages.setRoomTypeImg(fileUploadResize.uploadFileResizeAndReducedQuality(roomTypeImage));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return roomImages;
        }).forEach(roomImageService::save);

        RoomBeds roomBeds = RoomBeds.builder()
                .roomTypeId(roomTypes.getId())
                .bedTypeId(bedTypeId)
                .build();
        roomBedsServiceAD.save(roomBeds);
    }

}
