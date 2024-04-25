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
            "       COUNT(*)              AS orderCount,\n" +
            "       ROUND((COUNT(*) / SUM(COUNT(*)) OVER (PARTITION BY YEAR(o.date_created), MONTH(o.date_created))) * 100,\n" +
            "             1)              AS orderCountPercentage\n" +
            "FROM order_transportations o\n" +
            "         INNER JOIN order_transportations_details od ON o.id = od.order_transportations_id\n" +
            "         INNER JOIN transportation_schedules ts ON o.transportation_schedule_id = ts.id\n" +
            "         INNER JOIN transportations t ON ts.transportation_id = t.id\n" +
            "         INNER JOIN transportation_brands tb ON t.transportation_brand_id = tb.id\n" +
            "WHERE YEAR(o.date_created) = :year\n" +
            "  AND t.transportation_brand_id = :id\n" +
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
            "         INNER JOIN transportations t ON ts.transportation_id = t.id\n" +
            "         INNER JOIN transportation_brands tb ON t.transportation_brand_id = tb.id\n" +
            "WHERE YEAR(o.date_created) = :year\n" +
            "  AND tb.id = :id\n" +
            "  AND o.order_status = 1\n" +
            "GROUP BY YEAR(o.date_created), MONTH(o.date_created), o.order_status", nativeQuery = true)
    List<Object[]> findTransportRevenueStatistics(Integer year, String id);

    /**
     * Tìm kiếm chuyến xe có lượt khách hàng đi nhiều nhất trong 1 tháng
     * @param year 2024
     * @param id mã chuyến xe
     * @return trả về danh sách chuyến xe được đi nhiều nhất trong tháng
     */
    @Query(value = "SELECT year,\n" +
            "       month,\n" +
            "       formLocations,\n" +
            "       toLocations,\n" +
            "       MAX(amount) AS maxAmount\n" +
            "FROM (SELECT YEAR(o.date_created)  AS year,\n" +
            "             MONTH(o.date_created) AS month,\n" +
            "             ts.from_location      AS formLocations,\n" +
            "             ts.to_location        AS toLocations,\n" +
            "             COUNT(od.id)          AS amount\n" +
            "      FROM order_transportations o\n" +
            "               INNER JOIN order_transportations_details od ON o.id = od.order_transportations_id\n" +
            "               INNER JOIN transportation_schedules ts ON o.transportation_schedule_id = ts.id\n" +
            "               INNER JOIN transportations t ON ts.transportation_id = t.id\n" +
            "               INNER JOIN transportation_brands tb ON t.transportation_brand_id = tb.id\n" +
            "      WHERE YEAR(o.date_created) = :year\n" +
            "        AND t.transportation_brand_id = :id\n" +
            "        AND o.order_status = 1\n" +
            "      GROUP BY YEAR(o.date_created), MONTH(o.date_created), ts.from_location, ts.to_location) AS subquery\n" +
            "GROUP BY year, month, formLocations, toLocations;", nativeQuery = true)
    List<Object[]> statisticalTransportBrand(Integer year, String id);
}