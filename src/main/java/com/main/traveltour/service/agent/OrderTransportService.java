package com.main.traveltour.service.agent;

import com.main.traveltour.dto.agent.hotel.HotelRevenueDto;
import com.main.traveltour.dto.agent.hotel.StatisticalBookingHotelDto;
import com.main.traveltour.dto.agent.transport.StatiscalTransportBrandDto;
import com.main.traveltour.entity.OrderTransportations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderTransportService {

    String findMaxCode();

    OrderTransportations findById(String id);

    OrderTransportations save(OrderTransportations orderTransportations);

    Page<OrderTransportations> findAllOrderTransportAgent(String transportBrandId, String scheduleId, Pageable pageable);

    Page<OrderTransportations> findAllOrderTransportAgentWithSearch(String transportBrandId, String scheduleId, String searchTerm, Pageable pageable);

    List<Double> findStatisticalBookingTransport(Integer year, String hotelId);

    HotelRevenueDto findTransportRevenueStatistics(Integer year, String hotelId);

    List<Integer> findAllOrderHotelYear();

    List<StatiscalTransportBrandDto> statisticalTransportBrand(Integer year, String id);

    List<Integer> findAllOrderTransportYear();
}
