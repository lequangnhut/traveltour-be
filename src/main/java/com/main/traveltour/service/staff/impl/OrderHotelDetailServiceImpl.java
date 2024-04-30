package com.main.traveltour.service.staff.impl;

import com.main.traveltour.dto.staff.OrderHotelDetailsDto;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.OrderHotelDetailsRepository;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderHotelDetailServiceImpl implements OrderHotelDetailService {
    @Autowired
    OrderHotelDetailsRepository repo;

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    private EntityManager entityManager;

    @Override
    public void save(OrderHotelDetails orderHotelDetails) {
        repo.save(orderHotelDetails);
    }

    @Override
    public void saveOrderHotelDetailsCustomer(OrderHotelDetails orderHotelDetails) {

        BigDecimal price = roomTypeService.findRoomTypeById(orderHotelDetails.getRoomTypeId()).get().getPrice();
        orderHotelDetails.setUnitPrice(price);
        repo.save(orderHotelDetails);
    }

    @Override
    public List<OrderHotelDetails> findByOrderHotelId(String orderHotelsId) {
        return repo.findOrderHotelDetailByOrderHotelId(orderHotelsId);
    }

    @Override
    public Boolean getTotalBookedRooms(String roomTypeId, Timestamp checkInDate, Timestamp checkOutDate, Integer amount) {
        // Phương thức tìm số lượng phòng còn lại
        String jpql = "SELECT SUM(ohd.amount) " +
                "FROM RoomTypes rt " +
                "INNER JOIN OrderHotelDetails ohd ON rt.id = ohd.roomTypeId " +
                "INNER JOIN OrderHotels oh ON ohd.orderHotelId = oh.id " +
                "WHERE oh.checkIn <= :checkOutDate " +
                "AND oh.checkOut >= :checkInDate " +
                "AND ohd.roomTypeId = :roomTypeId";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("checkOutDate", checkOutDate);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("roomTypeId", roomTypeId);

        Long totalBooked = (Long) query.getSingleResult();

        if (totalBooked != null ) {
            RoomTypes roomType = entityManager.find(RoomTypes.class, roomTypeId);
            Integer amountRoom = roomType.getAmountRoom();
            if((amountRoom - totalBooked) >= 0 && (amountRoom - totalBooked) - amount >= 0) {
                return amountRoom > totalBooked ;
            }
            return false;
        } else {
            return true;
        }
    }

}
