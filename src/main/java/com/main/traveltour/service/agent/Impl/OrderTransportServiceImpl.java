package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.dto.agent.hotel.HotelRevenueDto;
import com.main.traveltour.dto.agent.hotel.LastYearRevenueDto;
import com.main.traveltour.dto.agent.hotel.RevenueThisYearDto;
import com.main.traveltour.dto.agent.transport.StatiscalTransportBrandDto;
import com.main.traveltour.dto.agent.transport.TransportRevenueDto;
import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.repository.OrderTransportationsRepository;
import com.main.traveltour.service.agent.OrderTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderTransportServiceImpl implements OrderTransportService {

    @Autowired
    private OrderTransportationsRepository repo;

    @Override
    public String findMaxCode() {
        return repo.findMaxCode();
    }

    @Override
    public OrderTransportations findById(String id) {
        return repo.findById(id);
    }

    @Override
    public OrderTransportations save(OrderTransportations orderTransportations) {
        return repo.save(orderTransportations);
    }

    @Override
    public Page<OrderTransportations> findAllOrderTransportAgent(String transportBrandId, String scheduleId, Pageable pageable) {
        return repo.findAllOrderTransportAgent(transportBrandId, scheduleId, pageable);
    }

    @Override
    public Page<OrderTransportations> findAllOrderTransportAgentWithSearch(String transportBrandId, String scheduleId, String searchTerm, Pageable pageable) {
        return repo.findAllOrderTransportAgentWithSearch(transportBrandId, scheduleId, searchTerm, pageable);
    }

    @Override
    public List<Double> findStatisticalBookingTransport(Integer year, String hotelId) {
        Objects.requireNonNull(year, "{\"message\": \"Không được bỏ trống dữ liệu\"}");
        Objects.requireNonNull(hotelId, "{\"message\": \"Không được bỏ trống dữ liệu\"}");

        List<Double> results = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
        List<Object[]> statisticalBookingTransport = repo.findStatisticalBookingTransport(year, hotelId);

        for (Object[] order : statisticalBookingTransport) {
            Integer orderStatus = (Integer) order[2];
            BigDecimal orderCountPercentage = (BigDecimal) order[4];

            if (orderStatus >= 0 && orderStatus <= 2) {
                results.set(orderStatus, Double.valueOf(String.valueOf(orderCountPercentage)));
            }
        }

        return results;
    }

    @Override
    public HotelRevenueDto findTransportRevenueStatistics(Integer year, String hotelId) {
        Objects.requireNonNull(year, "{\"message\": \"Không được bỏ trống dữ liệu\"}");
        Objects.requireNonNull(hotelId, "{\"message\": \"Không được bỏ trống dữ liệu\"}");
        TransportRevenueDto hotelRevenueDto = new TransportRevenueDto();

        List<RevenueThisYearDto> revenueThisYearDtos = new ArrayList<>();
        List<LastYearRevenueDto> lastYearRevenueDtos = new ArrayList<>();

        RevenueThisYearDto[] monthlyRevenue = new RevenueThisYearDto[12];
        LastYearRevenueDto[] monthlyLastRevenue = new LastYearRevenueDto[12];

        Arrays.fill(monthlyRevenue, null);
        Arrays.fill(monthlyLastRevenue, null);

        List<Object[]> revenues = repo.findTransportRevenueStatistics(year, hotelId);
        List<Object[]> lastRevenues = repo.findTransportRevenueStatistics(year - 1, hotelId);

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
    public List<Integer> findAllOrderHotelYear() {
        return null;
    }

    @Override
    public List<StatiscalTransportBrandDto> statisticalTransportBrand(Integer year, String id) {
        Objects.requireNonNull(year, "{\"message\": \"Không được bỏ trống dữ liệu\"}");
        Objects.requireNonNull(id, "{\"message\": \"Không được bỏ trống dữ liệu\"}");

        List<StatiscalTransportBrandDto> result = new ArrayList<>();
        List<Object[]> statisticalTransportBrand = repo.statisticalTransportBrand(year, id);

        // Tạo mảng 12 phần tử để lưu thông tin của từng tháng
        StatiscalTransportBrandDto[] monthlyStats = new StatiscalTransportBrandDto[12];
        Arrays.fill(monthlyStats, null);

        for (Object[] object : statisticalTransportBrand) {
            Integer years = (Integer) object[0];
            Integer month = (Integer) object[1];
            String formLocation = (String) object[2];
            String toLocation = (String) object[3];
            BigDecimal maxAmount = (BigDecimal) object[4];

            if (month >= 1 && month <= 12) {
                if (monthlyStats[month - 1] == null) {
                    monthlyStats[month - 1] = StatiscalTransportBrandDto.builder()
                            .year(years)
                            .month(month)
                            .formLocation(formLocation)
                            .toLocation(toLocation)
                            .maxAmount(maxAmount)
                            .build();
                } else {
                    StatiscalTransportBrandDto existingStat = monthlyStats[month - 1];
                    existingStat.setMaxAmount(existingStat.getMaxAmount());
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            if (monthlyStats[i] == null) {
                result.add(StatiscalTransportBrandDto.builder()
                        .year(year)
                        .month(i + 1)
                        .formLocation("")
                        .toLocation("")
                        .maxAmount(BigDecimal.valueOf(0))
                        .build());
            } else {
                result.add(monthlyStats[i]);
            }
        }
        return result;
    }

    @Override
    public List<Integer> findAllOrderTransportYear() {
        return repo.findAllOrderTransportYear();
    }
}
