package com.main.traveltour.repository;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.entity.TransportationBrands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;

import java.util.List;

public interface TransportationBrandsRepository extends JpaRepository<TransportationBrands, Integer> {

    @Query(value = "SELECT MAX(trans.id) FROM TransportationBrands trans")
    String findMaxCode();

    List<TransportationBrands> findAllByAgenciesIdAndIsActiveTrue(int agenciesId);

    TransportationBrands findByAgenciesId(int agenciesId);

    TransportationBrands findById(String transportBrandId);

    List<TransportationBrands> findAllByIsActiveIsTrueAndIsAcceptedIsTrue();

    @Query("SELECT br FROM TransportationBrands br " +
            "JOIN br.transportationsById tp " +
            "JOIN tp.transportationSchedulesById sc " +
            "WHERE br.isAccepted = true AND br.isActive = true AND sc.tripType = false " +
            "GROUP BY br")
    Page<TransportationBrands> findAllCustomer(Pageable pageable);


    @Query("SELECT br FROM TransportationBrands br " +
            "JOIN br.transportationsById tp " +
            "JOIN tp.transportationTypesByTransportationTypeId ty " +
            "JOIN tp.transportationSchedulesById sc " +
            "WHERE (br.isAccepted = true AND br.isActive = true AND sc.tripType = false) AND " +
            "(:searchTerm IS NULL OR (UPPER(br.agenciesByAgenciesId.nameAgency) LIKE %:searchTerm% OR " +
            "UPPER(br.id) LIKE %:searchTerm% OR " +
            "UPPER(tp.id) LIKE %:searchTerm% OR " +
            "UPPER(sc.id) LIKE %:searchTerm% OR " +
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
            "AND (DATE(sc.departureTime) >= DATE(:checkInDateFiller)) " +
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

    @Query("SELECT br FROM TransportationBrands br " +
            "JOIN br.transportationsById tp " +
            "JOIN tp.transportationTypesByTransportationTypeId ty " +
            "JOIN tp.transportationSchedulesById sc " +
            "JOIN sc.orderTransportationsById ord " +
            "WHERE (br.isAccepted = true AND br.isActive = true AND sc.tripType = false) " +
            "GROUP BY br")
    List<TransportationBrands> findAllCustomerDataList();

    @Query("SELECT br FROM TransportationBrands br " +
            "join br.agenciesByAgenciesId ag " +
            "WHERE (ag.isAccepted = 2) and (ag.isActive = true) " +
            "and br.isAccepted = :isAccepted AND br.isActive = true")
    Page<TransportationBrands> findAllBrandPost(@Param("isAccepted") Boolean isAccepted, Pageable pageable);

    @Query("SELECT br FROM TransportationBrands br " +
            "join br.agenciesByAgenciesId ag " +
            "WHERE (ag.isAccepted = 2) and (ag.isActive = true) " +
            "and br.isAccepted = :isAccepted AND br.isActive = true " +
            " and LOWER(br.transportationBrandName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<TransportationBrands> findAllBrandPostByName(@Param("isAccepted") Boolean isAccepted, Pageable pageable, String searchTerm);

    @Query("SELECT COALESCE(COUNT(tb), 0) FROM TransportationBrands tb " +
            "JOIN tb.agenciesByAgenciesId a " +
            "WHERE a.isActive = TRUE AND a.isAccepted = 2 " +
            "AND tb.isActive = TRUE AND tb.isAccepted = TRUE " +
            "AND YEAR(tb.dateCreated) = :year ")
    Long countTransportationBrandsChart(@Param("year") Integer year);

    @Query(value="SELECT tb.*" +
            "FROM order_transportations ot " +
            "JOIN order_transportations_details otd on ot.id = otd.order_transportations_id " +
            "JOIN transportation_schedules ts on ot.transportation_schedule_id = ts.id " +
            "JOIN transportations t on ts.transportation_id = t.id " +
            "JOIN transportation_brands tb on t.transportation_brand_id = tb.id " +
            "WHERE ot.order_status <> 2 AND tb.is_active = TRUE " +
            "GROUP BY tb.id " +
            "ORDER BY SUM(ot.id) DESC " +
            "LIMIT 3;", nativeQuery = true)
    List<TransportationBrands> find3BrandMostOrder ();

}