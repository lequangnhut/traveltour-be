package com.main.traveltour.restcontroller.agent.hotel;

import com.main.traveltour.dto.agent.hotel.order.OrderHotelDto;
import com.main.traveltour.dto.customer.infomation.OrderHotelsDto;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import com.main.traveltour.service.staff.OrderHotelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class OrderHotelAgentAPI {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private OrderHotelsService orderHotelsService;

    @Autowired
    private OrderHotelDetailService orderHotelDetailService;


    /**
     * Phương thức tìm kiếm hóa đơn của khách sạn
     *
     * @param hotelId mã khách sạn
     * @return danh sách hóa đơn khách sạn
     */
    @GetMapping("/agent/order-hotel/findAllOrderHotel")
    public ResponseObject findAllOrderHotel(
            @RequestParam("hotelId") String hotelId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(value = "sortField", defaultValue = "id", required = false) String sortField,
            @RequestParam(value = "sortDirection", required = false) Sort.Direction sortDirection,
            @RequestParam(value = "searchTerm", required = false) String searchTerm

    ) {
        List<String> roomType = roomTypeService.findAllByHotelId(hotelId).stream().map(RoomTypes::getId).toList();
        List<String> orderHotelDetails = orderHotelDetailService.findOrderHotelByRoomTypeIds(roomType).stream().map(OrderHotelDetails::getOrderHotelId).toList();

        // Xác định hướng sắp xếp mặc định nếu sortDirection là null
        Sort.Direction defaultSortDirection = Sort.Direction.ASC;

        // Xác định hướng sắp xếp cuối cùng
        Sort.Direction finalSortDirection = sortDirection != null ? sortDirection : defaultSortDirection;

        Sort sort = sortField != null ? Sort.by(finalSortDirection, sortField) : null;
        assert sort != null;

        Page<OrderHotels> orderHotels = orderHotelsService.findOrderByIds(orderHotelDetails, PageRequest.of(page, size, sort));
        return new ResponseObject("200", "success", orderHotels);
    }

    @GetMapping("agent/order-hotel/findOrderHotelById")
    public ResponseEntity<OrderHotelDto> findOrderHotelById(@RequestParam("orderId") String orderId) {
        OrderHotelDto orderHotelsDto = orderHotelsService.findByOrderHotelId(orderId);
        return ResponseEntity.ok(orderHotelsDto);
    }
}
