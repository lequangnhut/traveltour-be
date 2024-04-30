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

    @Query("SELECT COALESCE(MAX(td.id), 'TD0000') FROM TourDetails td")
    String getMaxCodeTourDetailId();

    TourDetails findById(String id);

    TourDetails getById(String id);

    @Query("SELECT td FROM TourDetails td " +
            "WHERE td.tourDetailStatus != 2 " +
            "AND td.tourDetailStatus != 3 " +
            "AND td.tourDetailStatus != 4")
    List<TourDetails> getAllTourDetail();

    @Query("SELECT td FROM TourDetails td " +
            "LEFT JOIN td.bookingToursById bt " +
            "WHERE td.tourDetailStatus != 2 " +
            "AND td.tourDetailStatus != 3 " +
            "AND td.tourDetailStatus != 4 " +
            "GROUP BY td.id " +
            "ORDER BY COUNT(bt.id) DESC")
    List<TourDetails> findAllOrderByBookingCountDesc();

    @Query("SELECT td FROM TourDetails td " +
            "LEFT JOIN td.bookingToursById bt " +
            "WHERE td.tourDetailStatus != 2 " +
            "AND td.tourDetailStatus != 3 " +
            "AND td.tourDetailStatus != 4 " +
            "AND (:departureArrives IS NULL OR " +
            "(UPPER(td.toursByTourId.tourName) LIKE %:departureArrives% OR " +
            "UPPER(td.toursByTourId.tourTypesByTourTypeId.tourTypeName) LIKE %:departureArrives% OR " +
            "UPPER(td.toLocation) LIKE %:departureArrives%)) " +
            "AND (:departureFrom IS NULL OR td.fromLocation LIKE :departureFrom) " +
            "AND (:departureDate IS NULL OR DATE(td.departureDate) >= DATE(:departureDate)) " +
            "AND (:price IS NULL OR td.unitPrice <= :price) " +
            "AND (:listOfIdsOfTourTypes IS NULL OR td.toursByTourId.tourTypesByTourTypeId.id IN :listOfIdsOfTourTypes) " +
            "GROUP BY td.id " +
            "ORDER BY COUNT(bt.id) DESC")
    List<TourDetails> getAListOfPopularTours(@Param("departureArrives") String departureArrives,
                                             @Param("departureFrom") String departureFrom,
                                             @Param("departureDate") Date departureDate,
                                             @Param("price") BigDecimal price,
                                             @Param("listOfIdsOfTourTypes") List<Integer> listOfIdsOfTourTypes);

    @Query("SELECT tt.id FROM TourTypes tt " +
            "jOIN tt.toursById t " +
            "jOIN t.tourDetailsById td " +
            "WHERE t.tourName LIKE %:departureArrives% " +
            "OR td.toLocation LIKE %:departureArrives% " +
            "OR tt.tourTypeName LIKE %:departureArrives% " +
            "GROUP BY tt.id")
    List<Integer> getListOfIdsOfTourTypes(@Param("departureArrives") String departureArrives);


    @Query("SELECT td FROM TourDetails td " +
            "WHERE td.tourDetailStatus = 1 ")
    List<TourDetails> findAllTourDetailUseRequestCar();

    @Query("SELECT td FROM TourDetails td " +
            "WHERE (:searchTerm IS NULL OR " +
            "UPPER(td.id) LIKE %:searchTerm% OR " +
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
            "GROUP BY td.id " +
            "ORDER BY td.id ASC")
    List<TourDetails> getAllJoinBooking();

    @Query("SELECT td FROM TourDetails td " +
            "WHERE td.tourDetailStatus != 2 " +
            "AND td.tourDetailStatus != 3 " +
            "AND td.tourDetailStatus != 4")
    Page<TourDetails> findAllTourDetail(Pageable pageable);

    @Query("SELECT td FROM TourDetails td " +
            "WHERE td.tourDetailStatus = :tourDetailStatus")
    Page<TourDetails> findAllTourDetailStaff(@Param("tourDetailStatus") Integer tourDetailStatus,
                                             Pageable pageable);

    @Query("SELECT td FROM TourDetails td " +
            "WHERE td.tourDetailStatus = :tourDetailStatus AND " +
            "UPPER(td.id) LIKE %:searchTerm% OR " +
            "UPPER(td.id) LIKE %:searchTerm% OR " +
            "UPPER(td.toursByTourId.tourName) LIKE %:searchTerm% OR " +
            "UPPER(td.fromLocation) LIKE %:searchTerm% OR " +
            "UPPER(td.toLocation) LIKE %:searchTerm%")
    Page<TourDetails> findTourDetailsByTourNameOrFromLocationOrToLocationContainingIgnoreCase(@Param("tourDetailStatus") Integer tourDetailStatus,
                                                                                              @Param("searchTerm") String searchTerm,
                                                                                              Pageable pageable);

    @Query("SELECT td " +
            "FROM TourDetails td " +
            "JOIN td.toursByTourId t " +
            "JOIN t.tourTypesByTourTypeId ty " +
            "WHERE td.tourDetailStatus != 2 AND td.tourDetailStatus != 3 AND td.tourDetailStatus != 4" +
            "AND (:departureArrives IS NULL OR " +
            "(UPPER(t.tourName) LIKE %:departureArrives% OR " +
            "UPPER(t.id) LIKE %:departureArrives% OR " +
            "UPPER(td.id) LIKE %:departureArrives% OR " +
            "UPPER(ty.tourTypeName) LIKE %:departureArrives% OR " +
            "UPPER(td.toLocation) LIKE %:departureArrives%)) " +
            "AND (:departureFrom IS NULL OR td.fromLocation LIKE :departureFrom) " +
            "AND (:numberOfPeople IS NULL OR :numberOfPeople <= (td.numberOfGuests - td.bookedSeat)) " +
            "AND (:departureDate IS NULL OR DATE(td.departureDate) >= DATE(:departureDate)) " +
            "AND (:price IS NULL OR td.unitPrice <= :price) " +
            "AND (:tourTypesByTourTypeId IS NULL OR ty.id IN (:tourTypesByTourTypeId))")
    Page<TourDetails> findTourDetailWithFilter(
            @Param("departureArrives") String departureArrives,
            @Param("departureFrom") String departureFrom,
            @Param("numberOfPeople") Integer numberOfPeople,
            @Param("departureDate") Date departureDate,
            @Param("price") BigDecimal price,
            @Param("tourTypesByTourTypeId") List<Integer> tourTypesByTourTypeId,
            Pageable pageable);

    @Query("SELECT td " +
            "FROM TourDetails td " +
            "JOIN td.toursByTourId t " +
            "JOIN t.tourTypesByTourTypeId ty " +
            "WHERE td.tourDetailStatus != 2 AND td.tourDetailStatus != 3 AND td.tourDetailStatus != 4 " +
            "AND (:tourDetailIdList IS NULL OR td.id IN :tourDetailIdList) " +
            "AND (:numberOfPeople IS NULL OR :numberOfPeople <= (td.numberOfGuests - td.bookedSeat)) " +
            "AND (:departureDate IS NULL OR DATE(td.departureDate) >= DATE(:departureDate)) " +
            "AND (:price IS NULL OR td.unitPrice <= :price) " +
            "AND (:tourTypesByTourTypeId IS NULL OR ty.id IN :tourTypesByTourTypeId)")
    Page<TourDetails> findTourDetailWithInFilter(
            @Param("tourDetailIdList") List<String> tourDetailIdList,
            @Param("numberOfPeople") Integer numberOfPeople,
            @Param("departureDate") Date departureDate,
            @Param("price") BigDecimal price,
            @Param("tourTypesByTourTypeId") List<Integer> tourTypesByTourTypeId,
            Pageable pageable);

    @Query("SELECT td.id " +
            "FROM TourDetails td " +
            "WHERE td.tourDetailStatus != 2 AND td.tourDetailStatus != 3 AND td.tourDetailStatus != 4 " +
            "AND (:cleanArrives IS NULL OR UPPER(td.toursByTourId.tourName) LIKE %:cleanArrives% " +
            "OR UPPER(td.toLocation) LIKE %:cleanArrives%) " +
            "AND (:cleanFrom IS NULL OR UPPER(td.fromLocation) LIKE %:cleanFrom%) " +
            "AND (:tourDetailIdList IS NULL OR td.id NOT IN :tourDetailIdList)" +
            "GROUP BY td.id")
    List<String> findTourDetailIdList(
            @Param("cleanArrives") String cleanArrives,
            @Param("cleanFrom") String cleanFrom,
            @Param("tourDetailIdList") List<String> tourDetailIdList
    );


    @Query(value = "SELECT td.id as tour_id, t.tour_name, t.tour_img, td.unit_price, COUNT(td.id) as tour_detail_count " +
            "FROM tours t LEFT JOIN tour_details td ON t.id = td.tour_id " +
            "GROUP BY td.id, t.tour_name, t.tour_img, td.unit_price " +
            "ORDER BY tour_detail_count DESC limit 5;", nativeQuery = true)
    List<Object[]> findTourDetailTrend();

    //SELECT * FROM tour_details WHERE departure_date <= NOW() AND arrival_date >= NOW() AND tour_detail_status <> 2;
    @Query(value = "SELECT * FROM tour_details WHERE departure_date <= NOW() AND arrival_date >= NOW() AND tour_detail_status NOT IN (2, 4);", nativeQuery = true)
    List<TourDetails> findTourInProgress();

    //SELECT * FROM tour_details WHERE arrival_date < NOW() AND tour_detail_status <> 3;
    @Query(value = "SELECT * FROM tour_details WHERE arrival_date < NOW() AND tour_detail_status NOT IN (3, 4);", nativeQuery = true)
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

    @Query("SELECT t FROM TourDetails t " +
            "WHERE t.guideId = :guideId AND t.tourDetailStatus = :tourStatus " +
            "AND (:searchTerm IS NULL OR " +
            "(UPPER(t.id) LIKE %:searchTerm% OR " +
            "UPPER(t.toursByTourId.tourName) LIKE %:searchTerm% OR " +
            "UPPER(t.toursByTourId.tourTypesByTourTypeId.tourTypeName) LIKE %:searchTerm% OR " +
            "UPPER(t.tourDetailNotes) LIKE %:searchTerm% OR " +
            "UPPER(t.fromLocation) LIKE %:searchTerm% OR " +
            "UPPER(t.toLocation) LIKE %:searchTerm%))")
    Page<TourDetails> findTourByGuideAndStatus(@Param("guideId") Integer guideId, @Param("tourStatus") Integer tourStatus, @Param("searchTerm") String searchTerm, Pageable pageable);


}