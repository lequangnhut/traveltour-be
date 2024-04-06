package com.main.traveltour.repository;

import com.main.traveltour.entity.TourDetails;
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
public interface TourDetailsRepository extends JpaRepository<TourDetails, Integer> {

    TourDetails findById(String id);

    TourDetails getById(String id);

    @Query("SELECT td FROM TourDetails td " +
            "WHERE td.tourDetailStatus != 4 ")
    List<TourDetails> getAllTourDetail();

    @Query("SELECT td FROM TourDetails td " +
            "WHERE td.tourDetailStatus = 1 ")
    List<TourDetails> findAllTourDetailUseRequestCar();

    @Query("SELECT td FROM TourDetails td " +
            "WHERE (:searchTerm IS NULL OR " +
            "(UPPER(td.toursByTourId.tourName) LIKE %:searchTerm% OR " +
            "UPPER(td.toursByTourId.tourTypesByTourTypeId.tourTypeName) LIKE %:searchTerm% OR " +
            "UPPER(td.tourDetailNotes) LIKE %:searchTerm% OR " +
            "UPPER(td.fromLocation) LIKE %:searchTerm% OR " +
            "UPPER(td.toLocation) LIKE %:searchTerm%)) " +
            "AND (td.tourDetailStatus = 2)")
    Page<TourDetails> getAllTourDetailByStatusIs2AndSearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT td FROM TourDetails td " +
            "JOIN td.bookingToursById bt " +
            "JOIN bt.bookingTourCustomersById btc " +
            "WHERE td.tourDetailStatus != 4 " +
            "GROUP BY td.id " +
            "ORDER BY td.id ASC")
    List<TourDetails> getAllJoinBooking();

    @Query("SELECT td FROM TourDetails td " +
            "WHERE td.tourDetailStatus != 4")
    Page<TourDetails> findAllTourDetail(Pageable pageable);

    @Query("SELECT td FROM TourDetails td WHERE " +
            "UPPER(td.toursByTourId.tourName) LIKE %:searchTerm% OR " +
            "UPPER(td.fromLocation) LIKE %:searchTerm% OR " +
            "UPPER(td.toLocation) LIKE %:searchTerm%")
    Page<TourDetails> findTourDetailsByTourNameOrFromLocationOrToLocationContainingIgnoreCase(
            @Param("searchTerm") String searchTerm, Pageable pageable);


    @Query("SELECT COALESCE(MAX(td.id), 'TD0000') FROM TourDetails td")
    String getMaxCodeTourDetailId();

    @Query("SELECT td " +
            "FROM TourDetails td " +
            "JOIN td.toursByTourId t " +
            "JOIN t.tourTypesByTourTypeId ty " +
            "WHERE (:searchTerm IS NULL OR " +
            "(UPPER(t.tourName) LIKE %:searchTerm% OR " +
            "UPPER(ty.tourTypeName) LIKE %:searchTerm% OR " +
            "UPPER(td.tourDetailNotes) LIKE %:searchTerm% OR " +
            "UPPER(td.fromLocation) LIKE %:searchTerm% OR " +
            "UPPER(td.toLocation) LIKE %:searchTerm%)) " +
            "AND (:departureDate IS NULL OR DATE(td.departureDate) >= :departureDate) " +
            "AND (:price IS NULL OR td.unitPrice <= :price) " +
            "AND (:tourTypesByTourTypeId IS NULL OR ty.id IN (:tourTypesByTourTypeId))")
    Page<TourDetails> findTourDetailWithFilter(
            @Param("searchTerm") String searchTerm,
            @Param("departureDate") Date departureDate,
            @Param("price") BigDecimal price,
            @Param("tourTypesByTourTypeId") List<Integer> tourTypesByTourTypeId,
            Pageable pageable);

    @Query(value = "SELECT td.id as tour_id, t.tour_name, t.tour_img, td.unit_price, COUNT(td.id) as tour_detail_count " +
            "FROM tours t LEFT JOIN tour_details td ON t.id = td.tour_id " +
            "GROUP BY td.id, t.tour_name, t.tour_img, td.unit_price " +
            "ORDER BY tour_detail_count DESC limit 5;", nativeQuery = true)
    List<Object[]> findTourDetailTrend();


    @Query(value = "SELECT * FROM tour_details WHERE departure_date <= NOW() AND arrival_date >= NOW() AND tour_detail_status <> 2;", nativeQuery = true)
    List<TourDetails> findTourInProgress();

    @Query(value = "SELECT * FROM tour_details WHERE arrival_date < NOW() AND tour_detail_status <> 3;", nativeQuery = true)
    List<TourDetails> findTourCompleted();

    @Query("SELECT COUNT(t) FROM TourDetails t")
    Long countTourDetails();

    @Query("SELECT DISTINCT YEAR(td.departureDate) FROM TourDetails td ORDER BY YEAR(td.departureDate) DESC")
    List<Integer> getAllYear();

    @Query(value = "WITH MONTHS AS " +
            "(SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 " +
            "                UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 " +
            "                UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) " +
            "   SELECT COALESCE(ToursCount.count, 0) as revenue" +
            "   FROM MONTHS " +
            "   LEFT JOIN (SELECT COUNT(td.id) as count, MONTH(td.departure_date) as month" +
            "   FROM tour_details td" +
            "   INNER JOIN tours on td.tour_id = tours.id" +
            "   INNER JOIN tour_types tt on tours.tour_type_id = tt.id" +
            "   WHERE (:tourTypeId IS NULL OR tt.id = :tourTypeId) " +
            "   AND YEAR(td.departure_date) = :year " +
            "   AND td.tour_detail_status = 3 OR td.tour_detail_status = 2" +
            "   GROUP BY MONTH(td.departure_date)) AS ToursCount " +
            "   ON MONTHS.month = ToursCount.month " +
            "   ORDER BY MONTHS.month", nativeQuery = true)
    List<Integer> countCompletedToursByYearAndTourType(Integer tourTypeId, Integer year);

}