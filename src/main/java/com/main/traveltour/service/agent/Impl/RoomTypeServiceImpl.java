package com.main.traveltour.service.agent.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.traveltour.data.RoomOrder;
import com.main.traveltour.dto.agent.hotel.RoomTypeCustomerDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.repository.RoomTypesRepository;
import com.main.traveltour.repository.RoomUtilitiesRepository;
import com.main.traveltour.service.admin.RoomBedsServiceAD;
import com.main.traveltour.service.agent.BedTypeService;
import com.main.traveltour.service.agent.RoomImageService;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.agent.RoomUtilitiesService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.service.utils.FileUploadResize;
import com.main.traveltour.utils.ChangeCheckInTimeService;
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
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypesRepository roomTypesRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FileUploadResize fileUploadResize;

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomImageService roomImageService;

    @Autowired
    private RoomBedsServiceAD roomBedsServiceAD;

    @Autowired
    private RoomUtilitiesRepository roomUtilitiesRepository;
    @Autowired
    ChangeCheckInTimeService changeCheckInTimeService;
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

        Timestamp newCheckIn = changeCheckInTimeService.changeCheckInTime(checkInDateFiller);
        Timestamp newCheckOut = changeCheckInTimeService.changeCheckInTime(checkOutDateFiller);
        System.out.println("new checkin: " + newCheckIn);

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
                    builder.like(hotelsJoin.get("ward"), "%" + locationFilter + "%"),
                    builder.like(hotelsJoin.get("address"), "%" + locationFilter + "%"),
                    builder.like(hotelsJoin.get("hotelDescription"), "%" + locationFilter + "%")
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
                            "SELECT MAX(total_amount) AS max_amount,\n" +
                                    "       subquery.room_type_id,\n" +
                                    "       subquery.checkIn,\n" +
                                    "       subquery.checkOut\n" +
                                    "FROM (SELECT SUM(ohd.amount) AS total_amount,\n" +
                                    "             rt.id           AS room_type_id,\n" +
                                    "             oh.check_in     AS checkIn,\n" +
                                    "             oh.check_out    AS checkOut\n" +
                                    "      FROM room_types rt\n" +
                                    "               INNER JOIN order_hotel_details ohd ON rt.id = ohd.room_type_id\n" +
                                    "               INNER JOIN order_hotels oh ON ohd.order_hotel_id = oh.id\n" +
                                    "      WHERE ((oh.check_in BETWEEN :checkInDate AND :checkOutDate)\n" +
                                    "          OR (oh.check_out BETWEEN :checkInDate AND :checkOutDate))\n" +
                                    "        AND oh.check_out != :checkInDate\n" +
                                    "        AND oh.check_in != :checkOutDate\n" +
                                    "        AND oh.id NOT IN (SELECT oh2.id\n" +
                                    "                          FROM order_hotels oh2\n" +
                                    "                          WHERE oh2.order_status = 4)\n" +
                                    "      GROUP BY rt.id, oh.check_in, oh.check_out, oh.order_status) AS subquery\n" +
                                    "GROUP BY subquery.room_type_id, subquery.checkIn, subquery.checkOut;")
                    .setParameter("checkInDate", newCheckIn)
                    .setParameter("checkOutDate", newCheckOut)
                    .getResultList();

            List<RoomOrder> roomOrders = new ArrayList<>();

            for (Object[] result : resultListAmountRoom) {
                BigDecimal maxCount = (BigDecimal) result[0];
                String roomId = (String) result[1];

                roomOrders.add(RoomOrder.builder()
                        .maxCount(maxCount.intValue())
                        .roomId(roomId)
                        .build());
            }

            List<String> roomTypeIds = roomOrders.stream().map(RoomOrder::getRoomId).distinct().toList();
            for (String roomTypeId : roomTypeIds) {

                RoomTypes roomType = entityManager.find(RoomTypes.class, roomTypeId);

                List<Integer> maxCounts = roomOrders.stream()
                        .filter(order -> order.getRoomId().equals(roomTypeId))
                        .map(RoomOrder::getMaxCount).toList();

                Optional<Integer> max = maxCounts.stream().max(Integer::compareTo);

                if (roomType != null) {
                    if(max.isPresent()) {
                        int remainingRooms = roomType.getAmountRoom() - max.get();
                        if (remainingRooms < 0) {
                            remainingRooms = 0;
                        }
                        roomType.setAmountRoom(remainingRooms);
                        entityManager.merge(roomType);
                    } else {
                        entityManager.merge(roomType);
                    }
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
            } else if ("05".equalsIgnoreCase(sort)) {
                query.orderBy(builder.desc(root.get("capacityAdults")));
            }
        } else {
            // Sắp xếp theo số lượng hóa đơn lớn nhất
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<OrderHotelDetails> subRoot = subquery.from(OrderHotelDetails.class);
            subquery.select(builder.count(subRoot.get("id")));
            subquery.where(builder.equal(subRoot.get("roomTypeId"), root.get("id")));

            query.orderBy(builder.desc(subquery.getSelection()));

        }

        if (hotelIdFilter != null && !hotelIdFilter.isEmpty()) {
            Join<RoomTypes, Hotels> hotelsJoin = root.join("hotelsByHotelId", JoinType.LEFT);
            predicates.add(builder.equal(hotelsJoin.get("id"), hotelIdFilter));
        }

        query.where(predicates.toArray(new Predicate[0]));

        roomTypeCustomerResult.setTotalCount(entityManager.createQuery(query).

                getResultList().

                size());
        roomTypeCustomerResult.setRoomTypes(
                entityManager.createQuery(query)
                                .

                        setFirstResult(page * size)
                                .

                        setMaxResults(size)
                                .

                        getResultList()
        );

        return roomTypeCustomerResult;
    }

    @Override
    public List<RoomTypes> findAllRoomTypeByIds(List<String> ids) {
        return roomTypesRepository.findByIdIn(ids);
    }

    @Override
    public void registerRoomType(RoomTypes roomType, String hotelId, List<Integer> roomTypeUtilities, MultipartFile roomTypeAvatar, List<MultipartFile> listRoomTypeImg, LocalTime checkinTime, LocalTime checkoutTime, Integer bedTypeId) throws IOException {
        String roomTypeAvatarUpload = fileUploadResize.uploadFileResize(roomTypeAvatar);
        String roomTypeId = GenerateNextID.generateNextCode("RT", roomTypeService.findMaxId());
        Collection<RoomImages> roomImages = new ArrayList<>();

        if (listRoomTypeImg != null && !listRoomTypeImg.isEmpty()) {
            for (MultipartFile roomTypeImage : listRoomTypeImg) {
                if (roomTypeImage != null) {
                    String roomTypeImageUpload = fileUpload.uploadFile(roomTypeImage);
                    RoomImages roomImage = new RoomImages();
                    roomImage.setRoomTypeId(roomType.getId());
                    roomImage.setRoomTypeImg(roomTypeImageUpload);
                    roomImages.add(roomImage);
                }
            }
        }

        Collection<RoomBeds> roomBeds = Collections.singleton(RoomBeds.builder()
                .roomTypeId(roomType.getId())
                .bedTypeId(bedTypeId)
                .build());
        List<RoomUtilities> roomUtilities = roomTypeUtilities.stream()
                .map(utilId -> {
                    return roomUtilitiesRepository.findById(utilId).orElseThrow(() -> new IllegalStateException("Không tìm thấy dịch vụ!"));
                })
                .toList();

        roomType.setId(roomTypeId);
        roomType.setHotelId(hotelId);
        roomType.setRoomTypeAvatar(roomTypeAvatarUpload);
        roomType.setCheckinTime(checkinTime);
        roomType.setCheckoutTime(checkoutTime);
        roomType.setRoomImagesById(roomImages);
        roomType.setRoomBedsById(roomBeds);
        roomType.setRoomUtilities(roomUtilities);

        roomTypesRepository.save(roomType);
    }

    @Override
    public void deleteAllRoomTypeByIds(List<String> roomTypeIds) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (roomTypeIds == null || roomTypeIds.isEmpty()) {
            throw new IllegalArgumentException(objectMapper.writeValueAsString(
                    Collections.singletonMap("message", "Dữ liệu không tồn tại!")));
        }

        List<RoomTypes> roomTypes = roomTypeIds.stream()
                .map(id -> roomTypesRepository.findById(id)
                        .orElseThrow(() -> {
                            try {
                                return new IllegalArgumentException(objectMapper.writeValueAsString(
                                        Collections.singletonMap("message", "Không có dữ liệu nào được tìm thấy")));
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        })
                )
                .peek(roomType -> roomType.setIsDeleted(true))
                .peek(roomType -> roomType.setDateDeleted(Timestamp.valueOf(LocalDateTime.now())))
                .toList();
        roomTypesRepository.saveAll(roomTypes);
    }


    @Override
    public void restoreAllRoomTypeByIds(List<String> roomTypeIds) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (roomTypeIds == null || roomTypeIds.isEmpty()) {
            throw new IllegalArgumentException(objectMapper.writeValueAsString(
                    Collections.singletonMap("message", "Dữ liệu không tồn tại!")));
        }

        List<RoomTypes> roomTypes = roomTypeIds.stream()
                .map(id -> roomTypesRepository.findById(id)
                        .orElseThrow(() -> {
                            try {
                                return new IllegalArgumentException(objectMapper.writeValueAsString(
                                        Collections.singletonMap("message", "Không có dữ liệu nào được tìm thấy")));
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        })
                )
                .peek(roomType -> roomType.setIsDeleted(false))
                .peek(roomType -> roomType.setDateDeleted(null))
                .toList();
        roomTypesRepository.saveAll(roomTypes);
    }


}
