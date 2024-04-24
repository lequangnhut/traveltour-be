package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderTransportationsRepository extends JpaRepository<OrderTransportations, Integer> {

    @Query("SELECT MAX(ort.id) FROM OrderTransportations ort")
    String findMaxCode();

    OrderTransportations findById(String id);

    @Query("SELECT ort FROM OrderTransportations ort " +
            "JOIN ort.transportationSchedulesByTransportationScheduleId tsc " +
            "JOIN tsc.transportationsByTransportationId t " +
            "JOIN t.transportationBrandsByTransportationBrandId tbr " +
            "WHERE tbr.id = :transportBrandId AND tsc.id = :scheduleId AND ort.orderStatus = 0")
    Page<OrderTransportations> findAllOrderTransportAgent(@Param("transportBrandId") String transportBrandId, @Param("scheduleId") String scheduleId, Pageable pageable);

    @Query("SELECT ort FROM OrderTransportations ort " +
            "JOIN ort.transportationSchedulesByTransportationScheduleId tsc " +
            "JOIN tsc.transportationsByTransportationId t " +
            "JOIN t.transportationBrandsByTransportationBrandId tbr " +
            "WHERE LOWER(ort.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.transportationScheduleId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.customerCitizenCard) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.customerPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ort.customerEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(CAST(ort.dateCreated AS STRING) ) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND t.id = :transportBrandId AND tsc.id = :scheduleId AND ort.orderStatus = 0")
    Page<OrderTransportations> findAllOrderTransportAgentWithSearch(@Param("transportBrandId") String transportBrandId, @Param("scheduleId") String scheduleId, @Param("searchTerm") String searchTerm, Pageable pageable);

    List<OrderTransportations> findAllByTransportationScheduleId(String transportationScheduleId);

    @Query("SELECT otr FROM OrderTransportations otr WHERE otr.orderStatus = :orderStatus AND otr.customerEmail = :email")
    Page<OrderTransportations> findAllBookingTransByUserId(@Param("orderStatus") Integer orderStatus, @Param("email") String email, Pageable pageable);

    /**
     * truy vấn tìm kiếm tổng số lượng đơn hàng của mỗi trạng thái của 1 năm qua từng trạng thái
     * @param year năm cần tìm
     * @param id mã của xe
     * @return trả về số phần trăm của mỗi trạng thái
     */
    @Query(value = "SELECT YEAR(o.date_created)  AS year,\n" +
            "       MONTH(o.date_created) AS month,\n" +
            "       o.order_status,\n" +
            "       COUNT(*),\n" +
            "       ROUND((COUNT(*) / SUM(COUNT(*)) OVER (PARTITION BY YEAR(o.date_created), MONTH(o.date_created))) * 100, 1)\n" +
            "FROM order_transportations o\n" +
            "         INNER JOIN order_transportations_details od ON o.id = od.order_transportations_id\n" +
            "         INNER JOIN transportation_schedules ts ON o.transportation_schedule_id = ts.id\n" +
            "WHERE YEAR(o.date_created) = :year AND o.transportation_schedule_id = :id\n" +
            "GROUP BY YEAR(o.date_created), MONTH(o.date_created), o.order_status", nativeQuery = true)
    List<Object[]> findStatisticalBookingTransport(Integer year, String id);

    /**
     * truy vấn tổng số tiền qua từng tháng của 1 năm
     * @param year năm
     * @param id mã của hãng xe
     * @return trả về tổng số tiền qua mỗi tháng
     */
    @Query(value = "SELECT YEAR(o.date_created)  AS year,\n" +
            "       MONTH(o.date_created) AS month,\n" +
            "       SUM(o.order_total) AS amount\n" +
            "FROM order_transportations o\n" +
            "         INNER JOIN order_transportations_details od ON o.id = od.order_transportations_id\n" +
            "         INNER JOIN transportation_schedules ts ON o.transportation_schedule_id = ts.id\n" +
            "WHERE YEAR(o.date_created) = :year AND o.transportation_schedule_id = :id AND o.order_status = 1\n" +
            "GROUP BY YEAR(o.date_created), MONTH(o.date_created), o.order_status", nativeQuery = true)
    List<Object[]> findTransportRevenueStatistics(Integer year, String id);
}