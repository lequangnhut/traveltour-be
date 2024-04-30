package com.main.traveltour.repository;

import com.main.traveltour.entity.OrderVisitDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderVisitDetailsRepository extends JpaRepository<OrderVisitDetails, Integer> {
    @Query("SELECT ovd FROM OrderVisits ov " +
            "JOIN ov.tourDetails td " +
            "JOIN ov.orderVisitDetailsById ovd " +
            "JOIN ov.visitLocationsByVisitLocationId vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE (td.id = :tourDetailId) AND " +
            "(vl.id = :visitId) AND " +
            "(:orderVisitStatus IS NULL OR ov.orderStatus = :orderVisitStatus)")
    List<OrderVisitDetails> findOrderVisitDetailByTourDetailIdAndVisitId(@Param("tourDetailId") String tourDetailId,
                                                                         @Param("visitId") String visitId,
                                                                         @Param("orderVisitStatus") Integer orderVisitStatus);

    @Query("SELECT ovd FROM OrderVisitDetails ovd " +
            "JOIN ovd.orderVisitsByOrderVisitId ov " +
            "WHERE ov.id = :orderId")
    List<OrderVisitDetails> findOrderVisitDetailByOrderVisitId(@Param("orderId") String orderId);

    List<OrderVisitDetails> findByOrderVisitId(String id);

    @Query("SELECT ovd FROM OrderVisits ov " +
            "JOIN ov.tourDetails td " +
            "JOIN ov.orderVisitDetailsById ovd " +
            "JOIN ov.visitLocationsByVisitLocationId vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE (td.id = :tourDetailId) AND " +
            "(vl.id = :visitId)")
    List<OrderVisitDetails> findOrderVisitDetailByTourDetailIdAndVisitIdForGuide(@Param("tourDetailId") String tourDetailId,
                                                                                 @Param("visitId") String visitId);
}