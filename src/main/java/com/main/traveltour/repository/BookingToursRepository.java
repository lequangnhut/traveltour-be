package com.main.traveltour.repository;

import com.main.traveltour.entity.BookingTours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public interface BookingToursRepository extends JpaRepository<BookingTours, Integer> {

    BookingTours findById(String bookingTourId);

    @Query("SELECT bt FROM BookingTours bt " +
            "LEFT JOIN bt.invoicesById inv " +
            "WHERE bt.orderStatus = :orderStatus")
    Page<BookingTours> findAllBookingTours(@Param("orderStatus") Integer orderStatus,
                                           Pageable pageable);

    @Query("SELECT bt FROM BookingTours bt " +
            "LEFT JOIN bt.invoicesById inv " +
            "WHERE bt.orderStatus = :orderStatus AND " +
            "(UPPER(bt.customerName) LIKE %:searchTerm% OR " +
            "UPPER(bt.customerCitizenCard) LIKE %:searchTerm% OR " +
            "UPPER(bt.tourDetailsByTourDetailId.toursByTourId.tourName) LIKE %:searchTerm% OR " +
            "UPPER(bt.id) LIKE %:searchTerm% OR " +
            "UPPER(bt.customerPhone) LIKE %:searchTerm% OR " +
            "UPPER(bt.customerEmail) LIKE %:searchTerm% OR " +
            "CAST(bt.orderTotal AS string) LIKE %:searchTerm%)")
    Page<BookingTours> findBySearchTerm(@Param("orderStatus") Integer orderStatus,
                                        @Param("searchTerm") String searchTerm,
                                        Pageable pageable);

    @Query("SELECT bt FROM BookingTours bt WHERE (:orderStatus IS NULL OR bt.orderStatus = :orderStatus) AND bt.customerEmail = :email")
    Page<BookingTours> findAllBookingToursByUserId(@Param("orderStatus") Integer orderStatus, @Param("email") String email, Pageable pageable);

    //doanh thu cùa dashboard
    @Query("SELECT new map(YEAR(bt.dateCreated) AS year, COALESCE(SUM(bt.orderTotal), 0) AS totalRevenue) " +
            "FROM BookingTours bt " +
            "WHERE bt.orderStatus = 1 " +
            "AND YEAR(bt.dateCreated) >= YEAR(CURRENT_TIMESTAMP) - 3 " +
            "GROUP BY YEAR(bt.dateCreated) " +
            "ORDER BY YEAR(bt.dateCreated) DESC")
    List<Map<String, Object>> getBookingTourRevenueByYear();

    @Query("SELECT new map(YEAR(co.dateCreated) AS year, COALESCE(SUM(co.depositPrice), 0) AS totalRevenue) " +
            "FROM CancelOrders co " +
            "WHERE co.categogy = 0 " +
            "AND YEAR(co.dateCreated) >= YEAR(CURRENT_TIMESTAMP) - 3 " +
            "GROUP BY YEAR(co.dateCreated) " +
            "ORDER BY YEAR(co.dateCreated) DESC")
    List<Map<String, Object>> getCancelOrderRevenueByYear();

    //doanh thu của admin

    @Query("SELECT COALESCE(SUM(co.depositPrice), 0) " +
            "FROM CancelOrders co " +
            "WHERE MONTH(co.dateCreated) = :month " +
            "AND YEAR(co.dateCreated) = :year " +
            "AND co.categogy = 0")
    BigDecimal getRevenueForMonthAndYear(@Param("year") Integer year, @Param("month") Integer month);


    @Query("SELECT COALESCE(SUM(bt.orderTotal), 0) as revenue " +
            "FROM BookingTours bt " +
            "WHERE MONTH(bt.dateCreated) = :month " +
            "AND YEAR(bt.dateCreated) = :year " +
            "AND bt.orderStatus = 1")
    BigDecimal getRevenueForMonth(@Param("year") Integer year, @Param("month") Integer month);

    default List<BigDecimal> revenueOf12MonthsOfTheYearFromTourBooking(Integer year) {
        List<BigDecimal> revenues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            BigDecimal revenueFromBookingTours = getRevenueForMonth(year, i);
            BigDecimal revenueFromCancelOrders = getRevenueForMonthAndYear(year, i);
            BigDecimal totalRevenue = revenueFromBookingTours.add(revenueFromCancelOrders);
            revenues.add(totalRevenue);
        }
        return revenues;
    }

    @Query("SELECT DISTINCT YEAR(bt.dateCreated) FROM BookingTours bt ORDER BY YEAR(bt.dateCreated) DESC")
    List<Integer> getAllYearColumn();

    @Query("SELECT DISTINCT YEAR(a.dateCreated) FROM Agencies a ORDER BY YEAR(a.dateCreated) DESC")
    List<Integer> getAllYearPie();

}