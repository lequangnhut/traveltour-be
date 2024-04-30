package com.main.traveltour.repository;

import com.main.traveltour.entity.RequestCar;
import com.main.traveltour.entity.TransportationSchedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface RequestCarRepository extends JpaRepository<RequestCar, Integer> {

    Page<RequestCar> findAllByOrderByIsAcceptedAsc(Pageable pageable);

    List<RequestCar> findAllByTourDetailId(String tourDetailId);

    @Query("SELECT rqc FROM RequestCar rqc " +
            "JOIN rqc.requestCarDetailsById rqcd " +
            "JOIN rqcd.transportationSchedulesByTransportationScheduleId tsc " +
            "JOIN tsc.transportationsByTransportationId tp " +
            "JOIN tp.transportationBrandsByTransportationBrandId br " +
            "WHERE rqc.id = :requestCarId AND br.id = :transportBrandId AND (rqcd.isAccepted = 0)")
    RequestCar findTransportBrandSubmitted(@Param("requestCarId") Integer requestCarId, @Param("transportBrandId") String transportBrandId);

    @Query("SELECT rc FROM RequestCar rc " +
            "LEFT JOIN rc.requestCarDetailsById rcd " +
            "LEFT JOIN rcd.transportationSchedulesByTransportationScheduleId ts " +
            "LEFT JOIN ts.transportationsByTransportationId t " +
            "LEFT JOIN t.transportationBrandsByTransportationBrandId tb " +
            "LEFT JOIN t.transportationTypesByTransportationTypeId tt " +
            "WHERE (:fromLocation IS NULL OR rc.fromLocation LIKE %:fromLocation%) " +
            "AND (:toLocation IS NULL OR rc.toLocation LIKE %:toLocation%) " +
            "AND (:listOfVehicleManufacturers IS NULL OR tb.id IN (:listOfVehicleManufacturers)) " +
            "AND (:mediaTypeList IS NULL OR tt.id IN (:mediaTypeList))" +
            "AND (:seatTypeList IS NULL OR rc.isTransportBed IN (:seatTypeList))")
    Page<RequestCar> findAllRequestCarsFilters(
            @Param("fromLocation") String fromLocation,
            @Param("toLocation") String toLocation,
            @Param("mediaTypeList") List<Integer> mediaTypeList,
            @Param("listOfVehicleManufacturers") List<String> listOfVehicleManufacturers,
            @Param("seatTypeList") List<Boolean> seatTypeList,
            Pageable pageable);

}
