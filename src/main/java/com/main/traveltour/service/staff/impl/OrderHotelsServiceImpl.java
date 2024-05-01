package com.main.traveltour.service.staff.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.traveltour.dto.agent.hotel.HotelRevenueDto;
import com.main.traveltour.dto.agent.hotel.LastYearRevenueDto;
import com.main.traveltour.dto.agent.hotel.RevenueThisYearDto;
import com.main.traveltour.dto.agent.hotel.StatisticalBookingHotelDto;
import com.main.traveltour.dto.agent.hotel.order.OrderHotelDetailsDto;
import com.main.traveltour.dto.agent.hotel.order.OrderHotelDto;
import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.dto.customer.infomation.CancelOrderHotelsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.enums.OrderStatus;
import com.main.traveltour.repository.OrderHotelsRepository;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import com.main.traveltour.service.staff.OrderHotelsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.ChangeCheckInTimeService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateOrderCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderHotelsServiceImpl implements OrderHotelsService {
    @Autowired
    OrderHotelsRepository orderHotelsRepository;
    @Autowired
    OrderHotelsService orderHotelsService;
    @Autowired
    OrderHotelDetailService orderHotelDetailService;
    @Autowired
    RoomTypeService roomTypeService;
    @Autowired
    ChangeCheckInTimeService changeCheckInTimeService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EmailService emailService;

    @Override
    public String maxCodeTourId() {
        return orderHotelsRepository.maxCodeTourId();
    }

    @Override
    public OrderHotels save(OrderHotels orderHotels) {
        return orderHotelsRepository.save(orderHotels);
    }

    @Override
    public void saveOrderHotelCustomer(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel) {
        Timestamp newCheckIn = changeCheckInTimeService.changeCheckInTime(orderHotels.getCheckIn());
        Timestamp newCheckOut = changeCheckInTimeService.changeCheckInTime(orderHotels.getCheckOut());

        BigDecimal orderTotal = orderDetailsHotel.stream()
                .map(orderDetails -> {
                    Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(orderDetails.getRoomTypeId());
                    if (roomTypes.isPresent()) {
                        BigDecimal roomPrice = roomTypes.get().getPrice();
                        BigDecimal amount = BigDecimal.valueOf(orderDetails.getAmount());
                        return roomPrice.multiply(BigDecimal.valueOf(changeCheckInTimeService.getDaysDifference(newCheckIn, newCheckOut))).multiply(amount);
                    } else {
                        return BigDecimal.ZERO;
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderHotels.setOrderTotal(orderTotal);
        orderHotels.setOrderCode(orderHotels.getId());
        orderHotels.setOrderStatus(OrderStatus.PENDING.getValue());
        orderHotels.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        orderHotels.setCheckIn(newCheckIn);
        orderHotels.setCheckOut(newCheckOut);
        orderHotelsRepository.save(orderHotels);
    }

    @Override
    public void saveOrderHotelAgent(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel) {
        Timestamp newCheckIn = changeCheckInTimeService.changeCheckInTime(orderHotels.getCheckIn());
        Timestamp newCheckOut = changeCheckInTimeService.changeCheckInTime(orderHotels.getCheckOut());

        BigDecimal orderTotal = orderDetailsHotel.stream()
                .map(orderDetails -> {
                    Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(orderDetails.getRoomTypeId());
                    if (roomTypes.isPresent()) {
                        BigDecimal roomPrice = roomTypes.get().getPrice();
                        BigDecimal amount = BigDecimal.valueOf(orderDetails.getAmount());
                        return roomPrice.multiply(BigDecimal.valueOf(changeCheckInTimeService.getDaysDifference(newCheckIn, newCheckOut))).multiply(amount);
                    } else {
                        return BigDecimal.ZERO;
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderHotels.setOrderTotal(orderTotal);
        orderHotels.setOrderCode(orderHotels.getId());
        orderHotels.setOrderStatus(OrderStatus.SUCCESSFUL.getValue());
        orderHotels.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        orderHotels.setCheckIn(newCheckIn);
        orderHotels.setCheckOut(newCheckOut);
        orderHotelsRepository.save(orderHotels);
    }

    @Override
    public void saveOrderHotelPaymentOnlineCustomer(OrderHotels orderHotels, List<OrderDetailsHotelCustomerDto> orderDetailsHotel) {
        Timestamp newCheckIn = changeCheckInTimeService.changeCheckInTime(orderHotels.getCheckIn());
        Timestamp newCheckOut = changeCheckInTimeService.changeCheckInTime(orderHotels.getCheckOut());
        BigDecimal orderTotal = orderDetailsHotel.stream()
                .map(orderDetails -> {
                    Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(orderDetails.getRoomTypeId());
                    if (roomTypes.isPresent()) {
                        BigDecimal roomPrice = roomTypes.get().getPrice();
                        BigDecimal amount = BigDecimal.valueOf(orderDetails.getAmount());
                        return roomPrice.multiply(BigDecimal.valueOf(changeCheckInTimeService.getDaysDifference(newCheckIn, newCheckOut))).multiply(amount);
                    } else {
                        return BigDecimal.ZERO;
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderHotels.setOrderTotal(orderTotal);
        orderHotels.setId(GenerateOrderCode.generateCodePayment(orderHotels.getPaymentMethod()));
        orderHotels.setOrderCode(orderHotels.getId());
        orderHotels.setOrderStatus(OrderStatus.PROCESSING.getValue());
        orderHotels.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        orderHotels.setCheckIn(newCheckIn);
        orderHotels.setCheckOut(newCheckOut);
        orderHotelsRepository.save(orderHotels);
    }


    @Override
    public Page<OrderHotels> getAllByUserId(Integer orderStatus, String email, Pageable pageable) {
        return orderHotelsRepository.findAllBookingHotelsByUserId(orderStatus, email, pageable);
    }

    @Override
    public OrderHotels findById(String id) {
        return orderHotelsRepository.findById(id);
    }

    @Override
    public Optional<OrderHotels> findByIdOptional(String orderId) {
        return Optional.ofNullable(orderHotelsRepository.findById(orderId));
    }

    @Override
    public Page<OrderHotels> findOrderByIds(List<String> orderIds, Pageable pageable) {
        return orderHotelsRepository.findByIdIn(orderIds, pageable);
    }

    @Override
    public List<Double> findStatisticalBookingHotel(Integer year, String hotelId) {
        Objects.requireNonNull(year, "{\"message\": \"Không được bỏ trống dữ liệu\"}");
        Objects.requireNonNull(hotelId, "{\"message\": \"Không được bỏ trống dữ liệu\"}");

        List<Double> results = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        List<Object[]> statisticalBookingHotelDtos = orderHotelsRepository.findStatisticalBookingHotel(year, hotelId);

        for (Object[] order : statisticalBookingHotelDtos) {
            Integer orderStatus = (Integer) order[2];
            BigDecimal orderCountPercentage = (BigDecimal) order[4];

            if (orderStatus >= 0 && orderStatus <= 5) {
                results.set(orderStatus, Double.valueOf(String.valueOf(orderCountPercentage)));
            }
        }

        return results;
    }

    @Override
    public OrderHotelDto findByOrderHotelId(String orderId) {
        Objects.requireNonNull(orderId, "{\"message: \" \"Không được bỏ trống mã đơn hàng\"}");
        OrderHotels orderHotels = orderHotelsRepository.findById(orderId);
        List<OrderHotelDetails> orderHotelDetails = orderHotelDetailService.findByOrderHotelId(orderId);

        OrderHotelDto orderHotelDto = EntityDtoUtils.convertToDto(orderHotels, OrderHotelDto.class);
        List<OrderHotelDetailsDto> orderHotelDetailsDtos = EntityDtoUtils.convertToDtoList(orderHotelDetails, OrderHotelDetailsDto.class);
        orderHotelDetailsDtos.stream()
                .map(orderHotelDetail -> {
                    RoomTypes roomTypes = roomTypeService.findRoomTypeById(orderHotelDetail.getRoomTypeId()).orElseThrow(() -> new IllegalStateException("{ \"message: \" \"Không tìm thấy loại phòng\"}"));
                    orderHotelDetail.setRoomTypesByRoomTypeId(roomTypes);
                    return orderHotelDetail;
                });
        orderHotelDto.setOrderHotelDetailsById(orderHotelDetails);


        return orderHotelDto;
    }

    @Override
    public void confirmInvoiceByIdOrder(String orderId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Objects.requireNonNull(orderId, objectMapper.writeValueAsString(Collections.singletonMap("message", "Không có dữ liệu được tìm thấy")));

        OrderHotels orderHotel = orderHotelsService.findByIdOptional(orderId)
                .orElseThrow(() -> {
                    try {
                        return new IllegalStateException(objectMapper.writeValueAsString(Collections.singletonMap("message", "Không có dữ liệu")));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        orderHotel.setOrderStatus(2);
        orderHotelsService.save(orderHotel);
    }

    @Override
    public void cancelInvoiceByIdOrder(String orderId, String cancelReason) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Objects.requireNonNull(orderId, objectMapper.writeValueAsString(Collections.singletonMap("message", "Không có dữ liệu được tìm thấy")));

        OrderHotels orderHotel = orderHotelsService.findByIdOptional(orderId)
                .orElseThrow(() -> {
                    try {
                        return new IllegalStateException(objectMapper.writeValueAsString(Collections.singletonMap("message", "Không có dữ liệu")));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        orderHotel.setOrderNote(cancelReason);
        orderHotel.setOrderStatus(5);
        orderHotelsService.save(orderHotel);
    }

    @Override
    public Page<OrderHotels> findOrderHotelsByFilter(String hotelId, LocalDate targetTimestamp,  String searchTerm, Integer orderStatus, Pageable pageable) {
        return orderHotelsRepository.findOrderHotelsByFilter(hotelId, targetTimestamp, searchTerm, orderStatus, pageable);
    }

    @Override
    public void checkOrderHotelExpires() {
        List<OrderHotels> orderHotelsExpires = orderHotelsRepository.findOrderHotelsExpires();

        for (OrderHotels orderHotels : orderHotelsExpires) {
            int orderStatus = orderHotels.getOrderStatus();
            String orderNote = null;

            if (orderStatus == 0) {
                orderStatus = 4;
                orderNote = "Bạn chưa thanh toán";

            } else if (orderStatus == 1) {
                orderStatus = 5;
                orderNote = "Khách sạn chưa chuẩn bị phòng";
            } else if (orderStatus == 2) {
                orderStatus = 4;
            }

            if (orderHotels.getOrderStatus() != orderStatus || orderNote != null) {
                orderHotels.setOrderStatus(orderStatus);
                orderHotels.setOrderNote(orderNote);
                saveOrderHotels(orderHotels);
            }
        }
    }

    public void saveOrderHotels(OrderHotels orderHotels) {
        String sql = "UPDATE order_hotels SET order_status = ?, order_note = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderHotels.getOrderStatus(), orderHotels.getOrderNote(), orderHotels.getId());
    }




    @Override
    public List<StatisticalBookingHotelDto> findStatisticalRoomTypeHotel(Integer year, String hotelId) {
        Objects.requireNonNull(year, "{\"message\": \"Không được bỏ trống dữ liệu\"}");
        Objects.requireNonNull(hotelId, "{\"message\": \"Không được bỏ trống dữ liệu\"}");

        List<StatisticalBookingHotelDto> result = new ArrayList<>();
        List<Object[]> statisticalRoomTypeHotel = orderHotelsRepository.statisticalRoomTypeHotel(year, hotelId);

        // Tạo mảng 12 phần tử để lưu thông tin của từng tháng
        StatisticalBookingHotelDto[] monthlyStats = new StatisticalBookingHotelDto[12];
        Arrays.fill(monthlyStats, null);

        for (Object[] object : statisticalRoomTypeHotel) {
            String id = (String) object[0];
            String roomTypeName = (String) object[1];
            Integer years = (Integer) object[2];
            Integer month = (Integer) object[3];
            Integer orderStatus = (Integer) object[4];
            Long countRoomType = (Long) object[5];

            if (month >= 1 && month <= 12) {
                if (monthlyStats[month - 1] == null) {
                    monthlyStats[month - 1] = StatisticalBookingHotelDto.builder()
                            .id(id)
                            .roomTypeName(roomTypeName)
                            .year(years)
                            .month(month)
                            .orderStatus(orderStatus)
                            .countRoomType(Math.toIntExact(countRoomType))
                            .build();
                } else {
                    StatisticalBookingHotelDto existingStat = monthlyStats[month - 1];
                    existingStat.setCountRoomType(existingStat.getCountRoomType() + Math.toIntExact(countRoomType));
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            if (monthlyStats[i] == null) {
                result.add(StatisticalBookingHotelDto.builder()
                        .id("")
                        .roomTypeName("")
                        .year(year)
                        .month(i + 1)
                        .orderStatus(0)
                        .countRoomType(0)
                        .build());
            } else {
                result.add(monthlyStats[i]);
            }
        }

        return result;
    }

    @Override
    public HotelRevenueDto findHotelRevenueStatistics(Integer year, String hotelId) {
        Objects.requireNonNull(year, "{\"message\": \"Không được bỏ trống dữ liệu\"}");
        Objects.requireNonNull(hotelId, "{\"message\": \"Không được bỏ trống dữ liệu\"}");
        HotelRevenueDto hotelRevenueDto = new HotelRevenueDto();

        List<RevenueThisYearDto> revenueThisYearDtos = new ArrayList<>();
        List<LastYearRevenueDto> lastYearRevenueDtos = new ArrayList<>();

        RevenueThisYearDto[] monthlyRevenue = new RevenueThisYearDto[12];
        LastYearRevenueDto[] monthlyLastRevenue = new LastYearRevenueDto[12];

        Arrays.fill(monthlyRevenue, null);
        Arrays.fill(monthlyLastRevenue, null);

        List<Object[]> revenues = orderHotelsRepository.findHotelRevenueStatistics(year, hotelId);
        List<Object[]> lastRevenues = orderHotelsRepository.findHotelRevenueStatistics(year - 1, hotelId);

        for (Object[] object : revenues) {
            Integer years = (Integer) object[0];
            Integer month = (Integer) object[1];
            BigDecimal totalRoomType = (BigDecimal) object[2];

            if (month >= 1 && month <= 12) {
                if (monthlyRevenue[month - 1] == null) {
                    monthlyRevenue[month - 1] = RevenueThisYearDto.builder()
                            .year(years)
                            .month(month)
                            .totalRoomType(totalRoomType)
                            .build();
                } else {
                    RevenueThisYearDto existingStat = monthlyRevenue[month - 1];
                    existingStat.setTotalRoomType(existingStat.getTotalRoomType().add(totalRoomType));
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            if (monthlyRevenue[i] == null) {
                revenueThisYearDtos.add(RevenueThisYearDto.builder()
                        .year(year)
                        .month(i + 1)
                        .totalRoomType(BigDecimal.valueOf(0.0))
                        .build());
            } else {
                revenueThisYearDtos.add(monthlyRevenue[i]);
            }
        }

        for (Object[] object : lastRevenues) {
            Integer years = (Integer) object[0];
            Integer month = (Integer) object[1];
            BigDecimal totalRoomType = (BigDecimal) object[2];

            if (month >= 1 && month <= 12) {
                if (monthlyLastRevenue[month - 1] == null) {
                    monthlyLastRevenue[month - 1] = LastYearRevenueDto.builder()
                            .year(years)
                            .month(month)
                            .totalRoomType(totalRoomType)
                            .build();
                } else {
                    LastYearRevenueDto existingStat = monthlyLastRevenue[month - 1];
                    existingStat.setTotalRoomType(existingStat.getTotalRoomType().add(totalRoomType));
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            if (monthlyLastRevenue[i] == null) {
                lastYearRevenueDtos.add(LastYearRevenueDto.builder()
                        .year(year - 1)
                        .month(i + 1)
                        .totalRoomType(BigDecimal.valueOf(0.0))
                        .build());
            } else {
                lastYearRevenueDtos.add(monthlyLastRevenue[i]);
            }
        }

        List<BigDecimal> arrRevenue = revenueThisYearDtos.stream()
                .map(RevenueThisYearDto::getTotalRoomType).collect(Collectors.toList());

        List<BigDecimal> arrLastRevenue = lastYearRevenueDtos.stream()
                .map(LastYearRevenueDto::getTotalRoomType).collect(Collectors.toList());

        return HotelRevenueDto.builder()
                .revenue(arrRevenue)
                .lastYearRevenue(arrLastRevenue)
                .build();
    }

    @Override
    public List<Integer> getAllOrderHotelYear() {
        return orderHotelsRepository.getAllOrderHotelYear();
    }


}
