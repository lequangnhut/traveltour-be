package com.main.traveltour.restcontroller.agent.visitlocation;

import com.main.traveltour.dto.agent.visit_location.OrderVisitsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.agent.OrderVisitDetailService;
import com.main.traveltour.service.agent.OrderVisitService;
import com.main.traveltour.service.agent.VisitLocationTicketService;
import com.main.traveltour.service.agent.VisitLocationsService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class OrderVisitAPI {

    @Autowired
    private OrderVisitService orderVisitService;

    @Autowired
    private VisitLocationsService visitLocationsService;

    @Autowired
    private OrderVisitDetailService orderVisitDetailService;

    @Autowired
    private VisitLocationTicketService visitLocationTicketService;

    @GetMapping("/agent/order-visit/find-all-order-visit/{brandId}")
    private ResponseEntity<Page<OrderVisits>> findAllTransportBrand(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String sortDir,
                                                                    @RequestParam(required = false) String searchTerm,
                                                                    @PathVariable String brandId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<OrderVisits> orderTransportations = searchTerm == null || searchTerm.isEmpty()
                ? orderVisitService.findAllOrderVisits(brandId, PageRequest.of(page, size, sort))
                : orderVisitService.findAllOrderVisitsWithSearch(brandId, searchTerm, PageRequest.of(page, size, sort));
        return new ResponseEntity<>(orderTransportations, HttpStatus.OK);
    }

    @GetMapping("/agent/order-visit/find-by-order-visit-id/{orderVisitId}")
    private ResponseObject findByOrderVisitId(@PathVariable String orderVisitId) {
        OrderVisits orderVisits = orderVisitService.findById(orderVisitId);

        if (orderVisits == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", orderVisits);
        }
    }

    @GetMapping("/agent/order-visit/find-by-visit-ticket-id/{visitTicketId}")
    private ResponseObject findVisitTicket(@PathVariable int visitTicketId) {
        VisitLocationTickets tickets = visitLocationTicketService.findByVisitTicketId(visitTicketId);

        if (tickets == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tickets);
        }
    }

    @GetMapping("/agent/order-visit/find-by-visit-location-id/{visitLocationId}")
    private ResponseObject findVisitLocationId(@PathVariable String visitLocationId) {
        VisitLocations visitLocations = visitLocationsService.findByVisitLocationId(visitLocationId);

        if (visitLocations == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocations);
        }
    }

    @GetMapping("/agent/order-visit/find-all-visit-location/{visitLocationId}")
    private ResponseObject findByVisitLocationId(@PathVariable String visitLocationId) {
        List<VisitLocations> visitLocations = visitLocationsService.findAllByVisitLocationId(visitLocationId);

        if (visitLocations == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocations);
        }
    }

    @PostMapping("/agent/order-visit/create-order-visit")
    private void createOrderVisit(@RequestBody OrderVisitsDto orderVisitsDto) {
        String orderVisitId = GenerateNextID.generateNextCode("OVS", orderVisitService.findMaxCode());

        OrderVisits orderVisits = EntityDtoUtils.convertToEntity(orderVisitsDto, OrderVisits.class);
        orderVisits.setId(orderVisitId);
        if (orderVisits.getCapacityAdult() == null && orderVisits.getCapacityKid() == null) {
            orderVisits.setCapacityAdult(orderVisitsDto.getCapacityFree());
            orderVisits.setCapacityKid(0);
        }
        orderVisits.setDateCreated(new Timestamp(System.currentTimeMillis()));
        orderVisits.setOrderStatus(0); // Đã tạo vé

        orderVisitService.save(orderVisits);

        createOrderVisitDetail(orderVisitId, orderVisitsDto);
    }

    @PutMapping("/agent/order-visit/update-order-visit")
    private void updateOrderVisit(@RequestBody OrderVisitsDto orderVisitsDto) {
        OrderVisits orderVisits = EntityDtoUtils.convertToEntity(orderVisitsDto, OrderVisits.class);
        orderVisits.setId(orderVisitsDto.getId());
        if (orderVisits.getCapacityAdult() == null && orderVisits.getCapacityKid() == null) {
            orderVisits.setCapacityAdult(orderVisitsDto.getCapacityFree());
            orderVisits.setCapacityKid(0);
        }

        orderVisitService.save(orderVisits);
    }

    @GetMapping("/agent/order-visit/delete-order-visit/{orderVisitId}")
    private void deleteOrderVisit(@PathVariable String orderVisitId) {
        OrderVisits orderVisits = orderVisitService.findById(orderVisitId);
        orderVisits.setOrderStatus(1); // Đã hủy vé
        orderVisitService.save(orderVisits);
    }

    private void createOrderVisitDetail(String orderVisitId, OrderVisitsDto orderVisitsDto) {
        String visitLocationId = orderVisitsDto.getVisitLocationId();
        List<String> ticketTypeNames = Arrays.asList("Vé người lớn", "Vé trẻ em", "Miễn phí vé");

        for (String ticketTypeName : ticketTypeNames) {
            Integer amount = null;

            switch (ticketTypeName) {
                case "Vé người lớn":
                    amount = orderVisitsDto.getCapacityAdult();
                    break;
                case "Vé trẻ em":
                    amount = orderVisitsDto.getCapacityKid();
                    break;
                case "Miễn phí vé":
                    amount = orderVisitsDto.getCapacityFree();
                    break;
            }

            if (amount != null) {
                VisitLocationTickets tickets = visitLocationTicketService.findByTicketTypeNameAndLocationId(ticketTypeName, visitLocationId);

                if (tickets != null) {
                    OrderVisitDetails orderVisitDetails = new OrderVisitDetails();
                    setOrderVisitDetails(orderVisitDetails, orderVisitId, tickets.getId(), amount);
                    orderVisitDetailService.save(orderVisitDetails);
                }
            }
        }
    }

    private void setOrderVisitDetails(OrderVisitDetails orderVisitDetails, String orderVisitId, int visitLocationTicketId, Integer amount) {
        if (orderVisitDetails != null && orderVisitId != null) {
            orderVisitDetails.setOrderVisitId(orderVisitId);
            orderVisitDetails.setVisitLocationTicketId(visitLocationTicketId);
            orderVisitDetails.setAmount(amount);
        }
    }
}
