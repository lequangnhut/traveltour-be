package com.main.traveltour.repository;

import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TransportationSchedulesRepository extends JpaRepository<TransportationSchedules, Integer> {

    @Query("SELECT MAX(t.id) FROM TransportationSchedules t")
    String fixMaxCode();

    TransportationSchedules findById(String id);

    List<TransportationSchedules> findByTransportationId(String transportId);

    List<TransportationSchedules> findByDepartureTimeAndIsActiveTrue(Timestamp departureTime);

    List<TransportationSchedules> findByArrivalTimeAndIsActiveTrue(Timestamp arrivalTime);

    @Query("SELECT t FROM TransportationSchedules t " +
            "JOIN t.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE tpb.id = :transportBrandId AND t.tripType = false")
    List<TransportationSchedules> findAllScheduleByBrandId(@Param("transportBrandId") String transportBrandId);

    @Query("SELECT t FROM TransportationSchedules t " +
            "JOIN t.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE tpb.id = :transportBrandId")
    Page<TransportationSchedules> findAllSchedules(@Param("transportBrandId") String transportBrandId, Pageable pageable);

    @Query("SELECT t FROM TransportationSchedules t " +
            "JOIN t.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE LOWER(CAST(t.unitPrice AS STRING)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.toLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.fromLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND tpb.id = :transportBrandId")
    Page<TransportationSchedules> findAllSchedulesWithSearch(@Param("transportBrandId") String transportBrandId, @Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT t FROM TransportationSchedules t " +
            "JOIN t.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE t.tripType = true AND t.isActive = true")
    Page<TransportationSchedules> getAllTransportationSchedules(Pageable pageable);

    @Query(value = "SELECT ts.* " +
            "FROM transportation_schedules ts " +
            "JOIN transportations t ON ts.transportation_id = t.id " +
            "JOIN transportation_brands tb ON t.transportation_brand_id = tb.id " +
            "JOIN transportation_types tt ON t.transportation_type_id = tt.id " +
            "WHERE (ts.from_location = :fromLocation) " +
            "AND (ts.to_location = :toLocation) " +
            "AND (ts.departure_time BETWEEN :departureTime AND (:departureTime + INTERVAL 1 HOUR)) " +
            "AND (ts.arrival_time BETWEEN (:arrivalTime - INTERVAL 1 HOUR) AND :arrivalTime) " +
            "AND (t.amount_seat >= :amountSeat) " +
            "AND (:price IS NULL OR ts.unit_price <= :price) " +
            "AND (coalesce(:transportationTypeIdList) IS NULL OR tt.id IN (:transportationTypeIdList)) " +
            "AND (coalesce(:listOfVehicleManufacturers) IS NULL OR tb.id IN (:listOfVehicleManufacturers)) ",
            countQuery = "SELECT COUNT(*) FROM (" +
                    "SELECT ts.id " +
                    "FROM transportation_schedules ts " +
                    "INNER JOIN transportations t ON ts.transportation_id = t.id " +
                    "INNER JOIN transportation_brands tb ON t.transportation_brand_id = tb.id " +
                    "INNER JOIN transportation_types tt ON t.transportation_type_id = tt.id " +
                    "WHERE (ts.from_location = :fromLocation) " +
                    "AND (ts.to_location = :toLocation) " +
                    "AND (ts.departure_time BETWEEN :departureTime AND (:departureTime + INTERVAL 1 HOUR)) " +
                    "AND (ts.arrival_time BETWEEN (:arrivalTime - INTERVAL 1 HOUR) AND :arrivalTime) " +
                    "AND (t.amount_seat >= :amountSeat) " +
                    "AND (:price IS NULL OR ts.unit_price <= :price) " +
                    "AND (coalesce(:transportationTypeIdList) IS NULL OR tt.id IN (:transportationTypeIdList)) " +
                    "AND (coalesce(:listOfVehicleManufacturers) IS NULL OR tb.id IN (:listOfVehicleManufacturers)) " +
                    "GROUP BY ts.id) AS temp",
            nativeQuery = true)
    Page<TransportationSchedules> findTransportationSchedulesWithFilter(
            @Param("fromLocation") String fromLocation,
            @Param("toLocation") String toLocation,
            @Param("departureTime") Timestamp departureTime,
            @Param("arrivalTime") Timestamp arrivalTime,
            @Param("amountSeat") Integer amountSeat,
            @Param("price") Integer price,
            @Param("transportationTypeIdList") List<Integer> transportationTypeIdList,
            @Param("listOfVehicleManufacturers") List<String> listOfVehicleManufacturers,
            Pageable pageable);

    @Query(value = "SELECT * FROM transportation_schedules WHERE departure_time <= NOW() AND arrival_time >= NOW() AND is_status <> 1;", nativeQuery = true)
    List<TransportationSchedules> findTripInProgress();

    @Query(value = "SELECT * FROM transportation_schedules WHERE arrival_time < NOW() AND is_status <> 2;", nativeQuery = true)
    List<TransportationSchedules> findTripCompleted();

}