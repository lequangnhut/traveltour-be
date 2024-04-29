package com.main.traveltour.repository;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface TransportationSchedulesRepository extends JpaRepository<TransportationSchedules, Integer> {

    @Query("SELECT MAX(t.id) FROM TransportationSchedules t")
    String fixMaxCode();

    TransportationSchedules findById(String id);

    List<TransportationSchedules> findByTransportationId(String transportId);

    @Query("SELECT tsc FROM TransportationSchedules tsc " +
            "JOIN tsc.requestCarDetailsById rqcd " +
            "WHERE rqcd.transportationScheduleId = :transportationScheduleId AND (rqcd.isAccepted = 0 OR rqcd.isAccepted = 1)")
    TransportationSchedules findCarIsSubmittedAgent(@Param("transportationScheduleId") String transportationScheduleId);

    @Query("SELECT tsc FROM TransportationSchedules tsc " +
            "JOIN tsc.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE tpb.id = :transportBrandId " +
            "AND tsc.tripType = true " +
            "AND tsc.isStatus = 4 " + // 4 chuyến đi đang trống
            "AND tsc.isActive = true")
    List<TransportationSchedules> findAllScheduleByBrandIdRequestCarAgent(@Param("transportBrandId") String transportBrandId);

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
            "JOIN tp.transportationTypesByTransportationTypeId ty " +
            "WHERE (br.id = :brandId AND sc.isStatus = 0 AND sc.isActive = true AND sc.tripType = false) " +
            "AND (:price IS NULL OR sc.unitPrice <= (:price)) " +
            "AND (:fromLocation IS NULL OR sc.fromLocation LIKE %:fromLocation%) " +
            "AND (:toLocation IS NULL OR sc.toLocation LIKE %:toLocation%) " +
            "AND (:checkInDateFiller IS NULL OR sc.departureTime >= :checkInDateFiller) " +
            "AND (:listOfVehicleManufacturers IS NULL OR br.id IN (:listOfVehicleManufacturers)) " +
            "AND (:mediaTypeList IS NULL OR ty.id IN (:mediaTypeList))" +
            "GROUP BY sc")
    Page<TransportationSchedules> findAllTransportScheduleCusFilters(
            @Param("brandId") String brandId,
            @Param("price") BigDecimal price,
            @Param("fromLocation") String fromLocation,
            @Param("toLocation") String toLocation,
            @Param("checkInDateFiller") Date checkInDateFiller,
            @Param("mediaTypeList") List<Integer> mediaTypeList,
            @Param("listOfVehicleManufacturers") List<String> listOfVehicleManufacturers,
            Pageable pageable);

    @Query("SELECT br FROM TransportationBrands br " +
            "JOIN br.transportationsById tp " +
            "JOIN tp.transportationTypesByTransportationTypeId ty " +
            "JOIN tp.transportationSchedulesById sc " +
            "WHERE (br.isAccepted = true AND br.isActive = true AND sc.tripType = false) AND " +
            "(:searchTerm IS NULL OR (UPPER(br.agenciesByAgenciesId.nameAgency) LIKE %:searchTerm% OR " +
            "UPPER(br.id) LIKE %:searchTerm% OR " +
            "UPPER(br.transportationBrandName) LIKE %:searchTerm% OR " +
            "UPPER(br.agenciesByAgenciesId.phone) LIKE %:searchTerm% OR " +
            "UPPER(br.agenciesByAgenciesId.province) LIKE %:searchTerm% OR " +
            "UPPER(br.agenciesByAgenciesId.ward) LIKE %:searchTerm% OR " +
            "UPPER(br.agenciesByAgenciesId.district) LIKE %:searchTerm% OR " +
            "UPPER(br.agenciesByAgenciesId.representativeName) LIKE %:searchTerm% OR " +
            "UPPER(CONCAT(br.agenciesByAgenciesId.ward, ' - ' ,br.agenciesByAgenciesId.district, ' - ' , br.agenciesByAgenciesId.province)) LIKE %:searchTerm% OR " +
            "UPPER(br.agenciesByAgenciesId.address) LIKE %:searchTerm%)) " +
            "AND (:price IS NULL OR sc.unitPrice <= (:price)) " +
            "AND (:fromLocation IS NULL OR sc.fromLocation LIKE %:fromLocation%) " +
            "AND (:toLocation IS NULL OR sc.toLocation LIKE %:toLocation%) " +
            "AND (:checkInDateFiller IS NULL OR sc.departureTime >= :checkInDateFiller) " +
            "AND (:listOfVehicleManufacturers IS NULL OR br.id IN (:listOfVehicleManufacturers)) " +
            "AND (:mediaTypeList IS NULL OR ty.id IN (:mediaTypeList))" +
            "GROUP BY br")
    Page<TransportationBrands> findAllCustomerWithFilter(
            @Param("searchTerm") String searchTerm,
            @Param("price") BigDecimal price,
            @Param("fromLocation") String fromLocation,
            @Param("toLocation") String toLocation,
            @Param("checkInDateFiller") Date checkInDateFiller,
            @Param("mediaTypeList") List<Integer> mediaTypeList,
            @Param("listOfVehicleManufacturers") List<String> listOfVehicleManufacturers,
            Pageable pageable);

    @Query("SELECT tsc FROM TransportationSchedules tsc " +
            "JOIN tsc.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE tpb.id = :transportBrandId " +
            "AND tsc.tripType = :tripType " +
            "AND tsc.isStatus != 3 " +
            "AND tsc.isActive = true")
    Page<TransportationSchedules> findAllSchedulesAgent(@Param("transportBrandId") String transportBrandId,
                                                        @Param("tripType") Boolean tripType,
                                                        Pageable pageable);

    @Query("SELECT sc FROM TransportationSchedules sc " +
            "JOIN sc.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId tpb " +
            "WHERE LOWER(CAST(sc.unitPrice AS STRING)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(sc.toLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(sc.fromLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(sc.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND tpb.id = :transportBrandId " +
            "AND sc.tripType = :tripType " +
            "AND sc.isActive = true")
    Page<TransportationSchedules> findAllSchedulesAgentWithSearch(@Param("transportBrandId") String transportBrandId,
                                                                  @Param("tripType") Boolean tripType,
                                                                  @Param("searchTerm") String searchTerm,
                                                                  Pageable pageable);

    @Query(value = "SELECT ts.* " +
            "FROM transportation_schedules ts " +
            "JOIN transportations t ON ts.transportation_id = t.id " +
            "JOIN transportation_brands tb ON t.transportation_brand_id = tb.id " +
            "JOIN transportation_types tt ON t.transportation_type_id = tt.id " +
            "WHERE (ts.from_location LIKE :fromLocation) " +
            "AND (ts.to_location LIKE :toLocation) " +
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
                    "WHERE (ts.from_location LIKE :fromLocation) " +
                    "AND (ts.to_location LIKE :toLocation) " +
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
            "(:orderStatus IS NULL OR " +
            "(ot.orderStatus = :orderStatus)) AND " +
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
                                                                            @Param("orderStatus") Integer orderStatus,
                                                                            @Param("searchTerm") String searchTerm,
                                                                            Pageable pageable);

    @Query("SELECT ts FROM TransportationSchedules ts " +
            "where (ts.transportationId = :transId) and " +
            "(ts.isActive = :isActive)")
    Page<TransportationSchedules> findScheduleByTransId(@Param("isActive") Boolean isActive, @Param("transId") String transId, Pageable pageable);

    @Query("SELECT ts FROM TransportationSchedules ts " +
            "where (ts.transportationId = :transId) and " +
            "(ts.isActive = :isActive) " +
            "and (:searchTerm is null or " +
            "(LOWER(ts.fromLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "or LOWER(ts.toLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%'))))")
    Page<TransportationSchedules> findScheduleByTransIdByName(@Param("isActive") Boolean isActive, @Param("transId") String transId, Pageable pageable, String searchTerm);

    @Query("SELECT COUNT(ts) FROM TransportationSchedules ts")
    Long countTransportationSchedules();

    @Query("SELECT ts FROM TransportationSchedules ts " +
            "JOIN ts.transportationsByTransportationId t " +
            "JOIN ts.orderTransportationsById ot " +
            "JOIN ot.tourDetails td " +
            "WHERE (td.id = :tourDetailId) AND " +
            "((ot.orderStatus = 0) OR (ot.orderStatus = 1)) AND " +
            "(:searchTerm IS NULL OR (UPPER(ts.fromLocation) LIKE %:searchTerm% OR " +
            "UPPER(ts.toLocation) LIKE %:searchTerm% OR " +
            "UPPER(ts.transportationsByTransportationId.licensePlate) LIKE %:searchTerm% OR " +
            "CAST(ts.bookedSeat AS string) LIKE %:searchTerm% OR " +
            "UPPER(ts.transportationsByTransportationId.transportationBrandsByTransportationBrandId.transportationBrandName) LIKE %:searchTerm% OR " +
            "UPPER(ts.transportationsByTransportationId.transportationTypesByTransportationTypeId.transportationTypeName) LIKE %:searchTerm% OR " +
            "CAST(ts.transportationsByTransportationId.amountSeat AS string) LIKE %:searchTerm% OR " +
            "CAST(ts.unitPrice AS string) LIKE %:searchTerm%)) " +
            "GROUP BY ts.id")
    Page<TransportationSchedules> findTransportationSchedulesByTourDetailIdForGuide(@Param("tourDetailId") String tourDetailId,
                                                                                    @Param("searchTerm") String searchTerm,
                                                                                    Pageable pageable);
}