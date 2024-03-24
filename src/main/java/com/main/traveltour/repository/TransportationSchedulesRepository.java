package com.main.traveltour.repository;

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

    @Query("SELECT t FROM TransportationSchedules t " +
            "JOIN t.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE tpb.id = :transportBrandId AND t.tripType = false")
    List<TransportationSchedules> findAllScheduleByBrandId(@Param("transportBrandId") String transportBrandId);

    @Query("SELECT t FROM TransportationSchedules t " +
            "JOIN t.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE t.tripType = false")
    List<TransportationSchedules> getAllFromLocationAndToLocation();

    @Query("SELECT sc FROM TransportationSchedules sc " +
            "JOIN sc.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId br " +
            "WHERE br.id = :brandId AND sc.isStatus = 0 AND sc.isActive = true AND sc.tripType = false")
    Page<TransportationSchedules> findAllTransportScheduleCus(Pageable pageable, @Param("brandId") String brandId);

    @Query("SELECT sc FROM TransportationSchedules sc " +
            "JOIN sc.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE tpb.id = :transportBrandId AND sc.isStatus != 4 AND sc.isStatus != 3 AND sc.isActive = true")
    Page<TransportationSchedules> findAllSchedules(@Param("transportBrandId") String transportBrandId, Pageable pageable);

    @Query("SELECT sc FROM TransportationSchedules sc " +
            "JOIN sc.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE LOWER(CAST(sc.unitPrice AS STRING)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(sc.toLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(sc.fromLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(sc.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND tpb.id = :transportBrandId AND sc.isStatus != 4 AND sc.isStatus != 3 AND sc.isActive = true")
    Page<TransportationSchedules> findAllSchedulesWithSearch(@Param("transportBrandId") String transportBrandId, @Param("searchTerm") String searchTerm, Pageable pageable);


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

    //fill phương tiện theo tour
    @Query("SELECT ts FROM TransportationSchedules ts " +
            "JOIN ts.transportationsByTransportationId t " +
            "JOIN ts.orderTransportationsById ot " +
            "JOIN ot.tourDetails td " +
            "WHERE (td.id = :tourDetailId) AND " +
            "(ot.orderStatus = :orderStatus) AND " +
            "(:searchTerm IS NULL OR (UPPER(ts.fromLocation) LIKE %:searchTerm% OR " +
            "UPPER(ts.toLocation) LIKE %:searchTerm% OR " +
            "UPPER(ts.transportationsByTransportationId.licensePlate) LIKE %:searchTerm% OR " +
            "CAST(ts.bookedSeat AS string) LIKE %:searchTerm% OR " +
            "UPPER(ts.transportationsByTransportationId.transportationBrandsByTransportationBrandId.transportationBrandName) LIKE %:searchTerm% OR " +
            "UPPER(ts.transportationsByTransportationId.transportationTypesByTransportationTypeId.transportationTypeName) LIKE %:searchTerm% OR " +
            "CAST(ts.transportationsByTransportationId.amountSeat AS string) LIKE %:searchTerm% OR " +
            "CAST(ts.unitPrice AS string) LIKE %:searchTerm%)) " +
            "GROUP BY ts.id")
    Page<TransportationSchedules> findTransportationSchedulesByTourDetailId(@Param("tourDetailId") String tourDetailId,
                                                                            @Param("orderStatus") Integer orderStatus, @Param("searchTerm") String searchTerm,
                                                                            Pageable pageable);
    @Query("SELECT ts FROM TransportationSchedules ts " +
            "where (ts.transportationId = :transId) and " +
            "(ts.isActive = :isActive)")
    Page<TransportationSchedules> findScheduleByTransId (@Param("isActive") Boolean isActive, @Param("transId") String transId, Pageable pageable);

    @Query("SELECT ts FROM TransportationSchedules ts " +
            "where (ts.transportationId = :transId) and " +
            "(ts.isActive = :isActive) " +
            "and (:searchTerm is null or " +
            "(LOWER(ts.fromLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "or LOWER(ts.toLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%'))))")
    Page<TransportationSchedules> findScheduleByTransIdByName (@Param("isActive") Boolean isActive, @Param("transId") String transId, Pageable pageable, String searchTerm);
}