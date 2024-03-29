package com.main.traveltour.restcontroller.admin;

import com.main.traveltour.dto.admin.revenue.RevenueDtoAD;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.admin.AgencyServiceAD;
import com.main.traveltour.service.staff.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class RevenueADAPI {

    @Autowired
    UsersService usersService;
    @Autowired
    HotelServiceService hotelServiceService;
    @Autowired
    TransportationScheduleService transportationScheduleService;
    @Autowired
    VisitLocationService visitLocationService;
    @Autowired
    AgencyServiceAD agencyServiceAD;
    @Autowired
    BookingTourCustomerService bookingTourCustomerService;
    @Autowired
    TourDetailsService tourDetailsService;


    @GetMapping("/admin/revenue/count-all")
    public ResponseObject countRevenueDashbord(){
        Long findAmountAgencies = 0L;   Long findAmountUsers = 0L;
        Long findAmountTour = 0L;       Long findAmountHotels = 0L;
        Long findAmountTrans = 0L;      Long findAmountVisit = 0L;
        Long findAmountCustomer = 0L;

        findAmountAgencies = agencyServiceAD.countAgency();
        findAmountUsers = usersService.countUsers();
        findAmountTour = tourDetailsService.countTourDetails();
        findAmountHotels = hotelServiceService.countHotels();
        findAmountTrans = transportationScheduleService.countSchedules();
        findAmountVisit = visitLocationService.countVisit();
        findAmountCustomer = bookingTourCustomerService.countCustomer();

        RevenueDtoAD revenueDtoAD = new RevenueDtoAD();
        revenueDtoAD.setAmountAgencies(findAmountAgencies);
        revenueDtoAD.setAmountUsers(findAmountUsers + findAmountCustomer);
        revenueDtoAD.setAmountTour(findAmountTour);
        revenueDtoAD.setAmountHotels(findAmountHotels);
        revenueDtoAD.setAmountTrans(findAmountTrans);
        revenueDtoAD.setAmountVisit(findAmountVisit);


        return new ResponseObject("200", "Đã tìm thấy dữ liệu", revenueDtoAD);
    }

}
